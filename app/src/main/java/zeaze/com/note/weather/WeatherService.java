package zeaze.com.note.weather;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import zeaze.com.note.App;
import zeaze.com.note.MainActivity;
import zeaze.com.note.R;
import zeaze.com.note.weather.bean.Base;
import zeaze.com.note.weather.bean.Basic;
import zeaze.com.note.weather.bean.HeWeather6;
import zeaze.com.note.weather.bean.Now;
import zeaze.com.note.weather.bean.Update;

public class WeatherService extends Service {
    String TAG="zeaze2";
    String url="https://free-api.heweather.com/s6/weather/now?"+"key=fcecd896ff33472eab517a59d52957a2&";

    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: "+"service");
        if (App.getLocation() != null) {
            Log.d(TAG, "onStartCommand: "+"location");
            String completeUrl=url+"location="+App.getLocation().getLongitude()+","+App.getLocation().getLatitude();
            OkGo.<HeWeather6>post(completeUrl)
                    .tag(this)
                    .cacheMode(CacheMode.NO_CACHE)
                    .execute(new Callback<HeWeather6>() {
                        @Override
                        public void onStart(Request<HeWeather6, ? extends Request> request) {

                        }
                        @Override
                        public void onSuccess(Response<HeWeather6> response) {
                            Log.d(TAG, "onSuccess: ");
                            HeWeather6 heWeather6 = response.body();
                            App.setHeWeather6(heWeather6);
                            if (heWeather6.getStatus().equals("ok")) {
                                Update update = heWeather6.getUpdate();
                                Now now = heWeather6.getNow();
                                Basic basic = heWeather6.getBasic();

                                String CHANNEL_ID = "channel_id_1";
                                String CHANNEL_NAME = "天气服务";
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                                    mNotificationManager.createNotificationChannel(notificationChannel);
                                }
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);

                                RemoteViews remoteViews;
                                remoteViews = new RemoteViews(getPackageName(), R.layout.weather_remoteviews);
                                remoteViews.setImageViewResource(R.id.icon, R.drawable.icon);
                                remoteViews.setTextViewText(R.id.weather, now.getCond_txt());
                                remoteViews.setTextViewText(R.id.date, update.getLoc());
                                remoteViews.setTextViewText(R.id.tmp, now.getTmp() + "℃");
                                remoteViews.setTextViewText(R.id.city, basic.getLocation());

                                Notification notification = builder
                                        .setSmallIcon(R.drawable.icon)
                                        .setCustomContentView(remoteViews)
                                        .setWhen(System.currentTimeMillis())
                                        .build();
                                startForeground(1, notification);


                            }
                        }
                        @Override
                        public void onCacheSuccess(Response<HeWeather6> response) {

                        }
                        @Override
                        public void onError(Response<HeWeather6> response) {

                        }
                        @Override
                        public void onFinish() {

                        }
                        @Override
                        public void uploadProgress(Progress progress) {

                        }
                        @Override
                        public void downloadProgress(Progress progress) {

                        }
                        @Override
                        public HeWeather6 convertResponse(okhttp3.Response response) throws Throwable {
                            Base base = JSON.parseObject(response.body().string(), Base.class);
                            HeWeather6 heWeather6 = JSON.parseObject(base.getHeWeather6().get(0),HeWeather6.class);
                            return heWeather6;
                        }
                    });

        }
        return super.onStartCommand(intent, flags, startId);
    }


}















