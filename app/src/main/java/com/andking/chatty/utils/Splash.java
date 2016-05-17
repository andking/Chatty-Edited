package com.andking.chatty.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.andking.chatty.LoginActivity;
import com.andking.chatty.MapActivity;
import com.andking.chatty.R;
import com.parse.ParseUser;

public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5 * 1000);

                    // After 5 seconds redirect to another intent
                    if (ParseUser.getCurrentUser() == null) {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getBaseContext(), MapActivity.class);
                        startActivity(i);
                    }


                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

    }
}
