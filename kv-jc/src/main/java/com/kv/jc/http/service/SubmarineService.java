package com.kv.jc.http.service;

import com.kv.jc.http.json.request.MoveRequest;
import com.kv.jc.http.json.request.ShootRequest;
import com.kv.jc.http.json.response.GetSubmarinesResponse;
import com.kv.jc.http.json.response.MoveResponse;
import com.kv.jc.http.json.response.ShootResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface SubmarineService {

	@Headers(ServiceConfiguration.HEADER)
	@GET(ServiceConfiguration.URL_GET_SUBMARINES)
	Call<GetSubmarinesResponse> getSubmarines(@Path(ServiceConfiguration.PARAM_GAME_ID) Long gameId);

	@Headers(ServiceConfiguration.HEADER)
	@POST(ServiceConfiguration.URL_MOVE)
	Call<MoveResponse> move(@Path(ServiceConfiguration.PARAM_GAME_ID) Long gameId,
			@Path(ServiceConfiguration.PARAM_SUBMARINE_ID) Long submarineId,
			@Body MoveRequest moveRequest);

	@Headers(ServiceConfiguration.HEADER)
	@POST(ServiceConfiguration.URL_SHOOT)
	Call<ShootResponse> shoot(@Path(ServiceConfiguration.PARAM_GAME_ID) Long gameId,
			@Path(ServiceConfiguration.PARAM_SUBMARINE_ID) Long submarineId,
			@Body ShootRequest shootRequest);

}
