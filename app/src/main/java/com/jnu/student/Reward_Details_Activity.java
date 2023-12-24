package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Reward_Details_Activity extends AppCompatActivity {
    private String selectedText;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_details);
        EditText editTitle = findViewById(R.id.Edit_Title);
        EditText editPoint = findViewById(R.id.Edit_Point);

        EditText editTag   = findViewById(R.id.Edit_Tag);
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra("title") != null) {
                TextView textView = findViewById(R.id.Title_Text);
                textView.setText("修改任务");
            }
            editTitle.setText(intent.getStringExtra("title"));
            editPoint.setText(intent.getStringExtra("points"));
            editTag.setText(intent.getStringExtra("tag"));
            position = intent.getIntExtra("position",-1);
        }
        // 对返回按钮的设置
        ImageView goBackImage = findViewById(R.id.Arrow_Back_Button);
        goBackImage.setOnClickListener(v -> {
            // 点击返回时，直接返回
            Reward_Details_Activity.this.finish();
        });
        // 设置下拉框的选择响应函数
        Spinner editType  = findViewById(R.id.Spinner_Reward_Type);
        editType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedText = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedText = "单次";
            }
        });

        // 对确定按钮的处理
        Button buttonOk = findViewById(R.id.Submit);
        buttonOk.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            setResult(Activity.RESULT_CANCELED, intent1);
            if(editTitle.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"请输入主题", Toast.LENGTH_SHORT).show();
                return;
            }
            if(editPoint.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"请输入耗费积分数", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Integer.parseInt(editPoint.getText().toString()) > 999.99){
                Toast.makeText(getApplicationContext(),"成就点数太高啦（最大为999.99）", Toast.LENGTH_SHORT).show();
                return;
            }

            intent1.putExtra("title",editTitle.getText().toString());
            intent1.putExtra("points",editPoint.getText().toString());
            intent1.putExtra("type",selectedText);

            if(editTag.getText().toString().equals("")){
                intent1.putExtra("tag","全部");
            }
            else{
                intent1.putExtra("tag",editTag.getText().toString());
            }
            intent1.putExtra("position",position);
            setResult(Activity.RESULT_OK, intent1);
            Reward_Details_Activity.this.finish();
        });
    }
}
