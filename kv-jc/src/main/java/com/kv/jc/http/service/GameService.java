package com.kv.jc.http.service;

import com.kv.jc.http.json.response.CreateGameResponse;
import com.kv.jc.http.json.response.GetGameListResponse;
import com.kv.jc.http.json.response.JoinGameResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface GameService {

	@Headers(ServiceConfiguration.HEADER)
	@GET(ServiceConfiguration.URL_GET_GAME_LIST)
	Call<GetGameListResponse> getGameList();

	@Headers(ServiceConfiguration.HEADER)
	@POST(ServiceConfiguration.URL_CREATE_GAME)
	Call<CreateGameResponse> createGame();

	@Headers(ServiceConfiguration.HEADER)
	@POST(ServiceConfiguration.URL_JOIN_GAME)
	Call<JoinGameResponse> joinGame(@Path(ServiceConfiguration.PARAM_GAME_ID) Integer gameId);

}
