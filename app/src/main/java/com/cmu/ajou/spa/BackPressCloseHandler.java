package com.cmu.ajou.spa;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Minseong on 2016-07-25.
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "If you push the \'Back\' Button, this app going to be finished.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
