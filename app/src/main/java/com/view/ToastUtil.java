package com.view;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/7.
 */
public class ToastUtil {
    public static Toast toast;


    public static void showToast(Context context, String text) {
        if (toast != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                toast.cancel();
            }
        } else {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    public static void cancle(){
        if(null != toast){
            toast.cancel();
        }
    }
}
