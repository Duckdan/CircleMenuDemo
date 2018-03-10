package study.com.circlemenudemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import study.com.circlemenudemo.view.CircleMenuView;

public class MainActivity extends AppCompatActivity {

    private String[] texts = new String[]{"安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡"};
    private int[] imgs = new int[]{R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
            R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
            R.drawable.home_mbank_6_normal};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleMenuView cmv = (CircleMenuView) findViewById(R.id.cmv);
        cmv.setData(imgs, texts);
    }
}
