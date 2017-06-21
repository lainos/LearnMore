package learnmore.yzx.com.learnmore.annotation;

/**
 * Created by laino on 2017/6/20.
 */

public class RequestNetworkApi {

    @RequestAnnotation(withDialog = true, withMessage = "正在加载，请稍后...")
    public void apiTestFunc(String param1, String param2) {
        try {
            // 模拟网络请求的耗时操作
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
