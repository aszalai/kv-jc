package com.kv.jc.engine;

import java.util.LinkedList;
import java.util.List;

import com.kv.jc.http.json.Entity;
import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.MapConfiguration;
import com.kv.jc.http.json.Position;
import com.kv.jc.http.json.Submarine;

public class Engine {
  public static List<Action> getActions(Game game) {
    List<Action> result = new LinkedList<Action>();
    List<Target> targets = getTargets(game);
    targets.sort(new TargetComparator());
    for (Submarine submarine : game.getSubmarines()) {
      if (targets.size() > 0) {
        Target target = targets.remove(targets.size() - 1);
        Shoot shoot = getShoot(game, target, submarine);
        if (shoot == null) {
          result.add(moveTo(game, target.position, submarine));
        } else {
          result.add(shoot);
        }
      }
    }
    return result;
  }
  
  public static List<Target> getTargets(Game game) {
    List<Target> targets = new LinkedList<Target>();
    for (Entity entity : game.getEnemies()) {
      targets.add(new Target(entity.getPosition(), entity.getAngle(), entity.getVelocity(), 0));
    }
    return targets;
  }
  
  public static Move moveTo(Game game, Position position, Submarine submarine) {
    double angle = (position.getY() - submarine.getPosition().getY()) / (position.getX() - submarine.getPosition().getX());
    angle -= submarine.getAngle();
    double distance = getDistance(position, submarine.getPosition()) + game.getMapConfiguration().getTorpedoExplosionRadius();
    double acc = game.getMapConfiguration().getMaxAccelerationPerRound();
    double velocity = 0.0;
    if (submarine.getVelocity() / acc > distance / submarine.getVelocity() - 1) {
      velocity = -acc;
    }
    if (submarine.getVelocity() / acc < distance + game.getMapConfiguration().getTorpedoRange() + 1) {
      velocity = acc;
    }
    // TODO: adjust angle by wall distance...
    return new Move(submarine, angle, velocity);
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
    double shootDistance = mapConfig.getTorpedoRange() * mapConfig.getTorpedoSpeed();
    double distanceToTarget = getDistance(submarine.getPosition(), target.position);
    
    System.out.println("DISTANCE: " + distanceToTarget);
    // speed per direction
    double dX = Math.cos(target.angle / 180.0 * Math.PI) * target.velocity;
    double dY = Math.sin(target.angle / 180.0 * Math.PI) * target.velocity;
    System.out.println("SPEED DIR: " + dX + "\t" + dY);
    
    // new position in next cycle
    Position p = new Position();
    p.setX(target.position.getX() + dX);
    p.setY(target.position.getY() + dY);
    
    // position distance per cycle
    double deltaDist = getDistance(p, submarine.getPosition()) - distanceToTarget;
    System.out.println("DELTA DIST: " + deltaDist);
    // rounds to reach target
    double t = distanceToTarget / (mapConfig.getTorpedoSpeed() - deltaDist);
    System.out.println("CYCLE TO REACH: " + t);
    // angle of shoot
    p.setX(target.position.getX() + (dX * t));
    p.setY(target.position.getY() + (dY * t));
    System.out.println("COLLUSION: " + p.getX() + "\t" + p.getY());
    double angle = Math.atan2(p.getY() - submarine.getPosition().getY(), p.getX() - submarine.getPosition().getX());
    angle /= Math.PI / 180.0;
    /*
    double angle = target.velocity * t * target.velocity * t;
    angle -= distanceToTarget * distanceToTarget;
    angle -= mapConfig.getTorpedoSpeed() * t * mapConfig.getTorpedoSpeed() * t;
    angle /= 2.0 * distanceToTarget * mapConfig.getTorpedoSpeed() * t;
    angle = Math.acos(angle);*/
    System.out.println("ANGLE: " + angle);
    // check explosion distance
    if (mapConfig.getTorpedoSpeed() * t <= mapConfig.getTorpedoExplosionRadius() ||
        mapConfig.getTorpedoSpeed() * t > shootDistance * 0.9) {
      return null;
    }
    return new Shoot(submarine, angle);
  }
  
  public static Position getEndPos(Game game, Position center, double angle) {
    double x = game.getMapConfiguration().getSubmarineSize() + 30;
    double y = 0.0;
    double angleRad = angle / 180.0 * Math.PI;
    Position result = new Position();
    result.setX((x * Math.cos(angleRad)) - (y * Math.sin(angleRad)) + center.getX());
    result.setY((x * Math.sin(angleRad)) + (y * Math.cos(angleRad)) + center.getY());
    return result;
  }
}
 