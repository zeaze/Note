package zeaze.com.note.weather.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Update {
    @JSONField(name = "loc")
    private String loc;
    @JSONField(name = "utc")
    private String utc;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }
}
