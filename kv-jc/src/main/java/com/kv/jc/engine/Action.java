package com.kv.jc.engine;

import com.kv.jc.http.json.Submarine;

public abstract class Action {
  @Override
  public String toString() {
    return "submarine=" + submarine + ", angle=" + angle + "]";
  }
  public final Submarine submarine;
  public final double angle;
  protected Action(Submarine submarine, double angle) {
    this.submarine = submarine;
    this.angle = angle;
  }
}
