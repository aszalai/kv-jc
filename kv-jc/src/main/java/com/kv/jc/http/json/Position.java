package com.kv.jc.http.json;

public class Position {

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}

	private Double x;
	private Double y;

	public Position() {
	}

	public Position(Double x, Double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

}
