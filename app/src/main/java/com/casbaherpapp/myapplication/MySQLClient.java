package com.casbaherpapp.myapplication;
import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;


public class MySQLClient {

    //SAVE/RETRIEVE URLS
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    private static int id=-1;
    private String password;
    private Object[][] o;
    private int i;

    //INSTANCE FIELDS
    private final Context c;

    public MySQLClient(Context c) {
        this.c = c;

    }
    /*
    SAVE/INSERT
     */
    public void login(String u,String p) {
        if(u==null || p==null)
        {
            Toast.makeText(c, "Remplissez tous les champs SVP", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","login")
                    .addBodyParameter("user",u)
                    .addBodyParameter("password",p)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null)
                                try {

                                    //SHOW RESPONSE FROM SERVER
                                    id=response.getJSONObject(0).getInt("id");
                                    Toast.makeText(c, "Success", Toast.LENGTH_SHORT).show();

                                    // String responseString= response.get(0).toString();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(c, "Nom d'utilisateur où le mot de passe sont incorrectes "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(c, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        }


                    });

        }

    }
    public int getID(){
        return id;
    }

    public String get_password(String mail) {
        if(mail==null)
        {
            Toast.makeText(c, "Remplissez tous les champs SVP", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","send_p")
                    .addBodyParameter("mail",mail)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null)
                                try {

                                    //SHOW RESPONSE FROM SERVER
                                    password = response.getJSONObject(0).getString("password");
                                    // String responseString= response.get(0).toString();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(c, "Email non valide où n'existe pas"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(c, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        }


                    });

        }
        return password;
    }

    public Object[][] livraison_tab(){

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","select_livraison")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                //SHOW RESPONSE FROM SERVER
                                for(i=0;i<response.length();i++) {
                                    o[i][0] = response.getJSONObject(0).getInt("id");
                                    // String responseString= response.get(0).toString();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(c, "Nom d'utilisateur où le mot de passe sont incorrectes "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(c, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    }


                });



        return o;
    }



}
