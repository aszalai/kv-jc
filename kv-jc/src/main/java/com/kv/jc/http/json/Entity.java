package com.kv.jc.http.json;

public class Entity {

	private EntityType type;
	private Long id;
	private Position position;
	private Double velocity;
	private Double angle;
	private Integer roundsMoved;

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

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

	public Integer getRoundsMoved() {
		return roundsMoved;
	}

	public void setRoundsMoved(Integer roundsMoved) {
		this.roundsMoved = roundsMoved;
	}

  @Override
  public String toString() {
    return "Entity [type=" + type + ", id=" + id + ", position=" + position + ", velocity=" + velocity + ", angle="
        + angle + ", roundsMoved=" + roundsMoved + "]";
  }

}
