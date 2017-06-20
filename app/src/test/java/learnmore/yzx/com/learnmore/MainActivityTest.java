package learnmore.yzx.com.learnmore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowBitmap;

import static org.junit.Assert.*;

/**
 * Created by laino on 2017/6/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowPerson.class})
public class MainActivityTest {

    @Test
    public void given_empty_when_empty_return_empty() throws Exception {
        // 使用框架提供的Shadow对象
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);

        //通过Shadows.shadowOf()可以获取很多Android对象的Shadow对象
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        ShadowApplication shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);

        Bitmap bitmap = BitmapFactory.decodeFile("Path");
        ShadowBitmap shadowBitmap = Shadows.shadowOf(bitmap);

        //Shadow对象提供方便我们用于模拟业务场景进行测试的api
        assertNull(shadowActivity.getNextStartedActivity());
        assertNull(shadowApplication.getNextStartedActivity());
        assertNotNull(shadowBitmap);

    }
}