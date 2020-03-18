package com.ly.imart.view.Others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.imart.R;

public class AddArticleTitle extends AppCompatActivity {
    EditText editText;
    RadioGroup radioGroup;
    TextView nextButton;

    int kind = 0;
    String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_article_title);
        initView();
    }

    void initView() {
        editText = findViewById(R.id.add_article_title_addtitle);
        radioGroup = findViewById(R.id.add_article_title_radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGrouplisten);
        nextButton = findViewById(R.id.add_article_title_goto);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = editText.getText().toString();
                if (kind == 0||title==null) {
                    Toast.makeText(AddArticleTitle.this, "请填写标题并选择类型", Toast.LENGTH_SHORT).show();
                } else {
                    AddArticleImg.openAddArticleImg(AddArticleTitle.this, title, kind);
                }
            }
        });
    }

    private RadioGroup.OnCheckedChangeListener radioGrouplisten = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            int id = group.getCheckedRadioButtonId();
            switch (group.getCheckedRadioButtonId()) {
                case R.id.add_article_title_kind1:
                    kind = 1;
                    break;
                case R.id.add_article_title_kind2:
                    kind = 2;
                    break;
                case R.id.add_article_title_kind3:
                    kind = 3;
                    break;
                case R.id.add_article_title_kind4:
                    kind = 4;
                    break;
                default:
                    kind=0;
                    break;
            }
        }
    };



}
