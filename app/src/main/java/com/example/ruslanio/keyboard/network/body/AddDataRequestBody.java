package com.example.ruslanio.keyboard.network.body;

/**
 * Created by Ruslanio on 30.11.2017.
 */

public class AddDataRequestBody  {

    private String text;
    private String date;



    public AddDataRequestBody(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
