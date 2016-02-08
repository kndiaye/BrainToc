package fr.phoenix_entreprise.braintocapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by valentin on 27/01/2016.
 */
public class Game1Activity extends AppCompatActivity {

    private ImageView picL;     //For Left
    private ImageView picR;     //For Right
    private String winner;      //The Right Picture
    private int score = 0;          //Actual Score
    private int numberOfTryWin = 0; //To count the number of chain win
    private int numberOfReverse = 0;    //to count the number of winner reverse
    private String history = "";
    private TextView TVscore;           //Score View
    private String pic1stimuli;
    private String pic2stimuli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        picL = (ImageView) findViewById(R.id.picL);
        picR = (ImageView) findViewById(R.id.picR);
        TVscore = (TextView) findViewById(R.id.tvScore);
        initGame();

    }


    private void initGame() {
        Random randGen = new Random();

        score = 0;
        numberOfTryWin = 0;
        numberOfReverse = 0;
        history = "START_" + dateNow();

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
        //System.out.println(point);
        if ((id == picL.getId() && winner == "L") || (id == picR.getId() && winner == "R")){       //WIN AND LEFT
            score += point;
            numberOfTryWin += 1;
            history += "_"+(int)point;
            if (score == 0){
                looseIndicator();
            }
            else {
                winIndicator();
            }
        }
        else {                                                                                      //LOOSE
            score -= point;
            numberOfTryWin = 0;
            history += "_-"+(int)point;
            looseIndicator();
        }

        history += "_" + dateNow();   //HYSTORY

        if (numberOfTryWin >= 10){                                                                  //MAKE A REVERSE OF THE LEARN
            winner = changeWiner(winner);
            numberOfReverse +=1;
            history += "_REV";
        }
        if (numberOfReverse > 4) {                                                                   //START A NEW ACTIVITY (AND SEND DATA)
            history += "_END" + dateNow();
            finish(view);
        }

        //System.out.println(history);

        changePic();                                                                                //CHANGE PICTURE DISPOSITION
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
            winner = changeWiner(winner);          // Changement du gagnant
        }
    }

    public void winIndicator(){
        FrameLayout FL = (FrameLayout) findViewById(R.id.Indicator);
        FL.setBackground(getResources().getDrawable(R.drawable.win_indicator));
        /*try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FL.setBackground(getResources().getDrawable(R.drawable.normal_indicator));*/
    }

    public void looseIndicator() {
        FrameLayout FL = (FrameLayout) findViewById(R.id.Indicator);
        FL.setBackground(getResources().getDrawable(R.drawable.loose_indicator));
    }

    public String changeWiner(String winner){
        if (winner == "R"){// Changement du gagnant
            return "L";
        }
        else {
            return "R";
        }
    }

    public String dateNow(){
        java.util.Date uDate = new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyy:hhmmss");
        return dateFormat.format(uDate);
    }

    //Switch to score activity
    public void finish(View view) {
        Intent intent = new Intent(this, ScoreGame1Activity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
    }


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
