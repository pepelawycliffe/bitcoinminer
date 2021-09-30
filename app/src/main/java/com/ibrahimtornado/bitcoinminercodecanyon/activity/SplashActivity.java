package com.ibrahimtornado.bitcoinminercodecanyon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ibrahimtornado.bitcoinminercodecanyon.MainActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private String userEmail, userPassword;
    private SharedPreferences prefs;
    private ProgressBar progressBar;
    private LinearLayout lytNoConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        logo = (ImageView) findViewById(R.id.logo);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        lytNoConnection = (LinearLayout) findViewById(R.id.lyt_no_connection);

        progressBar.setVisibility(View.GONE);
        lytNoConnection.setVisibility(View.GONE);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        logo.startAnimation(myanim);

        prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE);
        userEmail = prefs.getString("userEmail", "");
        userPassword = prefs.getString("userPassword", "");

        int splashTimeOut = 2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isConnectingToInternet(SplashActivity.this)) {
                    getTheData();
                } else {
                    showNoInterNet();
                }

            }
        }, splashTimeOut);


        lytNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                progressBar.setVisibility(View.VISIBLE);
                lytNoConnection.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isConnectingToInternet(SplashActivity.this)) {
                            getTheData();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            lytNoConnection.setVisibility(View.VISIBLE);
                        }
                    }
                }, 1000);
            }
        });

    }

    private void getTheData() {
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

    private void showNoInterNet() {
        logo.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);
        lytNoConnection.setVisibility(View.VISIBLE);

    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


}
