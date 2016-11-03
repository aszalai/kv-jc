package com.kv.jc.http.service;

interface ServiceConfiguration {

	/**
	 * Team token
	 */
	final String TEAM_TOKEN = "EB49C4C699FDC2EA52C26010079C8D88";

	/**
	 * Maximum ennyiszer próbálja újra a sikertelen kéréseket, mielõtt RuntimeException-t dobna.
	 */
	final int MAX_TRIES = 3;

	/**
	 * Header Team tokennel
	 */
	final String HEADER = "TEAMTOKEN: " + TEAM_TOKEN;

	/**
	 * Paraméterek az URL-ekben
	 */
	final String PARAM_GAME_ID = "gameId";
	final String PARAM_SUBMARINE_ID = "submarineId";

	/**
	 * URL-ek
	 */
	final String URL_CREATE_GAME	= "jc16-srv/game";
	final String URL_GET_GAME_LIST	= "jc16-srv/game";
	final String URL_JOIN_GAME		= "jc16-srv/game/{" + PARAM_GAME_ID + "}";
	final String URL_GET_GAME_INFO	= "jc16-srv/game/{" + PARAM_GAME_ID + "}";
	final String URL_GET_SUBMARINES	= "jc16-srv/game/{" + PARAM_GAME_ID + "}/submarine";
	final String URL_MOVE			= "jc16-srv/game/{" + PARAM_GAME_ID + "}/submarine/{" + PARAM_SUBMARINE_ID + "}/move";
	final String URL_SHOOT			= "jc16-srv/game/{" + PARAM_GAME_ID + "}/submarine/{" + PARAM_SUBMARINE_ID + "}/shoot";
	final String URL_GET_SONAR		= "jc16-srv/game/{" + PARAM_GAME_ID + "}/submarine/{" + PARAM_SUBMARINE_ID + "}/sonar";
	final String URL_EXTEND_SONAR	= "jc16-srv/game/{" + PARAM_GAME_ID + "}/submarine/{" + PARAM_SUBMARINE_ID + "}/sonar";
}
