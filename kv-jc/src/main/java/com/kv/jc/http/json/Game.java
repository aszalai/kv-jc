package com.kv.jc.http.json;

import java.util.Map;

public class Game {

	private Long id;
	private Integer round;
	private Map<String, Integer> scores;
	// private ConnectionStatus connectionStatus;
	private MapConfiguration mapConfiguration;
	private GameStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Map<String, Integer> getScores() {
		return scores;
	}

	public void setScores(Map<String, Integer> scores) {
		this.scores = scores;
	}

	public MapConfiguration getMapConfiguration() {
		return mapConfiguration;
	}

	public void setMapConfiguration(MapConfiguration mapConfiguration) {
		this.mapConfiguration = mapConfiguration;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

}
