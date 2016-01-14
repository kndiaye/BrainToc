package fr.phoenix_entreprise.braintocapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by valentin on 14/12/2015.
 */
public class RegisterActivity extends AppCompatActivity{

    private static final String REGISTER_URL = "http://192.168.24.41/HumanICM/inscription.php";

    public static final String KEY_PSEUDO = "pseudo";
    public static final String KEY_NAISSANCE = "naissance";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SEXE = "sexe";

    private EditText editTextPseudo;
    private EditText editTextNaissance;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private RadioGroup groupSex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ajouter if (si connecté ou pas)
        setContentView(R.layout.activity_register);

        editTextPseudo = (EditText) findViewById(R.id.TextPseudonyme);
        editTextNaissance = (EditText) findViewById(R.id.TextNaissance);
        editTextLogin = (EditText) findViewById(R.id.TextLogin);
        editTextPassword = (EditText) findViewById(R.id.TextMdp);
        groupSex = (RadioGroup) findViewById(R.id.radioGroupSexe);
    }

    public void register(View view) throws JSONException{

        final String pseudo = editTextPseudo.getText().toString().trim();
        final String naissance = editTextNaissance.getText().toString().trim();
        final String login = editTextLogin.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        int idSex = groupSex.getCheckedRadioButtonId();
        if(idSex == R.id.radioButtonHomme){ idSex = 1; }
        if(idSex == R.id.radioButtonFemme){ idSex = 0; }
        final int sex = idSex;
        Log.i(KEY_PSEUDO, pseudo);
        Log.i(KEY_NAISSANCE, naissance);
        Log.i(KEY_LOGIN, login);
        Log.i(KEY_PASSWORD, password);
        Log.i(KEY_SEXE, String.valueOf(sex));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response: ", error.toString());
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_PSEUDO, pseudo);
                params.put(KEY_NAISSANCE, naissance);
                params.put(KEY_LOGIN, login);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_SEXE, String.valueOf(sex));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }
}
