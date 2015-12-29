package com.oilfield.logix.crawler;

/**
 *
 *
 * @author Jordan Sanderson
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
