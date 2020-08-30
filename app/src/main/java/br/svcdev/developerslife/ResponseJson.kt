package br.svcdev.developerslife

import com.google.gson.annotations.SerializedName

class ResponseJson {
    @SerializedName("description")
    var description: String? = null

    @SerializedName("gifURL")
    var gifUrl: String? = null

}