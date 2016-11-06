package com.kv.jc.engine;

import com.kv.jc.http.json.Submarine;

public class Shoot extends Action {
  @Override
  public String toString() {
    return "Shoot [" + super.toString();
  }

  public Shoot(Submarine submarine, double angle) {
    super(submarine, angle);
  }
}
