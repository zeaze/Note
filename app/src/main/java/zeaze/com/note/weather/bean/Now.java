package zeaze.com.note.weather.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Now {
    @JSONField(name = "fl")
    private String fl;
    @JSONField(name = "tmp")
    private String tmp;
    @JSONField(name = "cond_code")
    private String cond_code;
    @JSONField(name = "cond_txt")
    private String cond_txt;
    @JSONField(name = "wind_deg")
    private String wind_deg;
    @JSONField(name = "wind_dir")
    private String wind_dir;
    @JSONField(name = "wind_sc")
    private String wind_sc;
    @JSONField(name = "wind_spd")
    private String wind_spd;
    @JSONField(name = "hum")
    private String hum;
    @JSONField(name = "pcpn")
    private String pcpn;
    @JSONField(name = "pres")
    private String pres;
    @JSONField(name = "vis")
    private String vis;
    @JSONField(name = "cloud")
    private String cloud;

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getCond_code() {
        return cond_code;
    }

    public void setCond_code(String cond_code) {
        this.cond_code = cond_code;
    }

    public String getCond_txt() {
        return cond_txt;
    }

    public void setCond_txt(String cond_txt) {
        this.cond_txt = cond_txt;
    }

    public String getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(String wind_deg) {
        this.wind_deg = wind_deg;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public void setWind_sc(String wind_sc) {
        this.wind_sc = wind_sc;
    }

    public String getWind_spd() {
        return wind_spd;
    }

    public void setWind_spd(String wind_spd) {
        this.wind_spd = wind_spd;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
}
