package com.kv.jc.http.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Scores {
	private Map<String, Integer> scores = new HashMap<>();

	public Map<String, Integer> getScores() {
		return scores;
	}

	public List<Entry<String, Integer>> getOrderedScores() {
		List<Entry<String, Integer>> result = new ArrayList<>(scores.entrySet());
		Collections.sort(result, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
		return result;
	}

	@Override
	public String toString() {
		return "Scores [scores=" + scores + "]";
	}

}
