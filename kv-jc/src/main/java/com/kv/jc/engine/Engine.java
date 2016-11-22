package com.kv.jc.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.kv.jc.http.json.Entity;
import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.MapConfiguration;
import com.kv.jc.http.json.Position;
import com.kv.jc.http.json.Submarine;

public class Engine {
  public static final Random r = new Random(1234567890);
  public static double wallDistance = 80;
  public static double islandDistance = 150;
  public static double torpedoDistance = 100;
  public static double submarineDistance = 50;
  public static int targetSeen = 5; // 10
  public static int followTarget = 5;
  public static Map<Long, Double> scores = new TreeMap<Long, Double>();
  
  public static Position[] idle;
  public static List<Action> getActions(Game game) {
    wallDistance = (game.getMapConfiguration().getMaxSpeed() / game.getMapConfiguration().getMaxAccelerationPerRound() + 1) * game.getMapConfiguration().getMaxSpeed();
    // TODO: turn earlier from the wall -> sonar range
    islandDistance = game.getMapConfiguration().getIslandSize() + game.getMapConfiguration().getTorpedoExplosionRadius() + wallDistance;
    torpedoDistance = game.getMapConfiguration().getTorpedoExplosionRadius() * 1.5;
    submarineDistance = game.getMapConfiguration().getSonarRange() * 1.5;
    
    targetSeen ++;
    if (targetSeen > followTarget) {
      if (idle == null) {
        idle = new Position[game.getSubmarines().size()];
      }
      for (int i = 0; i < idle.length; i++) {
        idle[i] = new Position();
        //idle[i].setX((i + 1) * game.getMapConfiguration().getWidth() / (idle.length + 2) + 0.0);
        idle[i].setX(game.getMapConfiguration().getWidth() / 2 + 0.0);
        idle[i].setY((r.nextInt(3) + 1) * game.getMapConfiguration().getHeight() / 4 + 0.0);
        //idle[i].setY(game.getMapConfiguration().getHeight() / 2 + 0.0);
      }
    }
    List<Action> result = new LinkedList<Action>();
    List<Target> targets = getTargets(game);
    targets.sort(new TargetComparator());
    int sidx = 0;
    for (Submarine submarine : game.getSubmarines()) {
      if (targets.size() > 0) {
        targetSeen = 0;
        Target target = targets.get(targets.size() - 1);
        /*double mindist = getDistance(submarine.getPosition(), target.position);
        // find closet target
        for (Target t : targets) {
          double dist = getDistance(submarine.getPosition(), t.position);
          if (dist < mindist) {
            mindist = dist;
            target = t;
          }
        }*/
        
        Shoot shoot = getShoot(game, target, submarine);
        if (shoot != null) {
          result.add(shoot);
          idle[sidx] = target.position;
          scores.put(target.id, target.score + 1.0);
          // shoot the highest score with only one submarine
          targets.remove(targets.size() - 1);
        }
      }  
      result.add(moveTo(game, idle[sidx], submarine));
      Radar radar = getRadat(game, submarine);
      if (radar != null) {
        result.add(radar);
      }
      sidx++;
    }
    return result;
  }
  
  public static List<Target> getTargets(Game game) {
    // TODO: targets should be cached
    // TODO: identify more targets
    List<Target> targets = new LinkedList<Target>();
    for (Entity entity : game.getEnemies()) {
      Double score = scores.get(entity.getId());
      if (score == null) {
        score = 0.0;
      }
      targets.add(new Target(entity.getId(), entity.getPosition(), entity.getAngle(), entity.getVelocity(), score));
    }
    return targets;
  }
  
  public static double getLength(Position position) {
    return Math.sqrt((position.getX() * position.getX()) + (position.getY() * position.getY()));
  }
  
  public static void normalize(Position position) {
    double length = getLength(position);
    position.setX(position.getX() / length);
    position.setY(position.getY() / length);
  }
  
  public static Radar getRadat(Game game, Submarine submarine) {
    if (submarine.getSonarCooldown() > 0) {
      return null;
    }
    return new Radar(submarine, 0.0);
  }
  
  public static Move moveTo(Game game, Position position, Submarine submarine) {
    System.out.println("MOVE " + submarine.getId() + " TO: " + position);
    
    Position direction = new Position();
    direction.setX(position.getX() - submarine.getPosition().getX());
    direction.setY(position.getY() - submarine.getPosition().getY());
    normalize(direction);
    double angle = Math.atan2(direction.getY(), direction.getX()) / Math.PI * 180.0;
    double distance = getDistance(position, submarine.getPosition()) + game.getMapConfiguration().getTorpedoExplosionRadius();
    double acc = game.getMapConfiguration().getMaxAccelerationPerRound();
    double velocity = acc;
    
    // close target let go
    if (distance < wallDistance && submarine.getVelocity() > game.getMapConfiguration().getMaxSpeed() - game.getMapConfiguration().getMaxAccelerationPerRound()) {
      velocity = -acc;
    }
    if (distance < game.getMapConfiguration().getTorpedoExplosionRadius() * 2) {
      angle += 180.0;
    }
    
    // close to other submarines
    for (Submarine s : game.getSubmarines()) {
      if (s.getId() != submarine.getId()) {
        double mindist = game.getMapConfiguration().getWidth();
        double dist = getDistance(submarine.getPosition(), s.getPosition());
        if (dist < submarineDistance && dist < mindist) {
          mindist = dist;
          direction.setX(s.getPosition().getX() - submarine.getPosition().getX());
          direction.setY(s.getPosition().getY() - submarine.getPosition().getY());
          normalize(direction);
          angle = Math.atan2(direction.getY(), direction.getX()) / Math.PI * 180.0;
          System.out.println("CLOSE SUBMARINE AT: " + s.getPosition());
          angle += 180.0;
        }
      }
    }
    
    // close to torpedo
    for (Entity torpedo : game.getTorpedos()) {
      double mindist = game.getMapConfiguration().getWidth();
      double dist = getDistance(submarine.getPosition(), torpedo.getPosition());
      // find the closet torpedo
      if (dist < mindist) {
        mindist = dist;
        direction.setX(torpedo.getPosition().getX() - submarine.getPosition().getX());
        direction.setY(torpedo.getPosition().getY() - submarine.getPosition().getY());
        normalize(direction);
        double torpedoToAngle = Math.atan2(direction.getY(), direction.getX()) / Math.PI * 180.0;
        double torpedoAngle = torpedo.getAngle();
        double a = getTurnAngle(submarine.getAngle(), torpedoAngle);
        System.out.println("CLOSE TORPEDO AT: " + torpedo.getPosition());
        angle = torpedoToAngle + 180.0;
        // turn orthogonal to the angle of the torpedo
        /*if (Math.abs(a) < 20.0 || Math.abs(a) > 160) {
          if (a < 0.0) { 
            angle = torpedoAngle + 90.0;
          } else {
            angle = torpedoAngle - 90.0;
          }
        }*/
        // slow down if a torpedo is close
        if (dist < torpedoDistance && submarine.getVelocity() > game.getMapConfiguration().getMaxSpeed() / 2) {
          velocity = -acc;
        }
      }
    }
    
    // close to islands
    for (Position island : game.getMapConfiguration().getIslandPositions()) {
      double mindist = game.getMapConfiguration().getWidth();
      double dist = getDistance(submarine.getPosition(), island);
      if (dist < islandDistance && dist < mindist) {
        mindist = dist;
        direction.setX(island.getX() - submarine.getPosition().getX());
        direction.setY(island.getY() - submarine.getPosition().getY());
        normalize(direction);
        double islandAngle = Math.atan2(direction.getY(), direction.getX()) / Math.PI * 180.0;
        double a = Math.abs(getTurnAngle(submarine.getAngle(), islandAngle));
        System.out.println("CLOSE ISLAND AT: " + island);
        //System.out.println("CLOSE ISLAND AT: " + island + "\t" + islandAngle);
        //System.out.println("SM: " + submarine.getId() + "\t" + submarine.getPosition() + "\t" + submarine.getAngle());
        //System.out.println("TURN ANGLE: " + a);
        if (a < 95.0) {
          angle = islandAngle + 180.0;
        }
        if (distance < wallDistance) {
          velocity = -acc;
        }
      }
    }
    
    // close to walls
    if (submarine.getPosition().getY() < wallDistance) {
      System.out.println("CLOSE BOTTOM WALL");
      angle = 90.0;
      velocity = submarine.getVelocity() > (game.getMapConfiguration().getMaxSpeed() + acc) * (submarine.getPosition().getY() / wallDistance) ? -acc : 0.0;
    } else if (submarine.getPosition().getY() > game.getMapConfiguration().getHeight() - wallDistance) {
      System.out.println("CLOSE TOP WALL");
      angle = 270.0;
      velocity = submarine.getVelocity() > (game.getMapConfiguration().getMaxSpeed() + acc) * ((game.getMapConfiguration().getHeight() - submarine.getPosition().getY()) / wallDistance) ? -acc : 0.0;
    }
    if (submarine.getPosition().getX() < wallDistance) {
      System.out.println("CLOSE LEFT WALL");
      angle = 0.0;
      velocity = submarine.getVelocity() > (game.getMapConfiguration().getMaxSpeed() + acc) * (submarine.getPosition().getX() / wallDistance) ? -acc : 0.0;
    } else if (submarine.getPosition().getX() > game.getMapConfiguration().getWidth() - wallDistance) {
      System.out.println("CLOSE RIGHT WALL");
      angle = 180.0;
      velocity = submarine.getVelocity() > (game.getMapConfiguration().getMaxSpeed() + acc) * ((game.getMapConfiguration().getWidth() - submarine.getPosition().getX()) / wallDistance) ? -acc : 0.0;
    }
    // never stop
    velocity = submarine.getVelocity() < acc ? acc : velocity;
    
    angle = getTurnAngle(submarine.getAngle(), angle);
    
    // correct angle by max angle
    if (angle < -game.getMapConfiguration().getMaxSteeringPerRound()) {
      angle = -game.getMapConfiguration().getMaxSteeringPerRound();
    }
    if (angle > game.getMapConfiguration().getMaxSteeringPerRound()) {
      angle = game.getMapConfiguration().getMaxSteeringPerRound();
    }
    //System.out.println("ANGLE: " + angle);
    return new Move(submarine, angle, velocity);
  }
  
  public static double getTurnAngle(double from, double to) {
    double result = to - from;
    if (result < 0.0) {
      result += 360.0;
    }
    if (result > 180.0) {
      result -= 360.0;
    }
    return result;
  }
  
  public static double getDistance(Position a, Position b) {
    double dx = a.getX() - b.getX();
    double dy = a.getY() - b.getY();
    dx *= dx;
    dy *= dy;
    return Math.sqrt(dx + dy);
  }
  
  public static Shoot getShoot(Game game, Target target, Submarine submarine) {
    if (submarine.getTorpedoCooldown() > 0) {
      return null;
    }
    MapConfiguration mapConfig = game.getMapConfiguration();
    double shootDistance = (mapConfig.getTorpedoRange() - 1) * mapConfig.getTorpedoSpeed();
    double distanceToTarget = getDistance(submarine.getPosition(), target.position);
    
    //System.out.println("DISTANCE: " + distanceToTarget);
    // speed per direction
    double dX = Math.cos(target.angle / 180.0 * Math.PI) * target.velocity;
    double dY = Math.sin(target.angle / 180.0 * Math.PI) * target.velocity;
    //System.out.println("SPEED DIR: " + dX + "\t" + dY);
    
    // new position in next cycle
    Position p = new Position();
    p.setX(target.position.getX() + dX);
    p.setY(target.position.getY() + dY);
    
    // position distance per cycle
    double deltaDist = getDistance(p, submarine.getPosition()) - distanceToTarget;
    //System.out.println("DELTA DIST: " + deltaDist);
    // rounds to reach target
    double t = distanceToTarget / (mapConfig.getTorpedoSpeed() - deltaDist);
    //System.out.println("CYCLE TO REACH: " + t);
    // angle of shoot
    p.setX(target.position.getX() + (dX * t));
    p.setY(target.position.getY() + (dY * t));
    //System.out.println("COLLUSION: " + p.getX() + "\t" + p.getY());
    double angle = Math.atan2(p.getY() - submarine.getPosition().getY(), p.getX() - submarine.getPosition().getX());
    angle /= Math.PI / 180.0;
    /*
    double angle = target.velocity * t * target.velocity * t;
    angle -= distanceToTarget * distanceToTarget;
    angle -= mapConfig.getTorpedoSpeed() * t * mapConfig.getTorpedoSpeed() * t;
    angle /= 2.0 * distanceToTarget * mapConfig.getTorpedoSpeed() * t;
    angle = Math.acos(angle);*/
    //System.out.println("ANGLE: " + angle);
    // check explosion distance
    if (mapConfig.getTorpedoSpeed() * t < mapConfig.getTorpedoExplosionRadius() * 1.5 ||
        mapConfig.getTorpedoSpeed() * t > shootDistance) {
      return null;
    }
    angle = angleCorrection(angle);
    return new Shoot(submarine, angle);
  }
  
  public static double angleCorrection(double angle) {
    while (angle < 0) {
      angle += 360;
    }
    while (angle > 360) {
      angle -= 360;
    }
    return angle;
  }
  
  public static Position getEndPos(Game game, Position center, double angle, double length) {
    double x = length;
    double y = 0.0;
    double angleRad = angle / 180.0 * Math.PI;
    Position result = new Position();
    result.setX((x * Math.cos(angleRad)) - (y * Math.sin(angleRad)) + center.getX());
    result.setY((x * Math.sin(angleRad)) + (y * Math.cos(angleRad)) + center.getY());
    return result;
  }
}
 