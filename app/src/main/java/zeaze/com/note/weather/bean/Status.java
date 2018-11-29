package zeaze.com.note.weather.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Status {
    @JSONField(name = "status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
