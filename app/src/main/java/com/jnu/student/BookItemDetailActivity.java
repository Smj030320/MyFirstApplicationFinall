package com.jnu.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BookItemDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item_details);
        Intent intent = getIntent();
        if(intent != null){
            String name = intent.getStringExtra("name");

            if(null != name ){
                EditText editTextItemName= findViewById(R.id.Edit_Text_Item_Name);
                editTextItemName.setText(name);
            }
        }
        Button buttonOk= findViewById(R.id.button_item_details_ok);

        buttonOk.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            EditText editTextItemName= findViewById(R.id.Edit_Text_Item_Name);

            intent1.putExtra( "name" , editTextItemName.getText().toString());
            setResult(Activity.RESULT_OK, intent1) ;

            BookItemDetailActivity.this.finish();
        });
    }
}