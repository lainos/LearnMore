package learnmore.yzx.com.learnmore.annotation;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by laino on 2017/6/20.
 */

public class ToastTip {
    private static Toast mToast;

    public static void show(Context context, String message) {
        int duration = Toast.LENGTH_SHORT;
        if (message.length() > 10) {
            duration = Toast.LENGTH_LONG; //如果字符串比较长，那么显示的时间也长一些。
        }
        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }
}
