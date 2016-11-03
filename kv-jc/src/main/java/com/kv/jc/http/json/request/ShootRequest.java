package com.kv.jc.http.json.request;

public class ShootRequest {

	private Double angle;

	public ShootRequest(Double angle) {
		super();
		this.angle = angle;
	}

	public Double getAngle() {
		return angle;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

}
