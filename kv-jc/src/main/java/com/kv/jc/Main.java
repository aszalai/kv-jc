package com.kv.jc;

import java.io.IOException;
import java.util.List;

import com.kv.jc.http.service.ServiceController;

public class Main {

	public static final String BASE_URL = "http://195.228.45.100:8080/";
	public enum state {START, WAITING, RUNNING, ENDED};
    

	public static void main(String[] args) throws IOException, InterruptedException {
		ServiceController controller = ServiceController.create(BASE_URL);
		
		state status = state.START;
		boolean run = true;
		long sleepTime = 100;
		long gameId = -1;
        
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
              status = state.WAITING;
            }
            break;
          case WAITING:
            // if waiting get common info (e.g. sleep time) and join to the game
            System.out.println("GAMEID: " + gameId);
            System.out.println(controller.gameInfo(gameId));
            status = state.RUNNING;
            break;
          case RUNNING:
            // get game state, send radar actions
            // if ended
            status = state.ENDED;
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
