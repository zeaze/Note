package zeaze.com.note.base.pullExtendLayoutForRecyclerView;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import zeaze.com.note.App;
import zeaze.com.note.R;
import zeaze.com.note.weather.bean.HeWeather6;


/**
 * 这个类封装了下拉刷新的布局
 */
public class ExtendListHeader extends ExtendLayout {


    float containerHeight = UIHelper.dip2px(60);
    float listHeight = UIHelper.dip2px(120);
    boolean arrivedListHeight = false;
   // private RecyclerView mRecyclerView;

    /**
     * 原点
     */

    private ExpendPoint mExpendPoint;

    /**
     * 构造方法
     *
     * @param context context
     */
    public ExtendListHeader(Context context) {
        super(context);

    }


    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public ExtendListHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void bindView(View container) {
    //    mRecyclerView = findViewById(R.id.list);
        mExpendPoint = findViewById(R.id.expend_point);
    }
/*
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }*/

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.extend_header, null);
    }


    @Override
    public int getContentSize() {
        return (int) (containerHeight);
    }

    @Override
    public int getListSize() {
        return (int) (listHeight);
    }


    @Override
    protected void onReset() {
        mExpendPoint.setVisibility(VISIBLE);
        mExpendPoint.setAlpha(1);
        mExpendPoint.setTranslationY(0);
       // mRecyclerView.setTranslationY(0);
        arrivedListHeight = false;
    }

    @Override
    protected void onReleaseToRefresh() {
    }

    @Override
    protected void onPullToRefresh() {

    }

    @Override
    protected void onArrivedListHeight() {
        arrivedListHeight = true;
    }

    @Override
    protected void onRefreshing() {
    }


    ImageView icon;
    TextView weather,date,tmp,city;

    public void init(){
        icon=findViewById(R.id.icon);
        weather=findViewById(R.id.weather);
        date=findViewById(R.id.date);
        tmp=findViewById(R.id.tmp);
        city=findViewById(R.id.city);
    }

    HeWeather6 heWeather6=null;
    String TAG="zeaze2";

    @Override
    public void onPull(int offset) {
        if (heWeather6!=App.getHeWeather6()){
            heWeather6=App.getHeWeather6();
            if (heWeather6.getStatus().equals("ok")) {
                icon.setImageResource(R.drawable.icon);
                weather.setText(heWeather6.getNow().getCond_txt());
                date.setText(heWeather6.getUpdate().getLoc());
                tmp.setText(heWeather6.getNow().getTmp() + "℃");
                city.setText(heWeather6.getBasic().getLocation());
                Log.d(TAG, "onPull: " + "天气刷新");
            }
            else {
                Log.d(TAG, "onPull: "+heWeather6.getStatus());
                weather.setText(heWeather6.getStatus());
            }
        }
        if (!arrivedListHeight) {
            mExpendPoint.setVisibility(VISIBLE);
            float percent = Math.abs(offset) / containerHeight;
            int moreOffset = Math.abs(offset) - (int) containerHeight;
            if (percent <= 1.0f) {
                mExpendPoint.setPercent(percent);
                mExpendPoint.setTranslationY(-Math.abs(offset) / 2 + mExpendPoint.getHeight() / 2);
              //  mRecyclerView.setTranslationY(-containerHeight);
            } else {
                float subPercent = (moreOffset) / (listHeight - containerHeight);
                subPercent = Math.min(1.0f, subPercent);
                mExpendPoint.setTranslationY(-(int) containerHeight / 2 + mExpendPoint.getHeight() / 2 + (int) containerHeight * subPercent / 2);
                mExpendPoint.setPercent(1.0f);
                float alpha = (1 - subPercent * 2);
                mExpendPoint.setAlpha(Math.max(alpha, 0));
               // mRecyclerView.setTranslationY(-(1 - subPercent) * containerHeight);
            }
        }
        if (Math.abs(offset) >= listHeight) {
            mExpendPoint.setVisibility(INVISIBLE);
            //mRecyclerView.setTranslationY(-(Math.abs(offset) - listHeight) / 2);
        }
    }


}
