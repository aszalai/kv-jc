package com.kv.jc.http.json.request;

public class MoveRequest {

	private Double speed;
	private Double turn;

	public MoveRequest(Double speed, Double turn) {
		super();
		this.speed = speed;
		this.turn = turn;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getTurn() {
		return turn;
	}

	public void setTurn(Double turn) {
		this.turn = turn;
	}

}
