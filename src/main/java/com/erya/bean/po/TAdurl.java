package com.erya.bean.po;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2019-07-30
 */
public class TAdurl {
    private Integer id;

    private String adurl;

    private String adcontent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdurl() {
        return adurl;
    }

    public void setAdurl(String adurl) {
        this.adurl = adurl == null ? null : adurl.trim();
    }

    public String getAdcontent() {
        return adcontent;
    }

    public void setAdcontent(String adcontent) {
        this.adcontent = adcontent == null ? null : adcontent.trim();
    }
}