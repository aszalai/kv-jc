package com.kv.jc.engine;

import java.util.Comparator;

public class TargetComparator implements Comparator<Target> {
  @Override
  public int compare(Target o1, Target o2) {
    if (o1.score < o2.score) {
      return -1;
    } else if (o1.score > o2.score) {
      return 1;
    }
    return 0;
  }

}
