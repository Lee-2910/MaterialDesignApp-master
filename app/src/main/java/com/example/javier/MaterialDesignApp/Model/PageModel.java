package com.example.javier.MaterialDesignApp.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Envy on 13/03/2015.
 */
public class PageModel {

    private String totalView;
    private String totalRow;
    private String page;
    private String totalPage;

    public String getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(String totalRow) {
        this.totalRow = totalRow;
    }

    public String getTotalView() {
        return totalView;
    }

    public void setTotalView(String totalView) {
        this.totalView = totalView;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public PageModel(JSONObject jsonObject) {
        try {
            this.totalView = jsonObject.getString("totalview");
            this.totalRow = jsonObject.getString("totalrow");
            this.page = jsonObject.getString("page");
            this.totalPage = jsonObject.getString("totalpage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public PageModel() {
    }
}
