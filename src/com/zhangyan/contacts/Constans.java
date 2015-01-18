package com.zhangyan.contacts;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by ku on 2015/1/7.
 */
public class Constans {
    public static final int IMPORT = 0;
    public static final int EXPORT = 1;
    public static final int PROGRESS_MAX = 2;
    public static final int PROGRESS_DISMISS = 3;
    public static void showToast(Context context,String neirong){
        Toast toast = Toast.makeText(context.getApplicationContext(),
                neirong, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void sendMessage(int what, Handler handler){
        sendMessage(what, handler, 0);
    }
    public static void sendMessage(int what, Handler handler, int max){
        Message message = new Message();
        message.what = what;
        if(max != 0) {
            Bundle data = new Bundle();
            data.putInt("max", max);
            message.setData(data);
        }
        handler.sendMessage(message);
    }
}
