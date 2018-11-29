package zeaze.com.note.weather.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class HeWeather6 {


    @JSONField(name = "basic")
    private Basic basic;
    @JSONField(name = "now")
    private Now now;
    @JSONField(name = "status")
    private String status;
    @JSONField(name = "update")
    private Update update;

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
}
