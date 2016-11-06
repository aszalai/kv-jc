package com.kv.jc.http.service;

import com.kv.jc.http.json.Submarine;

public interface ServiceCallback {

	public Long getGameId();

	public void onUpdateState();

	public void onMove(Submarine submarine, Double speed, Double turn);

	public void onShoot(Submarine submarine, Double angle);

}
