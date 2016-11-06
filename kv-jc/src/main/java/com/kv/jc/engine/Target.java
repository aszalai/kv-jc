package com.kv.jc.engine;

import com.kv.jc.http.json.Position;

public class Target {
  public final Position position;
  public final double angle;
  public final double velocity;
  public final double score;
  public Target(Position position, double angle, double velociy, double score) {
    this.position = position;
    this.angle = angle;
    this.velocity = velociy;
    this.score = score;
  }
}
