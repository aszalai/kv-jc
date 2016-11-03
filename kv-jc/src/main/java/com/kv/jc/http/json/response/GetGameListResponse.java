package com.kv.jc.http.json.response;

import java.util.List;

public class GetGameListResponse extends AbstractResponse {

	private List<Integer> games;

	public List<Integer> getGames() {
		return games;
	}

	public void setGames(List<Integer> games) {
		this.games = games;
	}

}
