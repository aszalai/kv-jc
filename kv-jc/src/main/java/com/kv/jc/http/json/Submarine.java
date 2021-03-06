package com.kv.jc.http.json;

public class Submarine {

	// private Type type;
	// private Owner owner;
	private Long id;
	private Position position;
	private Double velocity;
	private Double angle;
	private Integer hp;
	private Integer sonarCooldown;
	private Integer torpedoCooldown;
	private Integer sonarExtended;

	// transient fields
	private transient Long gameId;
	private transient Position idle = new Position();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Double getVelocity() {
		return velocity;
	}

	public void setVelocity(Double velocity) {
		this.velocity = velocity;
	}

	public Double getAngle() {
		return angle;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Integer getSonarCooldown() {
		return sonarCooldown;
	}

	public void setSonarCooldown(Integer sonarCooldown) {
		this.sonarCooldown = sonarCooldown;
	}

	public Integer getTorpedoCooldown() {
		return torpedoCooldown;
	}

	public void setTorpedoCooldown(Integer torpedoCooldown) {
		this.torpedoCooldown = torpedoCooldown;
	}

	public Integer getSonarExtended() {
		return sonarExtended;
	}

	public void setSonarExtended(Integer sonarExtended) {
		this.sonarExtended = sonarExtended;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Position getIdle() {
		return idle;
	}

	public void setIdle(Position idle) {
		this.idle = idle;
	}

	public void idleTo(Double x, Double y) {
		if (idle == null) {
			idle = new Position();
		}
		idle.setX(x);
		idle.setY(y);
	}

	@Override
	public String toString() {
		return "Submarine [id=" + id + ", position=" + position + ", velocity=" + velocity + ", angle=" + angle + ", hp=" + hp + ", sonarCooldown=" + sonarCooldown
				+ ", torpedoCooldown=" + torpedoCooldown + ", sonarExtended=" + sonarExtended + ", gameId=" + gameId + ", idle=" + idle + "]";
	}

}
