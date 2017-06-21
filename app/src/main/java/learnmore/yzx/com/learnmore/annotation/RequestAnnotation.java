package learnmore.yzx.com.learnmore.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建类的时候，选择Annotation创建
 * Created by laino on 2017/6/20.
 */
@Target(ElementType.METHOD)// 修饰的对象范围为方法
@Retention(RetentionPolicy.RUNTIME)// 在运行时有效
@Inherited// 标记注解，@Inherited阐述了某个被标注的类型是被继承的
@Documented // 是一个标记注解，用于描述其它类型的注解应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化
public @interface RequestAnnotation {
    boolean withDialog() default true;

    String withMessage() default "正在加载中";
}
