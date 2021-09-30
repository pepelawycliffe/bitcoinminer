package com.ibrahimtornado.bitcoinminercodecanyon.activity;


import static com.ibrahimtornado.bitcoinminercodecanyon.Config.adMobAds;
import static com.ibrahimtornado.bitcoinminercodecanyon.Config.appLovinAds;
import static com.ibrahimtornado.bitcoinminercodecanyon.Config.quizPoint;
import static com.ibrahimtornado.bitcoinminercodecanyon.Config.userProfile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import java.util.Random;
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
import com.ibrahimtornado.bitcoinminercodecanyon.R;
import com.ibrahimtornado.bitcoinminercodecanyon.quiz.MathQuestions;

public class MathQuizActivity extends AppCompatActivity implements MaxAdViewAdListener {

    private TextView questionText;
    private TextView numberOfQuestion;
    private Button btnAnswerOne, btnAnswerTwo, btnAnswerThree, btnAnswerFour;
    private ProgressDialog pDialog;
    private int questionNumber = 1;
    private int pointsAdded;
    private String theCorrectAnswer;
    private String email ,password;
    private MathQuestions questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

        setToolbar();

        // Admob Ads
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

        // AppLovin Ads
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                if(appLovinAds.contains("True"))
                {
                    ShowMaxBannerAd();
                }
            }
        } );

        numberOfQuestion = (TextView) findViewById(R.id.NumberOf_Question);
        questionText = (TextView) findViewById(R.id.Question_text);
        btnAnswerOne = (Button) findViewById(R.id.btn_Answer_one);
        btnAnswerTwo = (Button) findViewById(R.id.btn_Answer_two);
        btnAnswerThree = (Button) findViewById(R.id.btn_Answer_three);
        btnAnswerFour = (Button) findViewById(R.id.btn_Answer_four);

        questionText.setShadowLayer(5, 1, 1, Color.BLACK);

        SharedPreferences prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE);
        email = prefs.getString("userEmail", "");
        password = prefs.getString("userPassword", "");

        btnAnswerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnAnswerOne.getText().toString() == theCorrectAnswer)
                {
                    setPoints();
                    delayNextQuestion();
                    CorrectAnswer(btnAnswerOne);
                }
                else
                {
                    delayExit();
                    WrongAnswer(btnAnswerOne);
                }
            }
        });

        btnAnswerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnAnswerTwo.getText().toString() == theCorrectAnswer)
                {
                    setPoints();
                    delayNextQuestion();
                    CorrectAnswer(btnAnswerTwo);
                }
                else
                {
                    delayExit();
                    WrongAnswer(btnAnswerTwo);
                }
            }
        });

        btnAnswerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnAnswerThree.getText().toString() == theCorrectAnswer)
                {
                    setPoints();
                    delayNextQuestion();
                    CorrectAnswer(btnAnswerThree);
                }
                else
                {
                    delayExit();
                    WrongAnswer(btnAnswerThree);
                }
            }
        });

        btnAnswerFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnAnswerFour.getText().toString() == theCorrectAnswer)
                {
                    setPoints();
                    delayNextQuestion();
                    CorrectAnswer(btnAnswerFour);
                }
                else
                {
                    delayExit();
                    WrongAnswer(btnAnswerFour);
                }
            }
        });


        getNextQuestion();
        openProgressBar();
        new SendRequest().execute();

    }

    private void setPoints()
    {
        pointsAdded = pointsAdded + quizPoint;
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.toolbar_math_quiz);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void ShowMaxBannerAd()
    {
        MaxAdView maxBannerAdView = findViewById(R.id.MaxAdView);
        maxBannerAdView.loadAd();
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

                if (responseCode == HttpsURLConnection.HTTP_OK)
                {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null)
                    {
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

            Pattern pattern2 = Pattern.compile("M1(.*?)M2");
            Matcher matcher2 = pattern2.matcher(result);

            if (matcher2.find())
            {
                int mocouns = Integer.parseInt(matcher2.group(1));

            }

            if (result.contains("Sorry, your email or password is incorrect"))
            {
                Intent intent = new Intent(MathQuizActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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


    private void delayNextQuestion()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                questionNumber++;
                getNextQuestion();
                enableClickButton();
            }
        }, 1500);
    }

    private void getNextQuestion()
    {

        if (questionNumber == 11)
        {
            Bundle bundle = new Bundle();
            bundle.putInt("theResult", questionNumber - 1);
            bundle.putInt("thePoints", pointsAdded);
            bundle.putInt("theLevel", 1);
            Intent outIntent = new Intent(MathQuizActivity.this, QuizResultActivity.class);
            outIntent.putExtras(bundle);
            startActivity(outIntent);
            finish();
        }
        else
        {
            numberOfQuestion.setText("" + getString(R.string.Question) + " " + questionNumber);
            setQuestionAnimation();
            setOptionsAnimation();

            int questionRandomId = randomNumber(1, 70);
            String[] theQuestion = questions.getMathQuestionAndOptions(questionRandomId);
            questionText.setText(theQuestion[0]);
            btnAnswerOne.setText(theQuestion[1]);
            btnAnswerTwo.setText(theQuestion[2]);
            btnAnswerThree.setText(theQuestion[3]);
            btnAnswerFour.setText(theQuestion[4]);
            theCorrectAnswer = theQuestion[5];

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnAnswerOne.setBackground(getDrawable(R.drawable.exit_button));
                btnAnswerTwo.setBackground(getDrawable(R.drawable.exit_button));
                btnAnswerThree.setBackground(getDrawable(R.drawable.exit_button));
                btnAnswerFour.setBackground(getDrawable(R.drawable.exit_button));
            }
        }

    }

    private void delayExit()
    {
        disableClickButton();

        if (btnAnswerOne.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btnAnswerOne);
        }
        if (btnAnswerTwo.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btnAnswerTwo);
        }
        if (btnAnswerThree.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btnAnswerThree);
        }
        if (btnAnswerFour.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btnAnswerFour);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putInt("theResult", questionNumber - 1);
                bundle.putInt("thePoints", pointsAdded);
                bundle.putInt("theLevel", 1);
                Intent outIntent = new Intent(MathQuizActivity.this, QuizResultActivity.class);
                outIntent.putExtras(bundle);
                startActivity(outIntent);
                finish();
            }
        }, 1500);
    }

    private void CorrectAnswer(Button button)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackground(getDrawable(R.drawable.btn_correct_answer));
        }
        else {
            button.setBackgroundColor(getResources().getColor(R.color.green));
        }
    }

    private void WrongAnswer(Button button)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackground(getDrawable(R.drawable.btn_wrong_answer));
        }
        else {
            button.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    private void enableClickButton()
    {
        btnAnswerOne.setClickable(true);
        btnAnswerTwo.setClickable(true);
        btnAnswerThree.setClickable(true);
        btnAnswerFour.setClickable(true);
    }

    private void disableClickButton()
    {
        btnAnswerOne.setClickable(false);
        btnAnswerTwo.setClickable(false);
        btnAnswerThree.setClickable(false);
        btnAnswerFour.setClickable(false);
    }

    private int randomNumber(int min, int max)
    {
        Random random = new Random();
        int rr = random.nextInt(max - min) + min;

        return rr;
    }


    private void setQuestionAnimation()
    {
        Animation queAnim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        questionText.startAnimation(queAnim);
    }

    private void setOptionsAnimation()
    {
        Animation queAnim = AnimationUtils.loadAnimation(this, R.anim.in_right);
        btnAnswerOne.startAnimation(queAnim);
        btnAnswerTwo.startAnimation(queAnim);
        Animation queAnimfast = AnimationUtils.loadAnimation(this, R.anim.in_right);
        btnAnswerThree.startAnimation(queAnimfast);
        btnAnswerFour.startAnimation(queAnimfast);
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
}
