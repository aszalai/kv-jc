package com.kv.jc.http.json;

import java.util.Map;

public class Scores {
  private Map<String, Integer> scores;
  
  public Map<String, Integer> getScores() {
    return scores;
  }

  @Override
  public String toString() {
    return "Scores [scores=" + scores + "]";
  }
  
}
