package br.svcdev.developerslife

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkRequest {
    @GET("random")
    fun sendRequest(@Query("json") json: Boolean): Call<ResponseJson?>?
}