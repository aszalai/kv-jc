package com.kv.jc.http.json;

import java.util.List;

public class Game {

	private Long id;
	private Integer round;
	private Scores scores;
	// private ConnectionStatus connectionStatus;
	private MapConfiguration mapConfiguration;
	private GameStatus status;
	private long createdTime;

	// transient fields
	private transient List<Submarine> submarines;
	private transient List<Entity> enemies;
	private transient List<Entity> torpedos;

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

	public Scores getScores() {
		return scores;
	}

	public void setScores(Scores scores) {
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

	public List<Submarine> getSubmarines() {
		return submarines;
	}

	public void setSubmarines(List<Submarine> submarines) {
		this.submarines = submarines;
	}

	public List<Entity> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<Entity> enemies) {
		this.enemies = enemies;
	}

	public List<Entity> getTorpedos() {
		return torpedos;
	}

	public void setTorpedos(List<Entity> torpedos) {
		this.torpedos = torpedos;
	}

	@Override
	public String toString() {
		return "Game\n\tid=" + id +
				"\n\tround=" + round +
				"\n\tscores=" + scores +
				// "\n\tconnectionStatus" + connectionStatus +
				"\n\tstatus=" + status +
				"\n\tcreatedTime=" + createdTime +
				"\n\tmapConfiguration=" + mapConfiguration;
	}

}
