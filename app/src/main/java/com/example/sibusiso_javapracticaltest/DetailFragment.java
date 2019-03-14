package com.example.sibusiso_javapracticaltest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private TextView tvName, tvTypes, tvAttack, tvDefense, tvSpeed;
    private ImageView ivPokemon;

    private String name, height, weight;
    private int attack, defense, health, pokeId, speed;
    private List<Sprite> sprites = new ArrayList<>();
    private List<PokeType> pokeTypes = new ArrayList<>();
    int id;
    View rootView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_detail, container, false);

        tvName = (TextView) rootView.findViewById(R.id.tv_detail_name);
        tvTypes = (TextView) rootView.findViewById(R.id.tv_detail_types);
        tvAttack = (TextView) rootView.findViewById(R.id.tv_detail_attack);
        tvDefense = (TextView) rootView.findViewById(R.id.tv_detail_defense);
        tvSpeed = (TextView) rootView.findViewById(R.id.tv_detail_speed);
        ivPokemon = (ImageView) rootView.findViewById(R.id.iv_detail_pokemon);


        Bundle bundle = this.getArguments();

        try {
            if (bundle != null) {


                     id =bundle.getInt("ID");

                LoadPokeMonData(id);


            }
        } catch (Exception ex) {
            Log.d("ReminderService In", "####1" + ex.getMessage());
        }


        return rootView;
    }

    private void LoadPokeMonData(final int pokeid) {
        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);

        for (int i = 1; i <= 20; i++) {
            Call<JsonObject> call = request.getPokemon(i);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                    if (response.isSuccessful()) {

                        JsonObject user_array = response.body();
                        // Log.d("ReminderService In", "####1"+user_array.size()+"\n"+ user_array.toString());
                        // Log.d("ReminderService In", "####1"+user_array.getAsJsonObject("sprites").get("back_default"));


                        name = user_array.get("name").toString().toUpperCase().replace("\"","");
                        height = user_array.get("height").toString();
                        weight = user_array.get("weight").toString();
                        pokeId = Integer.valueOf(user_array.get("id").toString());
                        //  Log.d("ReminderService In", "####"+name+" "+height+" "+weight+" "+pokeId);
                        JsonArray jsonArray = user_array.getAsJsonArray("stats");
                        for (int j = 0; j < jsonArray.size(); j++) {

                            JsonObject jsonobj_1 = (JsonObject) jsonArray.get(j);
                            if (j == 0) {
                                speed = Integer.valueOf(jsonobj_1.get("base_stat").toString());
                            } else if (j == 3) {
                                defense = Integer.valueOf(jsonobj_1.get("base_stat").toString());
                            } else if (j == 4) {
                                attack = Integer.valueOf(jsonobj_1.get("base_stat").toString());
                            } else if (j == 5) {
                                health = Integer.valueOf(jsonobj_1.get("base_stat").toString());
                            }


                        }
                        //  Log.d("ReminderService In", "####"+speed+" "+defense+" "+attack+" "+health);
                        String[] spritename = new String[]{"back_default", "back_shiny", "front_default", "front_shiny"};
                        sprites.clear();
                        for (String names : spritename) {
                            Sprite sprite = new Sprite(name);
                            String url = user_array.getAsJsonObject("sprites").get(names).toString();
                            sprite.setResourceUri(url.substring(0, url.length() - 1));
                            sprites.add(sprite);
                        }
                        // Log.d("ReminderService In", "Prite Size"+sprites.size());
                        JsonArray types = user_array.getAsJsonArray("types");
                        pokeTypes.clear();
                        for (int j = 0; j < types.size(); j++) {
                            JsonObject jsonobj_1 = (JsonObject) types.get(j);
                            String type = jsonobj_1.getAsJsonObject("type").get("name").toString();
                            PokeType pokeType = new PokeType(type);
                            pokeTypes.add(pokeType);

                        }
                        // Log.d("ReminderService In", "PokeType Size"+pokeTypes.size());

                        final Pokemon pokemon = new Pokemon(name, attack, defense, height, health, pokeId, speed, weight, sprites, pokeTypes);


                        try {
                            URL newurl = new URL(pokemon.getSprites().get(0).getResourceUri());
                            Bitmap b = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());

                        } catch (Exception ex) {

                        }


// Test Received ID
                        if(pokeid==pokemon.getPokedexId()){
                            Log.d("POKEMON", "Id: " + pokemon.getPokedexId());
                            Log.d("POKEMON", "Name: " + pokemon.getName());
                            Log.d("POKEMON", "Attack: " + pokemon.getAttack());
                            Log.d("POKEMON", "Defense: " + pokemon.getDefense());
                            Log.d("POKEMON", "Health: " + pokemon.getHealth());
                            Log.d("POKEMON", "Height: " + pokemon.getHeight());
                            Log.d("POKEMON", "Weight: " + pokemon.getWeight());
                            Log.d("POKEMON", "Type: " + pokemon.pokeTypesToString());



        tvName.setText(pokemon.getName());
        tvTypes.setText(pokemon.pokeTypesToString());
        tvAttack.setText("Attack: " + pokemon.getAttack().toString());
        tvDefense.setText("Defense: " + pokemon.getDefense().toString());
        tvSpeed.setText("Speed: " + pokemon.getSpeed().toString());
        Picasso.with(rootView.getContext())
                .load(pokemon.getSprites().get(0).getResourceUri())
                .resize(128,128)
                .into(ivPokemon);
                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("ReminderService In", "####1" + t.getMessage());
                }
            });
        }


    }

}
