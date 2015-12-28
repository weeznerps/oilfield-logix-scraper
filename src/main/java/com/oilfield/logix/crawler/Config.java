package com.oilfield.logix.crawler;

/**
 * Created by js028708 on 12/28/15.
 */
public class Config {

    private String beginDate,endDate,district;

    public Config(String beginDate, String endDate, String district) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.district = district;
    }


    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDistrict() {
        return district;
    }
}
