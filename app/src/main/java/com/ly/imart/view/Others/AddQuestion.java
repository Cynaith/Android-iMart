package com.ly.imart.view.Others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.imart.R;
import com.ly.imart.model.Others.QuestionModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddQuestion extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.add_question_title)
    EditText editText_title;
    @BindView(R.id.add_question_text)
    EditText editText_message;
    @BindView(R.id.add_question_send)
    TextView button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.add_question_send){
            try{
                String title = editText_title.getText().toString();
                String message = editText_message.getText().toString();
                if (title==null||message==null){
                    Toast.makeText(this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                }
                else {
                    QuestionModel questionModel = new QuestionModel();
                    questionModel.question(message,title);
                    finish();
                }
            }catch (Exception e){
                Toast.makeText(this,"请填写完整信息",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
