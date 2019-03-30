package com.example.mytvs.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
Type of data returned by JSON from POST request
 */
public class TableDataResponse {

    @SerializedName("TABLE_DATA")
    @Expose
    private String tableData;

    public String getTableData() {
        return tableData;
    }

    public void setTableData(String tableData) {
        this.tableData = tableData;
    }
}
