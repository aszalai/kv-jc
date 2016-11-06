package com.kv.jc.http.service;

import java.io.IOException;
import java.util.List;

import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.response.AbstractResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ServiceController {

	public static final ServiceController create(String baseUrl) {
		return new ServiceController(baseUrl);
	}

	private GameService gameService;

	private ServiceController(String baseUrl) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
		gameService = retrofit.create(GameService.class);
	}

	private <T extends AbstractResponse> T call(Call<T> call) {
		Response<T> executeResponse = null;
		Throwable throwable = null;
		int numberOfTries = 0;

		while (executeResponse == null) {
			try {
				executeResponse = call.execute();
			} catch (IOException e) {
				throwable = e;
			}
			if ((throwable != null && numberOfTries >= ServiceConfiguration.MAX_TRIES)) {
				throw new RuntimeException(throwable);
			}
		}

		T response = executeResponse.body();
		if (!response.isOK()) {
			throw new RuntimeException(response.getMessage() + " " + response.getErrorMessage());
		}
		return response;
	}

	public List<Integer> getGames() {
		return call(gameService.getGameList()).getGames();
	}
	
	public Long createGame() {
	  return call(gameService.createGame()).getId();
	}
	
	public Game gameInfo(long gameId) {
	  return call(gameService.gameInfo(gameId)).getGame();
	}

}
