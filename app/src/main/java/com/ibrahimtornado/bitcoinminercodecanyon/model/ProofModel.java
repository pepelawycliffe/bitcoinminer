package com.ibrahimtornado.bitcoinminercodecanyon.model;

public class ProofModel {

    public int id;
    public String username;
    public String amount;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}