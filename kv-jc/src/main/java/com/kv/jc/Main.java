package com.kv.jc;

import java.io.IOException;
import java.util.List;

import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.GameStatus;
import com.kv.jc.http.service.ServiceController;

public class Main {

	public static String BASE_URL = "http://";
	
	public static void main(String[] args) throws IOException, InterruptedException {
	  BASE_URL += args[0] + "/";
		ServiceController controller = ServiceController.create(BASE_URL);
		
		GameStatus status = GameStatus.START;
		boolean run = true;
		long sleepTime = 100;
		long gameId = -1;
        Game game = null;
        
		while (run) {
          switch (status) {
          case START:
            // select game id or create a game
            List<Integer> games = controller.getGames();
            System.out.println("GAMES: " + games);
            if (games.size() == 0) {
              System.out.println("CREATE GAME");
              gameId = controller.createGame();
            } else {
              gameId = games.get(0);
            }
            if (gameId != -1) {
              status = GameStatus.WAITING;
            }
            break;
          case WAITING:
            // if waiting get common info (e.g. sleep time) and join to the game
            System.out.println("GAMEID: " + gameId);
            game = controller.gameInfo(gameId);
            System.out.println(game);
            sleepTime = game.getMapConfiguration().getRoundLength() / 2;
            if (game.getStatus() == GameStatus.WAITING) {
              // join the game
              System.out.println("JOINING TO: " + gameId);
              controller.joinGame(gameId);
            }
            status = game.getStatus();
            break;
          case RUNNING:
            // get game state, send radar actions
            game = controller.gameInfo(gameId);
            status = game.getStatus();
            System.out.println(game);
            // send actions
            break;
          case ENDED: 
            run = false;
            break;
          default:
            break;
          }
          Thread.sleep(sleepTime);
        }
		
	}

}
