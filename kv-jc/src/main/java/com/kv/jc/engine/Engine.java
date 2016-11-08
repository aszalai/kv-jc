package com.kv.jc.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
  public static int targetSeen = 5;
  
  public static Position[] idle;
  public static List<Action> getActions(Game game) {
    wallDistance = game.getMapConfiguration().getMaxSpeed() / game.getMapConfiguration().getMaxAccelerationPerRound() * game.getMapConfiguration().getMaxSpeed();
    islandDistance = game.getMapConfiguration().getIslandSize() + game.getMapConfiguration().getTorpedoExplosionRadius() + wallDistance;
    torpedoDistance = game.getMapConfiguration().getTorpedoExplosionRadius() * 3;
    submarineDistance = game.getMapConfiguration().getSonarRange() * 1.5;
    
    targetSeen ++;
    if (idle == null || targetSeen > 5) {
      idle = new Position[game.getSubmarines().size()];
      for (int i = 0; i < idle.length; i++) {
        idle[i] = new Position();
        idle[i].setX((i*2 + 1) * game.getMapConfiguration().getWidth() / 4 + 0.0);
        idle[i].setY((i*2 + 1) * game.getMapConfiguration().getHeight() / 4 + 0.0);
      }
    }
    List<Action> result = new LinkedList<Action>();
    List<Target> targets = getTargets(game);
    targets.sort(new TargetComparator());
    int sidx = 0;
    for (Submarine submarine : game.getSubmarines()) {
      if (targets.size() > 0) {
        targetSeen = 0;
        Target target = targets.remove(targets.size() - 1);
        Shoot shoot = getShoot(game, target, submarine);
        if (shoot == null) {
          targets.add(target);
        } else {
          result.add(shoot);
        }
        idle[sidx] = target.position;
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
    // TODO: identify more targets
    List<Target> targets = new LinkedList<Target>();
    for (Entity entity : game.getEnemies()) {
      targets.add(new Target(entity.getPosition(), entity.getAngle(), entity.getVelocity(), 0));
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
    System.out.println("MOVE TO: " + submarine.getId() + "\t" + position);
    
    Position direction = new Position();
    direction.setX(position.getX() - submarine.getPosition().getX());
    direction.setY(position.getY() - submarine.getPosition().getY());
    normalize(direction);
    double angle = Math.atan2(direction.getY(), direction.getX()) / Math.PI * 180.0;
    double distance = getDistance(position, submarine.getPosition()) + game.getMapConfiguration().getTorpedoExplosionRadius();
    double acc = game.getMapConfiguration().getMaxAccelerationPerRound();
    double velocity = acc;
    if (distance < wallDistance) {
      velocity = submarine.getVelocity() > acc ? -acc : 0.0;
    }
    
    // close to other submarines
    for (Submarine s : game.getSubmarines()) {
      if (s.getId() != submarine.getId()) {
        if (getDistance(submarine.getPosition(), s.getPosition()) < submarineDistance) {
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
      if (getDistance(submarine.getPosition(), torpedo.getPosition()) < torpedoDistance) {
        direction.setX(torpedo.getPosition().getX() - submarine.getPosition().getX());
        direction.setY(torpedo.getPosition().getY() - submarine.getPosition().getY());
        normalize(direction);
        angle = Math.atan2(direction.getY(), direction.getX()) / Math.PI * 180.0;
        System.out.println("CLOSE TORPEDO AT: " + torpedo.getPosition());
        angle += 180.0;
      }
    }
    
    // close to islands
    for (Position island : game.getMapConfiguration().getIslandPositions()) {
      if (getDistance(submarine.getPosition(), island) < islandDistance) {
        direction.setX(island.getX() - submarine.getPosition().getX());
        direction.setY(island.getY() - submarine.getPosition().getY());
        normalize(direction);
        angle = Math.atan2(direction.getY(), direction.getX()) / Math.PI * 180.0;
        System.out.println("CLOSE ISLAND AT: " + island);
        angle += 180.0;
        if (distance < wallDistance) {
          velocity = submarine.getVelocity() > acc ? -acc : 0.0;
        }
      }
    }
    
    // close to walls
    if (submarine.getPosition().getX() < wallDistance) {
      System.out.println("CLOSE LEFT WALL");
      angle = 0.0;
      velocity = submarine.getVelocity() > acc ? -acc : 0.0;
    } else if (submarine.getPosition().getX() > game.getMapConfiguration().getWidth() - wallDistance) {
      System.out.println("CLOSE RIGHT WALL");
      angle = 180.0;
      velocity = submarine.getVelocity() > acc ? -acc : 0.0;
    }
    if (submarine.getPosition().getY() < wallDistance) {
      System.out.println("CLOSE BOTTOM WALL");
      angle = 90.0;
      velocity = submarine.getVelocity() > acc ? -acc : 0.0;
    } else if (submarine.getPosition().getY() > game.getMapConfiguration().getHeight() - wallDistance) {
      System.out.println("CLOSE TOP WALL");
      angle = 270.0;
      velocity = submarine.getVelocity() > acc ? -acc : 0.0;
    }
    
    //System.out.println("ANGLE: " + angle);
    angle = getTurnAngle(submarine.getAngle(), angle);
    
    // correct angle by max angle
    if (angle < -game.getMapConfiguration().getMaxSteeringPerRound()) {
      angle = -game.getMapConfiguration().getMaxSteeringPerRound();
    }
    if (angle > game.getMapConfiguration().getMaxSteeringPerRound()) {
      angle = game.getMapConfiguration().getMaxSteeringPerRound();
    }
    return new Move(submarine, angle, velocity);
  }
  
  public static double getTurnAngle(double from, double to) {
    return getTurnAngle((int)from, (int)to);
  }
  
  public static int getTurnAngle(int from, int to) {
    int neg = (from - to) % 360;
    int pos = ((360 - from) + to) % 360;
   
    if (neg < pos) {
      return neg * -1;
    } else {
      return pos;
    }
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
    if (mapConfig.getTorpedoSpeed() * t <= mapConfig.getTorpedoExplosionRadius() * 1.5 ||
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
 