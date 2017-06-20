package learnmore.yzx.com.rxandroidbledemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.polidea.rxandroidble.RxBleClient;

/**
 * Created by laino on 2017/6/18.
 */

public class RxAndroidBleApplication extends Application {
    private RxBleClient rxBleClient;

    public static RxBleClient getRxBleClient(Context context) {
        RxAndroidBleApplication application = (RxAndroidBleApplication) context.getApplicationContext();
        return application.rxBleClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rxBleClient = RxBleClient.create(this);
        RxBleClient.setLogLevel(Log.INFO);
    }
}
