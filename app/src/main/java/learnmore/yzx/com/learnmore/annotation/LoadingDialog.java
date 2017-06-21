package learnmore.yzx.com.learnmore.annotation;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by laino on 2017/6/20.
 */

public class LoadingDialog {
    private Context mContext;
    private MaterialDialog dialog;

    public LoadingDialog(Context context) {
        this.mContext = context;
    }

    public MaterialDialog getDialog() {
        return dialog;
    }

    public void show() {
        show("正在加载，请稍后...");
    }

    public void show(String msg) {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(mContext)
                    .content(msg)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .build();
            dialog.setCanceledOnTouchOutside(true);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void showCancelDialog() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(mContext)
                    .content("正在加载，请稍后...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .build();
            dialog.setCanceledOnTouchOutside(false);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }
}
