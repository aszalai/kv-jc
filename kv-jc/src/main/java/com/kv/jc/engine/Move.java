package com.kv.jc.engine;

import com.kv.jc.http.json.Submarine;

public class Move extends Action {
  public final double velocity;
  public Move(Submarine submarine, double angle, double velocity) {
    super(submarine, angle);
    this.velocity = velocity;
  }
}
