package com.zhangyan.contacts;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by ku on 2015/1/7.
 */
public class Constans {
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
}
