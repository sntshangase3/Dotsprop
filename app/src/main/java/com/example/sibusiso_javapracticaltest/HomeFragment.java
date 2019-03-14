package com.example.sibusiso_javapracticaltest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sbusiso.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    FragmentManager fragmentManager;

    ArrayList<Integer> item_id = new ArrayList<Integer>();
    ArrayList<Bitmap> image = new ArrayList<Bitmap>();
    ArrayList<String> item_name = new ArrayList<String>();
    ArrayList<String> item_type = new ArrayList<String>();
    Connection con;
    String un, pass, db, ip;

    ListView lstgross;
    ImageView edtlogoImage, edtprofileImage;
    Bundle bundle;

    MainActivity activity = MainActivity.instance;
    ImageView b3,b4;
    private String name, height, weight;
    private int attack, defense, health, pokeId, speed;
    private List<Sprite> sprites = new ArrayList<>();
    private List<PokeType> pokeTypes = new ArrayList<>();


    public HomeFragment() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_homee, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        fragmentManager = getFragmentManager();
        edtlogoImage = (ImageView) rootView.findViewById(R.id.imgLogo);
        edtprofileImage = (ImageView) rootView.findViewById(R.id.profileImage);


        b3 = (ImageView) rootView.findViewById(R.id.b3);
        b4 = (ImageView) rootView.findViewById(R.id.b4);


        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "SqaloITSolutionsTest";
        un = "sqaloits";
        pass = "422q5mfQzU";


        bundle = this.getArguments();


        try {

            LoadUserData();

        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Fail Load User Details,Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        try {

            LoadPokeMonData();

        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), "Failed Load PokeMon ,Check your network connection!!", Toast.LENGTH_LONG).show();
        }

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent callnoIntent = new Intent(Intent.ACTION_CALL);
                    callnoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callnoIntent.setData(Uri.parse("tel:0795357393"));
                    getActivity().startActivity(callnoIntent);
                } catch (SecurityException ex) {
                    Toast.makeText(rootView.getContext(), "Chef Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String message = "Hi Kyle \nSomeone is testing.\nPokemom App(By Sibusiso)";
                Intent i = new Intent(rootView.getContext(), MainActivitySms.class);
                 startActivity(i);

            }
        });

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        Toast.makeText(rootView.getContext(), "You've selected " + String.valueOf(position)+" "+pokeId+" "+l , Toast.LENGTH_LONG).show();
        Log.d("POKEMON", "You've selected " + String.valueOf(position)+" "+pokeId+" "+l);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void LoadUserData() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {


                String query = "select * from [AppUser] where [id]=" + Integer.parseInt(activity.edthidenuserid.getText().toString());
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();

                try {
                    if (rs.getString("image") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        edtprofileImage.setImageBitmap(decodebitmap);
                    } else {

                        edtprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

                    }
                } catch (Exception e) {

                }


            }


        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString() + "Here0", Toast.LENGTH_LONG).show();
        }
//===========

    }

    private void LoadPokeMonData() {
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
                            item_id.add(pokemon.getPokedexId());
                            image.add(b);
                            item_name.add(pokemon.getName());
                            item_type.add(pokemon.pokeTypesToString());
                        } catch (Exception ex) {

                        }



                      /*  Log.d("POKEMON", "Id: " + pokemon.getPokedexId());
                        Log.d("POKEMON", "Name: " + pokemon.getName());
                        Log.d("POKEMON", "Attack: " + pokemon.getAttack());
                        Log.d("POKEMON", "Defense: " + pokemon.getDefense());
                        Log.d("POKEMON", "Health: " + pokemon.getHealth());
                        Log.d("POKEMON", "Height: " + pokemon.getHeight());
                        Log.d("POKEMON", "Weight: " + pokemon.getWeight());
                        Log.d("POKEMON", "Type: " + pokemon.pokeTypesToString());*/

                        PokemonAdapter adapter = new PokemonAdapter(getActivity(),item_id, image, item_name, item_type);
                        adapter.sort(new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                return lhs.compareTo(rhs);
                            }
                        });
                         lstgross.setAdapter(adapter);
                        lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    final int position, long id) {

                                final int selectedid = item_id.get(position);
                                final String selectedname = item_name.get(position);
                                Pokemon pokemon = new Pokemon(name, attack, defense, height, health, pokeId, speed, weight, sprites, pokeTypes);

                                Toast ToastMessage = Toast.makeText(rootView.getContext(),"You've selected "+selectedname,Toast.LENGTH_LONG);
                                View toastView = ToastMessage.getView();
                                toastView.setBackgroundResource(R.drawable.toast_bground);
                                ToastMessage.show();
                                Log.d("POKEMON", "You've selected " + String.valueOf(position)+" "+selectedid+" "+pokemon.getPokedexId());
                                // TODO Auto-generated method stub
                               Fragment fragment = new DetailFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("ID", selectedid);
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                            }
                        });


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
