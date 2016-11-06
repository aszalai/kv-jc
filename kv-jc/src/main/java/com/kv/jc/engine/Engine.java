package com.kv.jc.engine;

import java.util.LinkedList;
import java.util.List;

import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.MapConfiguration;
import com.kv.jc.http.json.Position;
import com.kv.jc.http.json.Submarine;

public class Engine {
  public static List<Action> getActions(Game game) {
    List<Action> result = new LinkedList<Action>();
    return result;
  }
  
  public static void targets(Game game) {
    game.getMapConfiguration().getIslandPositions();
  }
  
  public static void moveTo(Position position, Submarine submarine) {
    double angle = (position.getY() - submarine.getPosition().getY()) / (position.getX() - submarine.getPosition().getX());
    angle -= submarine.getAngle();
    double distance = getDistance(position, submarine.getPosition());
    
  }
  
  public static double getDistance(Position a, Position b) {
    double dx = a.getX() - b.getX();
    double dy = a.getY() - b.getY();
    dx *= dx;
    dy *= dy;
    return Math.sqrt(dx + dy);
  }
  
  public static void getShoot(MapConfiguration mapConfig, Target target, Submarine submarine) {
    double shootDistance = mapConfig.getTorpedoRange() * mapConfig.getTorpedoSpeed();
    double distanceToTarget = getDistance(submarine.getPosition(), target.position);
    if (distanceToTarget < shootDistance * 0.9) {
      //double roundsToReach = 
    }
  }
}
