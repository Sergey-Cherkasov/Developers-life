package br.svcdev.developerslife;

import com.google.gson.annotations.SerializedName;

public class ResponseJson {
	@SerializedName("description")
	private String description;
	@SerializedName("gifURL")
	private String gifUrl;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGifUrl() {
		return gifUrl;
	}

	public void setGifUrl(String gifUrl) {
		this.gifUrl = gifUrl;
	}
}
