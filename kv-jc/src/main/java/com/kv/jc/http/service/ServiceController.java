package com.kv.jc.http.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.kv.jc.http.json.Entity;
import com.kv.jc.http.json.EntityType;
import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.GameStatus;
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

	private List<ServiceCallback> callbacks = new ArrayList<>();

	private GameService gameService;
	private SubmarineService submarineService;
	private SonarService sonarService;

	private ServiceController(String baseUrl) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		gameService = retrofit.create(GameService.class);
		submarineService = retrofit.create(SubmarineService.class);
		sonarService = retrofit.create(SonarService.class);
	}

	private <T extends AbstractResponse> T call(Call<T> call) throws ServiceCallException {
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
				throw new ServiceCallException("Max tries reached", call, throwable);
			}
		}

		T response = executeResponse.body();
		if (response == null) {
			throw new ServiceCallException("Response is null", call);
		}
		if (!response.isOK()) {
			throw new ServiceCallException(response.getMessage() + " " + response.getErrorMessage(), call);
		}
		return response;
	}

	public void addCallback(ServiceCallback serviceCallback) {
		if (serviceCallback != null) {
			callbacks.add(serviceCallback);
		}
	}

	public List<Integer> getGames() throws ServiceCallException {
		return call(gameService.getGameList()).getGames();
	}

	public Long createGame() throws ServiceCallException {
		return call(gameService.createGame()).getId();
	}

	public void joinGame(long gameId) throws ServiceCallException {
		call(gameService.joinGame(gameId));
	}

	public Game gameInfo(long gameId) throws ServiceCallException {
		return call(gameService.gameInfo(gameId)).getGame();
	}

	private void updateGameInfo(final Game game) throws ServiceCallException {
		Game refreshedGame = gameInfo(game.getId());
		game.setRound(refreshedGame.getRound());
		game.setScores(refreshedGame.getScores());
		game.setMapConfiguration(refreshedGame.getMapConfiguration());
		game.setStatus(refreshedGame.getStatus());
	}

	public void updateState(final Game game) throws ServiceCallException {
		updateGameInfo(game);
		if (game.getStatus() == GameStatus.RUNNING) {
			updateSumbarines(game);
			updateEntities(game);
		}
		callbacks.forEach(callback -> {
			if (game.getId() == callback.getGameId()) {
				callback.onUpdateState();
			}
		});
	}

	private void updateSumbarines(final Game game) throws ServiceCallException {
		List<Submarine> submarines = getSubmarines(game);
		submarines.forEach(submarine -> submarine.setGameId(game.getId()));
		game.getSubmarines().clear();
		game.getSubmarines().addAll(submarines);
	}

	public void move(Submarine submarine, Double speed, Double turn) throws ServiceCallException {
		MoveRequest request = new MoveRequest(speed, turn);
		call(submarineService.move(submarine.getGameId(), submarine.getId(), request));
		callbacks.forEach(callback -> {
			if (submarine.getGameId() == callback.getGameId()) {
				callback.onMove(submarine, speed, turn);
			}
		});
	}

	public void shoot(Submarine submarine, Double angle) throws ServiceCallException {
    if (submarine.getTorpedoCooldown() > 0) {
      return;
    }
		ShootRequest request = new ShootRequest(angle);
		call(submarineService.shoot(submarine.getGameId(), submarine.getId(), request));
		callbacks.forEach(callback -> {
			if (submarine.getGameId() == callback.getGameId()) {
				callback.onShoot(submarine, angle);
			}
		});
	}

	private List<Submarine> getSubmarines(Game game) throws ServiceCallException {
		return call(submarineService.getSubmarines(game.getId())).getSubmarines();
	}

	private List<Entity> getSonar(Submarine submarine) throws ServiceCallException {
		return call(sonarService.getSonar(submarine.getGameId(), submarine.getId())).getEntities();
	}

	private void updateEntities(final Game game) throws ServiceCallException {
		final List<Entity> entities = new ArrayList<>();
		final Set<Long> ownSubmarineIds = new HashSet<>();
		for (Submarine submarine : game.getSubmarines()) {
			entities.addAll(getSonar(submarine));
			ownSubmarineIds.add(submarine.getId());
		}
		game.getEnemies().clear();
		game.getTorpedos().clear();
		game.getEnemies().addAll(entities.stream().filter(entity -> entity.getType() == EntityType.Submarine && !ownSubmarineIds.contains(entity.getId())).collect(Collectors.toList()));
		game.getTorpedos().addAll(entities.stream().filter(entity -> entity.getType() == EntityType.Torpedo).collect(Collectors.toList()));
	}

	// FIXME Minket is messzebbrol latnak!!!
	public boolean extendSonar(Submarine submarine) throws ServiceCallException {
		if (submarine.getSonarCooldown() > 0) {
			return false;
		}
		call(sonarService.extendSonar(submarine.getGameId(), submarine.getId()));
		return true;
	}
}
