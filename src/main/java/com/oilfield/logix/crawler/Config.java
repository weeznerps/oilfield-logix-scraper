package com.oilfield.logix.crawler;

/**
 * Run configuration
 *
 * @author Jordan Sanderson
 */
public class Config {

    private String beginDate,endDate;

    public Config(String beginDate, String endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }


    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

}
