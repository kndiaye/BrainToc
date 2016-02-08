package fr.phoenix_entreprise.braintocapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by valentin on 27/01/2016.
 */
public class ScoreGame1Activity extends AppCompatActivity {

    private TextView TVscore;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_game1);

        Intent intent = getIntent();
        score = intent.getIntExtra("SCORE",0);                  // Récupérer le score

        TVscore = (TextView) findViewById(R.id.tvScore);
        TVscore.setText(getString(R.string.score_name, score));               // Ecrire le score
    }


    //Restart
    public void restart(View view) {
        Intent intent = new Intent(this, Game1Activity.class);
        startActivity(intent);
    }

    //Menu
    public void menu(View view) {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }

}
