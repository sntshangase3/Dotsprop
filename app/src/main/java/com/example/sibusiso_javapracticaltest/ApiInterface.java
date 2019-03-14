package com.example.sibusiso_javapracticaltest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Sibusiso
 */

public interface ApiInterface {

    @GET("api/v1/pokemon/{id}")
    Call<JsonObject> getPokemon(@Path("id") int id);
     @GET("{resource_uri}")
    Call<SpriteResponse> getSprite(@Path("resource_uri") String resourceUri);
}
