package learnmore.yzx.com.learnmore.simpleannotation;

import android.util.Log;


/**
 * Created by 于在昕 on 2017/6/21.
 */
@TestA(name = "type", gid = Long.class)
public class UserAnnotation {

    @TestA(name = "param", id = 1, gid = Long.class)
    private Integer age;

    @TestA(name = "construct", id = 2, gid = Long.class)
    public UserAnnotation() {
    }

    @TestA(name = "public method a", id = 3, gid = Long.class)
    public void a() {
        Log.i("weloop", " a method run");
    }

    @TestA(name = "protected method b ", id = 4, gid = Long.class)
    protected void b() {
        Log.i("weloop", " b method run");
    }

    @TestA(name = "private method c", id = 5, gid = Long.class)
    private void c() {
        Log.i("weloop", " b method run");
    }
}
