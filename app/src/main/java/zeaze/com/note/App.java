package zeaze.com.note;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.OkGo;

import org.litepal.LitePalApplication;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import zeaze.com.note.weather.WeatherService;
import zeaze.com.note.weather.bean.HeWeather6;

public class App extends LitePalApplication {

    static private Context context;
    static private String TAG="zeaze2";
    static private Location location = null;
    static private HeWeather6 heWeather6=null;

    public static Location getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        };
        if (location != null) {
            return location;
        }
        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        for (String s:providerList){
            Log.d(TAG, "getLocation: "+s);
            if (locationManager.getLastKnownLocation(s)!=null){
                location=locationManager.getLastKnownLocation(s);
                return location;
            }
        }
        Log.d(TAG, "getLocation: "+"null");
        return location;
    }



    public static HeWeather6 getHeWeather6() {
        return heWeather6;
    }

    public static void setHeWeather6(HeWeather6 heWeather6) {
        App.heWeather6 = heWeather6;
    }

    @Override
    public void onCreate() {                                  //做一些初始化，bmob和OKgo，还有天气服务，用计时器重复请求天气
        super.onCreate();
        context=this;
        OkGo.getInstance().init(this);
        Bmob.initialize(this, "a193e6e7dccc3d36679d1136060599ed");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(context,WeatherService.class);
                startService(intent);
            }
        };
        timer.schedule(task,0,5000);
    }

    @Override
    public void onTerminate() {
        Intent intent=new Intent(this,WeatherService.class);
        stopService(intent);
        super.onTerminate();
    }

    static public void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static Context getContext() {
        return context;
    }

}
