package learnmore.yzx.com.learnmore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import learnmore.yzx.com.learnmore.fruitannotation.Apple;
import learnmore.yzx.com.learnmore.fruitannotation.FruitColor;
import learnmore.yzx.com.learnmore.fruitannotation.FruitName;
import learnmore.yzx.com.learnmore.simpleannotation.TestA;
import learnmore.yzx.com.learnmore.simpleannotation.UserAnnotation;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init1Option();
        init2Option();

        findViewById(R.id.btnAnnotation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseSimpleClassAnnotation();
                parseMethodAnnotation();
                parseConstructAnnotation();

                parseFieldAnnotationn();

                parseFruitInfo();
            }
        });
    }

    private void parseFruitInfo() {
        Field[] fields = Apple.class.getDeclaredFields();
        for (Field field : fields) {
            boolean hasAnnotation = field.isAnnotationPresent(FruitName.class);
            if (hasAnnotation) {
                FruitName fruitName = field.getAnnotation(FruitName.class);
                Log.i("weloop", "apple name = " + fruitName.value());
            }

            boolean hasColorAnnotation = field.isAnnotationPresent(FruitColor.class);
            if (hasColorAnnotation) {
                FruitColor fruitColor = field.getAnnotation(FruitColor.class);
                Log.i("weloop", "apple color = " + fruitColor.fruitColor());
            }
        }

    }


    public static void parseSimpleClassAnnotation() {
        try {
            Class userAnnotation = Class.forName("learnmore.yzx.com.learnmore.simpleannotation.UserAnnotation");
            Annotation[] annotations = userAnnotation.getAnnotations();
            for (Annotation annotation : annotations) {
                TestA testA = (TestA) annotation;
                Log.i("weloop", "类的 name = " + testA.name() + ", id = " + testA.id() + ", gid = " + testA.gid());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void parseMethodAnnotation() {
        Method[] methods = UserAnnotation.class.getDeclaredMethods();
        for (Method method : methods) {
            boolean hasAnnotation = method.isAnnotationPresent(TestA.class);
            if (hasAnnotation) {
                TestA testA = method.getAnnotation(TestA.class);
                Log.i("weloop", "所有普通方法方法" + method.getName() + ", name = " + testA.name() + ",id = " + testA.id() + ", gid = " + testA.gid());
            }
        }
    }

    public static void parseConstructAnnotation() {
        Constructor[] constructors = UserAnnotation.class.getConstructors();
        for (Constructor constructor : constructors) {
            boolean hasAnnotation = constructor.isAnnotationPresent(TestA.class);
            if (hasAnnotation) {
                TestA annotation = (TestA) constructor.getAnnotation(TestA.class);
                Log.i("weloop", "构造方法" + constructor.getName() + ", name = " + annotation.name() + ",id = " + annotation.id() + ", gid = " + annotation.gid());
            }
        }
    }

    public static void parseFieldAnnotationn() {
        Field[] fields = UserAnnotation.class.getDeclaredFields();
        for (Field field : fields) {
            boolean hasAnnotation = field.isAnnotationPresent(TestA.class);
            if (hasAnnotation) {
                TestA testA = field.getAnnotation(TestA.class);
                Log.i("weloop", "参数" + field.getName() + ", name = " + testA.name() + ",id = " + testA.id() + ", gid = " + testA.gid());
            }
        }
    }


    private void init2Option() {
        Class<Dialog> cls = Dialog.class;

        String allContent = "完整类名：" + cls.getName() + "\n";
        allContent += "所有public类型的属性值 : \n";
        Field[] allPublic = cls.getFields();
        for (Field field : allPublic) {
            allContent += (field.getType().getSimpleName() + " ---> " + field.getName() + ", \n");
        }

        try {
            Field changeField = cls.getClass().getField("mCancelMessage");
            changeField.setAccessible(true);
            changeField.set("what", 1024);
            allContent += (" 处理后的what值 = " + changeField.getInt("what"));
        } catch (Exception e) {
            e.printStackTrace();
            allContent += e.getMessage();
        }

        allContent += "\n\n\n\n";
        allContent += "类的所有属性-------------------->";
        allContent += "\n";
        allPublic = cls.getDeclaredFields();
        for (Field field : allPublic) {
            allContent += (field.getName() + ", \n");
        }

        allContent += "\n\n\n\n";
        allContent += "类的所有方法-------------------->";
        allContent += "\n";

        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            allContent += (Modifier.toString(method.getModifiers()) + "    " + method.getName() + "\n");
        }

        TextView tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.setText(allContent);


    }

    /**
     * 利用java反射技术阻止通过按钮关闭对话框连接
     */
    private void init1Option() {
        Button btnClick = (Button) findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDialogContent();
                alertDialog.show();
            }
        });

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("头")
                .setMessage("消息").setIcon(R.drawable.abc).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
    }

    private void changeDialogContent() {
        try {
            Field field = alertDialog.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);

            Object object = field.get(alertDialog);
            field = object.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);

            field.set(object, new ButtonHandler(alertDialog));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ButtonHandler extends Handler {
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<>(dialogInterface);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                case DialogInterface.BUTTON_NEGATIVE:
                    Toast.makeText(MainActivity.this, "BUTTON_NEGATIVE", Toast.LENGTH_LONG).show();
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(MainActivity.this, "BUTTON_POSITIVE", Toast.LENGTH_LONG).show();

                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;
            }
        }
    }


}
