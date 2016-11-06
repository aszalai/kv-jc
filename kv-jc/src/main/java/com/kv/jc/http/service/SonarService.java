package com.kv.jc.http.service;

import com.kv.jc.http.json.response.ExtendSonarResponse;
import com.kv.jc.http.json.response.GetSonarResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface SonarService {

	@Headers(ServiceConfiguration.HEADER)
	@GET(ServiceConfiguration.URL_GET_SONAR)
	Call<GetSonarResponse> getSonar(@Path(ServiceConfiguration.PARAM_GAME_ID) Long gameId,
			@Path(ServiceConfiguration.PARAM_SUBMARINE_ID) Long submarineId);

	@Headers(ServiceConfiguration.HEADER)
	@POST(ServiceConfiguration.URL_EXTEND_SONAR)
	Call<ExtendSonarResponse> extendSonar(@Path(ServiceConfiguration.PARAM_GAME_ID) Long gameId,
			@Path(ServiceConfiguration.PARAM_SUBMARINE_ID) Long submarineId);

}
