package com.svc.insertannotation;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Ino on 2016/11/24.
 */
public class InsertUtil {
    public static void bind(Activity activity) {
        View view = activity.getWindow().getDecorView();
        bind(activity, view);
    }

    public static void bind(Fragment fragment) {
        View view = fragment.getView();
        bind(fragment, view);
    }

    public static void bind(Object object, View view) {
        try {
            Method insertView = object.getClass().getMethod("insertView", View.class);
            if (!insertView.isAccessible()) {
                insertView.setAccessible(true);
            }
            insertView.invoke(object, view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}