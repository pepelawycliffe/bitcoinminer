package com.ibrahimtornado.bitcoinminercodecanyon;

import static com.ibrahimtornado.bitcoinminercodecanyon.Config.adMobAds;
import static com.ibrahimtornado.bitcoinminercodecanyon.Config.appLovinAds;
import static com.ibrahimtornado.bitcoinminercodecanyon.Config.userProfile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.AboutActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.FeedbackActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.HelpActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.LoginActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.MathQuizActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.PaymentActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.ProfileActivity;
import com.ibrahimtornado.bitcoinminercodecanyon.activity.ProofActivity;

public class MainActivity extends AppCompatActivity implements MaxAdViewAdListener {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView userName, mocoins;
    private LinearLayout startPlay, paymentsProof, payment, howItWorks, feedback, share, aboutApp;
    private SharedPreferences prefs;
    private ProgressDialog pDialog;
    private String email ,password;
    private MaxAdView adView;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (TextView) findViewById(R.id.userName_Txt);
        mocoins = (TextView) findViewById(R.id.Mocoins_Txt);

        startPlay = (LinearLayout) findViewById(R.id.Start_playBtn);
        paymentsProof = (LinearLayout) findViewById(R.id.Payments_proofBtn);
        payment = (LinearLayout) findViewById(R.id.PaymentBtn);
        howItWorks = (LinearLayout) findViewById(R.id.HowItWorksBtn);
        feedback = (LinearLayout) findViewById(R.id.FeedbackBtn);
        share = (LinearLayout) findViewById(R.id.ShareBtn);
        aboutApp = (LinearLayout) findViewById(R.id.AboutAppBtn);

        setToolbar();
        setBannerAds();

        // Applovin Ads
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads
                if(appLovinAds.contains("True"))
                {
                    ShowMaxBannerAd();
                }
            }
        } );


        prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE);
        email = prefs.getString("userEmail", "");
        password = prefs.getString("userPassword", "");


        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MathQuizActivity.class);
                startActivity(intent);
            }
        });

        paymentsProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProofActivity.class);
                startActivity(intent);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        howItWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String sAux = getString(R.string.Share_Text);
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            }
        });

        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        openProgressBar();
        new SendRequest().execute();

    }

    private void ShowMaxBannerAd()
    {
        MaxAdView maxBannerAdView = findViewById(R.id.MaxAdView);
        maxBannerAdView.loadAd();
    }

    private void setBannerAds()
    {

        if (adMobAds.contains("True"))
        {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            AdView adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        this.toolbarTitle.setText(R.string.toolbar_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onAdExpanded(MaxAd ad) {

    }

    @Override
    public void onAdCollapsed(MaxAd ad) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{
                URL url = new URL(userProfile);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", email);
                postDataParams.put("password", password);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Pattern pattern = Pattern.compile("N1(.*?)N2");
            Pattern pattern2 = Pattern.compile("M1(.*?)M2");
            Matcher matcher = pattern.matcher(result);
            Matcher matcher2 = pattern2.matcher(result);

            if (matcher.find() && matcher2.find())
            {
                userName.setText("Welcome, " + matcher.group(1));
                mocoins.setText("" + matcher2.group(1));

            }

            if (result.contains("Sorry, your email or password is incorrect"))
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            if (result.equals("0"))
            {
                unconfermedAccountDialog();
            }

            closeProgressBar();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    private void unconfermedAccountDialog()
    {
        AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
        build.setTitle("Sorry !");
        build.setMessage("You Have Problem in your Account, please contact us on our email.");
        build.setCancelable(false);

        build.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
                finish();
            }
        });

        AlertDialog olustur = build.create();
        olustur.show();
    }

    protected void openProgressBar(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
    }

    protected void closeProgressBar(){
        pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        openProgressBar();
        new SendRequest().execute();
    }
}