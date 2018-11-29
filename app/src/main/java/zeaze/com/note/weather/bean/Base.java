package zeaze.com.note.weather.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Base {
    @JSONField(name = "HeWeather6")
    private List<String> HeWeather6;

    public List<String> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<String> heWeather6) {
        HeWeather6 = heWeather6;
    }
}
