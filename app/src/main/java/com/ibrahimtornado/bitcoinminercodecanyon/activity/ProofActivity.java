package com.ibrahimtornado.bitcoinminercodecanyon.activity;

import static com.ibrahimtornado.bitcoinminercodecanyon.Config.paymentProof;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ibrahimtornado.bitcoinminercodecanyon.R;
import com.ibrahimtornado.bitcoinminercodecanyon.adapter.ProofAdapter;
import com.ibrahimtornado.bitcoinminercodecanyon.model.ProofModel;

public class ProofActivity extends AppCompatActivity {

    private ArrayList<ProofModel> dataArrayList = new ArrayList<ProofModel>();
    private RecyclerView recyclerView;
    private ProofAdapter mAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof);

        setToolbar();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.leader_view_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        setProgressBar(View.VISIBLE);


        getServerData();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.toolbar_proof);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void getServerData() {

        String urlGetServerData = paymentProof;
        System.out.print(urlGetServerData);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetServerData,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setProgressBar(View.GONE);
                        try {
                            Gson gson = new Gson();
                            JSONArray jsonArray = response.getJSONArray("paymentProof");

                            for (int p = 0; p <jsonArray.length(); p++){
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                ProofModel data = gson.fromJson(String.valueOf(jsonObject), ProofModel.class);
                                dataArrayList.add(data);
                                Collections.sort(dataArrayList, new Comparator<ProofModel>() {
                                    @Override
                                    public int compare(ProofModel lhs, ProofModel rhs) {
                                        Integer price1 = lhs.getId();
                                        Integer price2 = rhs.getId();
                                        return price2.compareTo(price1);
                                    }
                                });
                            }
                            mAdapter = new ProofAdapter(ProofActivity.this, dataArrayList);
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }



    private void setProgressBar(int view)
    {
        progressBar.setVisibility(view);
    }
}