package com.ly.imart.view.Fourth;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ly.imart.R;
import com.qmuiteam.qmui.widget.QMUITopBar;

public class FourthHelpActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourth_help);
        QMUITopBar qmuiTopBar = findViewById(R.id.four_help_topbar);
        qmuiTopBar.setTitle("帮 助");
        qmuiTopBar.setBackgroundColor(Color.parseColor("#7dc5eb"));
        textView = findViewById(R.id.four_help_text);
        textView.setTextSize(20);
        textView.setText("1.A问题:\n解决A问题的方案\n\n2.B问题:\n解决B问题的方案\n\n3.C问题:\n解决C问题的方案");

    }
}
