package com.kv.jc.http.json.response;

import com.kv.jc.http.json.Game;

public class GetGameInfoResponse extends AbstractResponse {

	private Game game;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
