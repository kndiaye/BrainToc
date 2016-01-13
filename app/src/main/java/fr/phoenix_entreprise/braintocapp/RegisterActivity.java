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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by valentin on 14/12/2015.
 */
public class RegisterActivity extends AppCompatActivity{

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
        int sex = groupSex.getCheckedRadioButtonId();
        if(sex == R.id.radioButtonHomme){ sex = 1; }
        if(sex == R.id.radioButtonFemme){ sex = 0; }
        Log.i(KEY_PSEUDO, pseudo);
        Log.i(KEY_NAISSANCE, naissance);
        Log.i(KEY_LOGIN, login);
        Log.i(KEY_PASSWORD, password);
        Log.i(KEY_SEXE, String.valueOf(sex));

        String url = "http://192.168.24.41/HumanICM/inscription.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_PSEUDO, pseudo);
        params.put(KEY_NAISSANCE, naissance);
        params.put(KEY_LOGIN, login);
        params.put(KEY_PASSWORD, password);
        params.put(KEY_SEXE, String.valueOf(sex));

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //try {
                    Log.d("Response: ", response.toString());
                    Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                //} catch (JSONException e) {
                //    // TODO Auto-generated catch block
                //    e.printStackTrace();
                //}

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
                Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }
}
