package learnmore.yzx.com.learnmore;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Created by laino on 2017/6/15.
 */
@Implements(Person.class)
public class ShadowPerson {
    @Implementation
    public String getName() {
        return "yu zai xin";
    }
}
