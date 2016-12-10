package com.svc.viewinsertplugin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.svc.insertannotation.BindView;
import com.svc.insertannotation.BindViews;
import com.svc.insertannotation.OnClick;

import java.util.List;

public class MainActivity extends BaseActivity {
    @BindView(R.id.linear)
    private LinearLayout linear;
    @BindView(R.id.head)
    private TextView head;
    @BindView(R.id.button)
    private Button button;
    @BindView(R.id.middle)
    private TextView middle;
    @BindView(R.id.image)
    private ImageView image;

    @BindViews({R.id.linear, R.id.head, R.id.button, R.id.image})
    private List<View> viewList;

    @BindView(R.id.view_list)
    private TextView view_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "FindView Success", Toast.LENGTH_SHORT).show();
            }
        });

        for (View view : viewList) {
            view_list.append(view.getClass().toString() + "\n");
        }
    }

    @OnClick({R.id.head})
    public void next(View view) {
        Toast.makeText(MainActivity.this, "Method Single Bind", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.middle, R.id.image})
    public void last() {
        Toast.makeText(MainActivity.this, "Method Multiple Bind", Toast.LENGTH_SHORT).show();
    }
}
