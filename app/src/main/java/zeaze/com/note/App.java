package zeaze.com.note;

import android.content.Context;
import android.widget.Toast;

import org.litepal.LitePalApplication;

public class App extends LitePalApplication {

    static Context context;
    static public void toast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    private static Context AppContext;
    public static Context getContext() {
        return AppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
}
