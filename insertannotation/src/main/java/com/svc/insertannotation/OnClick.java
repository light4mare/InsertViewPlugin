package com.svc.insertannotation;

import android.support.annotation.IdRes;
import android.view.View;


public @interface OnClick {
    /**
     * View IDs to which the method will be bound.
     */
    @IdRes int[] value() default {View.NO_ID};
}