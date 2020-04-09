package com.jihoon.myplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PopupActivity extends Activity {

    EditText title;
    EditText todo;
    String TAG = "jihoonDebugging";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        //UI 객체생성
        title = (EditText) findViewById(R.id.title);
        todo = (EditText)findViewById(R.id.todo);

        //데이터 가져오기
        Intent intent = getIntent();
        String _title = intent.getStringExtra("TITLE");
        String _todo = intent.getStringExtra("TODO");
        title.setText(_title);
        todo.setText(_todo);

        Button buttonOK = (Button)findViewById(R.id.ok);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String tmpTitle = title.getText().toString();
                    String tmpTodo = todo.getText().toString();

                    Intent intent = new Intent();
                    intent.putExtra("TITLE", tmpTitle);
                    intent.putExtra("TODO", tmpTodo);
                    setResult(RESULT_OK, intent);
                    //액티비티(팝업) 닫기
                    finish();
                }
                catch (Exception e)
                {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
        Button buttonX = (Button)findViewById(R.id.cancel);
        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}


