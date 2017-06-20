package learnmore.yzx.com.learnmore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;

import static org.junit.Assert.*;

/**
 * Created by laino on 2017/6/20.
 */
@RunWith(CustomShadowTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowPerson.class})
public class ShadowPersonTest {
    /**
     * 测试自定义的Shadow
     */
    @Test
    public void testCustomShadow() {
        Person person = new Person("yu zai xin");
        //getName()实际上调用的是ShadowPerson的方法
        assertEquals("yu zai xin", person.getName());

        //获取Person对象对应的Shadow对象
        ShadowPerson shadowPerson = (ShadowPerson) ShadowExtractor.extract(person);
        assertEquals("yu zai xin", shadowPerson.getName());
    }
}