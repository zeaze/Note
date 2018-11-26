package zeaze.com.note;

import android.content.Context;
import android.widget.Toast;

import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication {
    static Context context;
    static public void toast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
}
