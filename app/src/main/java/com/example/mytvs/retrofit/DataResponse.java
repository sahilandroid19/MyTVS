package com.example.mytvs.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
Inside JSON object of data returned
 */
public class DataResponse {

    @SerializedName("data")
    @Expose
    private List<List<String>> dataList = null;

    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
    }
}
