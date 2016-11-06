package com.kv.jc.http.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kv.jc.http.json.Entity;
import com.kv.jc.http.json.EntityType;
import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.Submarine;
import com.kv.jc.http.json.request.MoveRequest;
import com.kv.jc.http.json.request.ShootRequest;
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
	private SubmarineService submarineService;
	private SonarService sonarService;

	private ServiceController(String baseUrl) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
		gameService = retrofit.create(GameService.class);
		submarineService = retrofit.create(SubmarineService.class);
		sonarService = retrofit.create(SonarService.class);
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

	public List<Submarine> getSubmarines(Game game) {
		return call(submarineService.getSubmarines(game.getId())).getSubmarines();
	}
	
	public void joinGame(long gameId) {
	  call(gameService.joinGame(gameId));
	}

	public void updateSumbarines(final Game game) {
		List<Submarine> submarines = getSubmarines(game);
		submarines.forEach(submarine -> submarine.setGameId(game.getId()));
		game.getSubmarines().clear();
		game.getSubmarines().addAll(submarines);
	}

	public void move(Submarine submarine, Double speed, Double turn) {
		MoveRequest request = new MoveRequest(speed, turn);
		call(submarineService.move(submarine.getGameId(), submarine.getId(), request));
	}

	public void shoot(Submarine submarine, Double angle) {
		ShootRequest request = new ShootRequest(angle);
		call(submarineService.shoot(submarine.getGameId(), submarine.getId(), request));
	}

	public List<Entity> getSonar(Submarine submarine) {
		return call(sonarService.getSonar(submarine.getGameId(), submarine.getId())).getEntities();
	}

	public void updateEntities(final Game game) {
		final List<Entity> entities = new ArrayList<>();
		game.getSubmarines().forEach(submarine -> entities.addAll(getSonar(submarine)));
		game.getEnemies().clear();
		game.getTorpedos().clear();
		game.getEnemies().addAll(entities.stream().filter(entity -> entity.getType() == EntityType.Submarine).collect(Collectors.toList()));
		game.getTorpedos().addAll(entities.stream().filter(entity -> entity.getType() == EntityType.Torpedo).collect(Collectors.toList()));
	}

	// FIXME Minket is messzebbrõl látnak!!!
	@Deprecated
	public boolean extendSonar(Submarine submarine) {
		if (submarine.getSonarCooldown() > 0) {
			return false;
		}
		call(sonarService.extendSonar(submarine.getGameId(), submarine.getId()));
		return true;
	}
}
