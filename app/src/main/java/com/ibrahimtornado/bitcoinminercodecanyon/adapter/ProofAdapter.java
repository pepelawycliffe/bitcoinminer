package com.ibrahimtornado.bitcoinminercodecanyon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.ibrahimtornado.bitcoinminercodecanyon.R;
import com.ibrahimtornado.bitcoinminercodecanyon.model.ProofModel;

public class ProofAdapter extends RecyclerView.Adapter<ProofAdapter.ProofViewHolder>{

    Context context;
    ArrayList<ProofModel> proofAdapter;

    public ProofAdapter(Context context,ArrayList<ProofModel> proofAdapter){
        this.context = context;
        this.proofAdapter = proofAdapter;
    }

    View view;

    @Override
    public ProofViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.item_payments_proof, parent,false);
        ProofViewHolder rvViewHolder = new ProofViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(ProofViewHolder holder, final int position)
    {
        ProofModel data = proofAdapter.get(position);

        final int id = data.getId();
        final String name = data.getUsername();
        String amount = data.getAmount();

        int leader = position + 1;

        holder.idView.setText("" + leader);
        holder.nameView.setText(name);
        holder.scoreView.setText("" + amount);

    }

    @Override
    public int getItemCount() {
        return proofAdapter.size();
    }

    public class ProofViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, idView, scoreView;

        public ProofViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.NameTextView);
            idView = itemView.findViewById(R.id.IdTextView);
            scoreView = itemView.findViewById(R.id.ScoreTextView);
        }
    }
}