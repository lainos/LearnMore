package learnmore.yzx.com.learnmore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;

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

//        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        Log.i("weloop", "deviceId = " + tm.getDeviceId() + "， softWareVersion = " + tm.getDeviceSoftwareVersion());//String );//String
//        String imei = "864454030098044";
//        byte[] value = imei.getBytes();
//        byte[] param = new byte[16];
//        System.arraycopy(value, 0, param, 0, value.length);
//        Log.i("weloop", "param = " + Arrays.toString(param) + ", re connect = " + new String(value));
//
//        HashMap<String, CorosConfigEntity> hashMap = new HashMap<>();
//        CorosConfigEntity corosConfigEntity = new CorosConfigEntity();
//        corosConfigEntity.setBongState(1);
//        hashMap.put("123", corosConfigEntity);
//
//        hashMap.get("123").setBongState(56);
//        Log.i("weloop", "bongState = " + hashMap.get("123").getBongState());

    }

    private void init2Option() {
        Class<Dialog> cls = Dialog.class;

        String allContent = "完整类名：" + cls.getName() + "\n";
        allContent += "所有public类型的属性值 : ";
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
            // mAlert is AlertController,
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
        private static final int msgDismissDialog = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<DialogInterface>(dialogInterface);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DialogInterface.BUTTON_NEGATIVE:

                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;
            }
        }
    }


}
