package fr.phoenix_entreprise.braintocapp;

import android.app.Application;
import java.lang.String;

/**
 * Created by valentin on 14/12/2015.
 */

public class Account extends Application {

    private static String login;

    public Account(){
        login = null;
    }

    public Account(String log){
        login = log;
    }

    public String getLogin() {
        return login;
    }

    public static void setLogin(String log) {
        login = log;
    }

}
