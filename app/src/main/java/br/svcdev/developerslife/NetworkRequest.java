package br.svcdev.developerslife;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkRequest {
//	https://developerslife.ru/random?json=true

	@GET("random")
	Call<ResponseJson> sendRequest(@Query("json") boolean json);

}
