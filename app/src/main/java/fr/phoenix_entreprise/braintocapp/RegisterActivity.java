package fr.phoenix_entreprise.braintocapp;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

/**
 * Created by valentin on 14/12/2015.
 */
public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ajouter if (si connecté ou pas)
        setContentView(R.layout.activity_register);
    }

}
