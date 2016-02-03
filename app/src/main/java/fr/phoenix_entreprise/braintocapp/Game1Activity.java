package fr.phoenix_entreprise.braintocapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by valentin on 27/01/2016.
 */
public class Game1Activity extends AppCompatActivity {

    private ImageView picL;     //For Left
    private ImageView picR;     //For Right
    private String winner;      //The Right Picture
    private int score;          //Actual Score
    private TextView TVscore;   //
    private String pic1stimuli;
    private String pic2stimuli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("aa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        picL = (ImageView) findViewById(R.id.picL);
        picR = (ImageView) findViewById(R.id.picR);
        TVscore = (TextView) findViewById(R.id.tvScore);
        initGame();

    }


    private void initGame() {
        Random randGen = new Random();

        //Define the two pictures
        int rand1 = 1+randGen.nextInt(24);
        int rand2 = 1+randGen.nextInt(24);
        while (rand1 == rand2){
            rand2 = 1+randGen.nextInt(24);
        }
        pic1stimuli = "stim" + rand1;
        pic2stimuli = "stim" + rand2;
        picL.setImageDrawable(getResources().getDrawable(getResourceID(pic1stimuli, "drawable",
                        getApplicationContext())));
        picR.setImageDrawable(getResources().getDrawable(getResourceID(pic2stimuli, "drawable",
                getApplicationContext())));

        //Define the winner picture
        winner = "L";
        if (randGen.nextInt(1) == 0){
             winner = "R";
        }



    }


    public void onClickAction(View view) {// Gestion du jeu

        int id = view.getId();
        Random randGen = new Random();
        float point = randGen.nextInt(9);
        point = (float) (point/10 + 0.4);
        point = Math.round(point);
        if ((id == picL.getId() && winner == "L") || (id == picR.getId() && winner == "R")){       //WIN AND LEFT
            score += point;
        }
        else {                                                                                      //LOOSE
            score -= point;
        }
        refreshScore();


        changePic();                                                                                //CHANGE PICTURE DISPOSITION
    }

    private void refreshScore(){
        TVscore.setText(getString(R.string.score_name, score));
    }

    public void changePic(){
        Random randGen = new Random();
        int change = randGen.nextInt(2);
        if (change == 1){                           // Intervertion des images
            String picInter = pic1stimuli;
            pic1stimuli = pic2stimuli;
            pic2stimuli = picInter;
            picL.setImageDrawable(getResources().getDrawable(getResourceID(pic1stimuli, "drawable",
                    getApplicationContext())));
            picR.setImageDrawable(getResources().getDrawable(getResourceID(pic2stimuli, "drawable",
                    getApplicationContext())));
            if (winner == "R"){// Changement du gagnant
                winner = "L";
            }
            else{
                winner = "R";
            }
        }

    }


    //
    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }

}
