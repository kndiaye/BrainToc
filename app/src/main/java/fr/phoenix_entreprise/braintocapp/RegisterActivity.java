package fr.phoenix_entreprise.braintocapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by valentin on 14/12/2015.
 */
public class RegisterActivity extends AppCompatActivity{

    private static final String REGISTER_URL = "http://192.168.24.41/HumanICM/inscription.php";
    public static final String KEY_PSEUDO = "pseudo";
    public static final String KEY_NAISSANCE = "naissance";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SEXE = "sexe";

    private EditText editTextPseudo;
    private EditText editTextNaissance;
    private EditText editTextPassword;
    private RadioGroup groupSex;

    private static String date;
    private static boolean goNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check if connected or not
        final Account acc_log = (Account) getApplicationContext();
        final String log  = acc_log.getLogin();
        if(log==null){ // pas connecté
            setContentView(R.layout.activity_register);

            editTextPseudo = (EditText) findViewById(R.id.TextPseudonyme);
            editTextPassword = (EditText) findViewById(R.id.TextMdp);
            groupSex = (RadioGroup) findViewById(R.id.radioGroupSexe);
        }
        else{ // connecté
            setContentView(R.layout.activity_connected);
            TextView logView = (TextView) findViewById(R.id.ViewMessageConnected);
            logView.setText("Vous êtes déjà connecté en tant que " + log + ".");
        }
    }

    public static String getDate(){
        return date;
    }

    public static void setDate(String t_date){
        date = t_date;
    }

    public static boolean getGoNext(){
        return goNext;
    }

    public static void setGoNext(boolean next){
        goNext = next;
    }

    public void register(View view) throws JSONException{
        RegisterActivity.setGoNext(false);
        final String pseudo = editTextPseudo.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        int idSex = groupSex.getCheckedRadioButtonId();
        if(idSex == R.id.radioButtonHomme){ idSex = 1; }
        if(idSex == R.id.radioButtonFemme){ idSex = 0; }
        final int sex = idSex;

        if(pseudo.isEmpty()
                ||date==null
                ||password.isEmpty()
                ||String.valueOf(sex).isEmpty()){
            Toast.makeText(RegisterActivity.this, "Champs non renseignés", Toast.LENGTH_LONG).show();
        }
        else {

            Log.i(KEY_PSEUDO, pseudo);
            Log.i(KEY_NAISSANCE, date);
            Log.i(KEY_PASSWORD, password);
            Log.i(KEY_SEXE, String.valueOf(sex));

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response: ", String.valueOf(response));
                            String res_text = "";
                            switch(response){
                                case "1":
                                    res_text = "Pseudonyme déjà pris !";
                                    break;
                                case "2":
                                    res_text = "Enregistrement réussi !";
                                    RegisterActivity.setGoNext(true);
                                    break;
                                case "3":
                                    res_text = "Echec de l'enregistrement !";
                                    break;
                                case "4":
                                    res_text = "Erreur";
                                    break;

                            }
                            Toast.makeText(RegisterActivity.this, res_text, Toast.LENGTH_LONG).show();
                            nextScreen(RegisterActivity.getGoNext());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Response: ", error.toString());
                            Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            nextScreen(RegisterActivity.getGoNext());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_PSEUDO, pseudo);
                    params.put(KEY_NAISSANCE, date);
                    params.put(KEY_PASSWORD, password);
                    params.put(KEY_SEXE, String.valueOf(sex));
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
            Toast.makeText(RegisterActivity.this, "Connexion en cours ...", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextScreen(boolean next){
        if(next) {
            Intent intent = new Intent(this, ConnectionActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month+1;
            Log.i("année",String.valueOf(year));
            Log.i("mois",String.valueOf(month));
            Log.i("jour",String.valueOf(day));
            String temp_date = String.valueOf(year)+'-'+String.valueOf(month)+'-'+String.valueOf(day);
            Log.i("date",temp_date);
            RegisterActivity.setDate(temp_date);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Selection date");
    }

    public void retourMenu(View view) {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }
}
