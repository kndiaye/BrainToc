package fr.phoenix_entreprise.braintocapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * First Game: The reversal learning
 */
public class Game1Activity extends AppCompatActivity {

    // PARAMETERS /////////////////////////////
    ///////////////////////////////////////////
    private int nbOfTryToReverse = 10;  // Number of try needed to make the reversal
    private int nbOfReverseToEnd = 4;   // Number of reversal needed to stop the activity
    ///////////////////////////////////////////

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
    private int winIndic = 0; //To change the color of the win indicator
    private int looseIndic = 0; //To change the color of the loose indicator

    @Override
    /*************************************************************
     * Set the right layout. Define the element and init the game
     *************************************************************/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        picL = (ImageView) findViewById(R.id.picL);
        picR = (ImageView) findViewById(R.id.picR);
        TVscore = (TextView) findViewById(R.id.tvScore);
        initGame();

    }


    /*************************************************************
     * Init variable and choose randomly the picture for the activity
     *************************************************************/
    private void initGame() {
        Random randGen = new Random();

        //Init the variable
        score = 0;
        numberOfTryWin = 0;
        numberOfReverse = 0;
        history = "START_" + dateNow();

        //Define randomly the two pictures
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

        //Define randomly the winner picture
        winner = "L";
        if (randGen.nextInt(1) == 0){
             winner = "R";
        }
    }


    /*************************************************************
     * Action that append when the user click on a picture
     *************************************************************/
    public void onClickAction(View view) {

        int id = view.getId(); //Id of the picture choosen

        // Make a random number to decide if the user really win
        // Point will be 0 (1/10 chance) or 1 (9/10 chances)
        Random randGen = new Random();
        float point = randGen.nextInt(9);
        point = (float) (point/10 + 0.4);
        point = Math.round(point);


        /////////////////////////////// WIN
        if ((id == picL.getId() && winner == "L") || (id == picR.getId() && winner == "R")){       //WIN AND LEFT
            score += point;
            numberOfTryWin += 1;
            history += "_"+(int)point;

            // Change the indicator on screen
            if (score == 0){
                looseIndicator();
            }
            else {
                winIndicator();
            }
        }
        /////////////////////////////// LOOSE
        else {
            score -= point;
            numberOfTryWin = 0;
            history += "_-"+(int)point;
            looseIndicator();
        }
        ///////////////////////////////

        history += "_" + dateNow();

        //MAKE A REVERSE OF THE LEARN
        if (numberOfTryWin >= nbOfTryToReverse){
            winner = changeWiner(winner);       // Change the winner picture
            numberOfReverse +=1;                // Count the number of reversal
            history += "_REV";
        }

        //SHOW THE SCORE BOARD (AND SEND DATA)
        if (numberOfReverse > nbOfReverseToEnd) {
            history += "_END" + dateNow();
            finish(view);
        }

        //System.out.println(history);

        changePic(); //CHANGE PICTURE DISPOSITION
    }


    /*************************************************************
     * 50% of chance to invert the pictures
     *************************************************************/
    public void changePic(){

        Random randGen = new Random();
        int change = randGen.nextInt(2);

        if (change == 1){                           // Invert Pictures
            String picInter = pic1stimuli;
            pic1stimuli = pic2stimuli;
            pic2stimuli = picInter;
            picL.setImageDrawable(getResources().getDrawable(getResourceID(pic1stimuli, "drawable",
                    getApplicationContext())));
            picR.setImageDrawable(getResources().getDrawable(getResourceID(pic2stimuli, "drawable",
                    getApplicationContext())));

            winner = changeWiner(winner);          // Change the winner picture
        }
    }

    /*************************************************************
     * Return the other side
     *************************************************************/
    public String changeWiner(String winner){
        if (winner == "R"){
            return "L";
        }
        else {
            return "R";
        }
    }

    /*************************************************************
     * Change the indicatore to green for win
     *************************************************************/
    public void winIndicator(){
        FrameLayout FL = (FrameLayout) findViewById(R.id.Indicator);
        if(winIndic == 0){
            FL.setBackground(getResources().getDrawable(R.drawable.win_indicator));
            winIndic = 1;
        }
        else{
            FL.setBackground(getResources().getDrawable(R.drawable.win2_indicator));
            winIndic = 0;
        }
    }

    /*************************************************************
     * Change the indicator to red for loose
     *************************************************************/
    public void looseIndicator() {
        FrameLayout FL = (FrameLayout) findViewById(R.id.Indicator);
        if(looseIndic == 0) {
            FL.setBackground(getResources().getDrawable(R.drawable.loose_indicator));
            looseIndic = 1;
        }
        else{
            FL.setBackground(getResources().getDrawable(R.drawable.loose2_indicator));
            looseIndic = 0;
        }
    }


    /*************************************************************
     * Return the date of the moment
     *************************************************************/
    public String dateNow(){
        java.util.Date uDate = new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyy:hhmmss");
        return dateFormat.format(uDate);
    }

    /*************************************************************
     * Switch to score activity
     *************************************************************/
    public void finish(View view) {
        Intent intent = new Intent(this, ScoreGame1Activity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
    }

    /*************************************************************
     * Search for an ID
     *************************************************************/
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
