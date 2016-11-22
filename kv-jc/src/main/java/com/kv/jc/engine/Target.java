package com.kv.jc.engine;

import com.kv.jc.http.json.Position;

public class Target {
  public final long id;
  public final Position position;
  public final double angle;
  public final double velocity;
  public final double score;
  public Target(long id, Position position, double angle, double velociy, double score) {
    this.id = id;
    this.position = position;
    this.angle = angle;
    this.velocity = velociy;
    this.score = score;
  }
}
