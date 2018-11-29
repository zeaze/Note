package zeaze.com.note.weather.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Basic {

    @JSONField(name = "location")
    private String location;
    @JSONField(name = "cid")
    private String cid;
    @JSONField(name = "lat")
    private String lat;
    @JSONField(name = "lon")
    private String lon;
    @JSONField(name = "parent_city")
    private String parent_city;
    @JSONField(name = "admin_area")
    private String admin_area;
    @JSONField(name = "cnty")
    private String cnty;
    @JSONField(name = "tz")
    private String tz;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getParent_city() {
        return parent_city;
    }

    public void setParent_city(String parent_city) {
        this.parent_city = parent_city;
    }

    public String getAdmin_area() {
        return admin_area;
    }

    public void setAdmin_area(String admin_area) {
        this.admin_area = admin_area;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
}
