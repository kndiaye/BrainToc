package fr.phoenix_entreprise.braintocapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class ConnectionActivity extends AppCompatActivity {

    private static final String CONNEXION_URL = "http://192.168.24.41/HumanICM/connexion.php";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    private EditText editTextPseudo;
    private EditText editTextPassword;

    private static boolean connected;

    public static boolean getConnected(){
        return connected;
    }

    public static void setConnected(boolean co){
        connected = co;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Ajouter if (si connecté ou pas)
        setContentView(R.layout.activity_connection);

        editTextPseudo = (EditText) findViewById(R.id.TextPseudonyme);
        editTextPassword = (EditText) findViewById(R.id.TextMdp);
    }

    public void connexion(View view) throws JSONException {
        ConnectionActivity.setConnected(false);
        final String pseudo = editTextPseudo.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if(pseudo.isEmpty()
                ||password.isEmpty()){
            Toast.makeText(ConnectionActivity.this, "Champs non renseignés", Toast.LENGTH_LONG).show();
        }
        else {
            Log.i(KEY_LOGIN, pseudo);
            Log.i(KEY_PASSWORD, password);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, CONNEXION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response: ", response);
                            String res_text = "";
                            switch(response){
                                case "1":
                                    res_text = "Connexion réussi !";
                                    ConnectionActivity.setConnected(true);
                                    break;
                                case "2":
                                    res_text = "Mauvais mot de passe !";
                                    break;
                                case "3":
                                    res_text = "Mauvais pseudonyme !";
                                    break;
                                case "4":
                                    res_text = "Erreur";
                                    break;

                            }
                            Toast.makeText(ConnectionActivity.this, res_text, Toast.LENGTH_LONG).show();
                            nextScreen(ConnectionActivity.getConnected());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Response: ", error.toString());
                            Toast.makeText(ConnectionActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            nextScreen(ConnectionActivity.getConnected());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_LOGIN, pseudo);
                    params.put(KEY_PASSWORD, password);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
            Toast.makeText(ConnectionActivity.this, "Connexion en cours ...", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextScreen(boolean next){
        if(next) {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ConnectionActivity.class);
            startActivity(intent);
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}
