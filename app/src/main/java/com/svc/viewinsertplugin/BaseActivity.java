package com.svc.viewinsertplugin;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.svc.insertannotation.InsertUtil;

/**
 * Created by Ino on 2016/11/23.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initEvent();
    }

    protected void initView() {
        InsertUtil.bind(this);
    }

    protected void initEvent() {

    }
}