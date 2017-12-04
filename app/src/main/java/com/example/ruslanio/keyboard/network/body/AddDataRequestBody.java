package com.example.ruslanio.keyboard.network.body;

/**
 * Created by Ruslanio on 30.11.2017.
 */

public class AddDataRequestBody  {

    private String text;

    public AddDataRequestBody(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
