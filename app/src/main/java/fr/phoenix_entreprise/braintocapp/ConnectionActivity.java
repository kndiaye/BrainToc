package fr.phoenix_entreprise.braintocapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class ConnectionActivity extends AppCompatActivity {

    Account testCompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        testCompte = new Account();

        super.onCreate(savedInstanceState);
        //Ajouter if (si connecté ou pas)
        setContentView(R.layout.activity_connection);

        ImageButton photoAccount = (ImageButton) findViewById(R.id.account);
        int srcPhotoAccount = getResources().getIdentifier(testCompte.getPhoto(), "drawable", getPackageName());
        photoAccount.setImageResource(srcPhotoAccount);


    }

    public void connection(View view) {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}
