package fr.phoenix_entreprise.braintocapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * First Game: The reversal learning
 */
public class Game1Activity extends AppCompatActivity {

    // PARAMETERS /////////////////////////////
    ///////////////////////////////////////////
    private int nbOfTryToReverse = 1;  // Number of try needed to make the reversal
    private int nbOfReverseToEnd = 2;   // Number of reversal needed to stop the activity
    ///////////////////////////////////////////

    private String history = "";
    // HISTORY ////////////////////////////////
    ///////////////////////////////////////////
    // STARTDATE,STARTTIME:
    // N°try; TIME ONSET ; PIC POSITION; WINNER ; TIME PRESS ; CHOICE ; FEEDBACK :  }loop
    // REVERSAL:    }sometime
    // ENDDATE,ENDTIME:
    //
    // Exemple: 090216,024940:1.314;L;L:1;1.319;L1R2,L;8.465;R;L:2;8.475;L1R2,R;10.161;R;W:Reversal:3;10.165;L1R2,R;10.336;R;0:Reversal:090216,025030
    ///////////////////////////////////////////


    private ImageView picL;     //For Left
    private ImageView picR;     //For Right
    private String winner;      //The Right Picture
    private int score = 0;          //Actual Score
    private int numberofTry = 0;    //To count the number of try
    private int numberOfTryWin = 0; //To count the number of chain win
    private int numberOfReverse = 0;    //To count the number of winner reverse
    private java.util.Date startdate;

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
        startdate = dateNow();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyy,hhmmss");
        history =  dateFormat.format(startdate) + ":";                  // #R STARTDATE,STARTTIME

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

        history += time() + ";"; // #H Time press

        if (id == picL.getId()){ history += "L;";}   // #H Choice
        else{ history += "R;";}                      // #H Choice

        /////////////////////////////// WIN
        if ((id == picL.getId() && winner == "L") || (id == picR.getId() && winner == "R")){
            score += point;
            numberOfTryWin += 1;

            // Change the indicator on screen
            if (score == 0){
                looseIndicator();
                history += "0:";                      // #H Win but negative feedback
            }
            else {
                winIndicator();
                history += "W:";                      // #H Win and positive feedback
                //winIndicator(id);
            }
        }
        /////////////////////////////// LOOSE
        else {
            score -= point;
            numberOfTryWin = 0;
            history += "L:";                      // #H Win but negative feedback
            looseIndicator();
        }
        ///////////////////////////////


        //MAKE A REVERSE OF THE LEARN
        if (numberOfTryWin >= nbOfTryToReverse){
            winner = changeWiner(winner);       // Change the winner picture
            numberOfReverse +=1;                // Count the number of reversal
            history += "Reversal:";             // #H Reversal
        }

        //SHOW THE SCORE BOARD (AND SEND DATA)
        if (numberOfReverse > nbOfReverseToEnd) {
            DateFormat dateFormat = new SimpleDateFormat("ddMMyy,hhmmss");
            history += dateFormat.format(dateNow());        // #H End of the game
            finish(view);
        }

        //System.out.println(history);

        //picL.setBackground(getResources().getDrawable(R.drawable.normal_indicator));
        //picR.setBackground(getResources().getDrawable(R.drawable.normal_indicator));
        numberofTry +=1;
        history += numberofTry + ";";  // #H Number of try

        changePic(); //CHANGE PICTURE DISPOSITION
    }


    /*************************************************************
     * 50% of chance to invert the pictures
     *************************************************************/
    public void changePic(){

        Random randGen = new Random();
        int change = randGen.nextInt(2);

        history += time() + ";"; // #H Time onset

        if (change == 1){                           // Invert Pictures
            String picInter = pic1stimuli;
            pic1stimuli = pic2stimuli;
            pic2stimuli = picInter;
            picL.setImageDrawable(getResources().getDrawable(getResourceID(pic1stimuli, "drawable",
                    getApplicationContext())));
            picR.setImageDrawable(getResources().getDrawable(getResourceID(pic2stimuli, "drawable",
                    getApplicationContext())));

            winner = changeWiner(winner);          // Change the winner picture

            history += "L1R2,"; // #H Pic Position (left pic1, right pic2)
        }
        else{
            history += "L2R1,"; // #H Pic Position (left pic2, right pic1)
        }

        history += winner + ";"; // #H Winner between the two pictures

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

    /*
    public void winIndicator(int pic){
        FrameLayout FL = (FrameLayout) findViewById(R.id.Indicator);
        if(winIndic == 0){
            FL.setBackground(getResources().getDrawable(R.drawable.win_indicator));
            winIndic = 1;
        }
        else{
            FL.setBackground(getResources().getDrawable(R.drawable.win2_indicator));
            winIndic = 0;
        }

        ImageView IV = (ImageView) findViewById(pic);
        IV.setBackground(getResources().getDrawable(R.drawable.win_indicator));
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }*/

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
    /*public String dateNow(){
        java.util.Date uDate = new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyy,hhmmss");
        return dateFormat.format(uDate);
    }*/
    public java.util.Date dateNow(){
        java.util.Date uDate = new java.util.Date();
        return uDate;
    }

    /*************************************************************
     * Return the date of the moment
     *************************************************************/
    public float time(){
        java.util.Date uDate = new java.util.Date();
        float diff = (float) (uDate.getTime() - startdate.getTime())/1000;
        return diff;
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
