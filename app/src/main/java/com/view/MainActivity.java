package com.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private GesturePwdView mPwView;
    private TextView mTv_show;
    private ArrayList<Integer> mPositionList;
    private Button mBt_goOn;
    private Button mBt_reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPwView = (GesturePwdView) findViewById(R.id.pwview);
        mPwView.setLineColor(Color.BLACK);
        mPwView.setLineStrokeWidth(7f);
        mPwView.setPointColor(Color.RED);
        mPwView.setPointRadius(15);

        mTv_show = (TextView) findViewById(R.id.text);

        mBt_reset = (Button) findViewById(R.id.bt_reset);
        mBt_goOn = (Button) findViewById(R.id.bt_continue);

        mPwView.setCallBack(new GesturePwdView.CallBack() {
            @Override
            public void onComplete1(ArrayList<Integer> postionList) {
                mPositionList = postionList;
            }

            @Override
            public void onComplete2(ArrayList<Integer> postionList) {
                if (bianli(postionList)) {
                    //TODO
                    mTv_show.setText("设置成功");
                    ToastUtil.showToast(MainActivity.this, "设置成功");
                } else {
                    mTv_show.setText("两次绘制不一致，请重新绘制");
                    mPwView.setError();
                }
            }
        });
        mBt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFirst = mPwView.reSet();
                mTv_show.setText(isFirst ? "绘制解图案，请至少链接四个点" : "请再次设置");
            }
        });
        mBt_goOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mBt_goOn.getText().toString()) {
                    case "继续":
                        mPwView.goOn();
                        mTv_show.setText("设置第二次");
                        mBt_goOn.setText("完成");
                        break;
                    case "完成":
                        //TODO
                        ToastUtil.showToast(MainActivity.this, "关闭界面");
                        break;
                }
            }
        });
    }

    private boolean bianli(ArrayList<Integer> list) {
        if (mPositionList.size() != list.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != mPositionList.get(i)) {
                return false;
            }
        }
        return true;
    }
}
