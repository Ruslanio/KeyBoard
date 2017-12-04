
package com.example.ruslanio.keyboard.network.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerResponce {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

}
