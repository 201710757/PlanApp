package com.jihoon.myplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    String TAG = "jihoonDebugging";
    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    DBHelper dbHelper;
    SQLiteDatabase db = null;
    Cursor cursor;
    String tmpYear, tmpMonth, tmpDay;
    String tmpTitle, tmpTodo;
    EditText tvLabel;
    TextView showText;


    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page, String title) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    View view;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        dbHelper = new DBHelper(view.getContext(), 1);
        db = dbHelper.getWritableDatabase();


        showText = (TextView)view.findViewById(R.id.showText);



        Button okButton = (Button)view.findViewById(R.id.buttonok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PopupActivity.class);
                intent.putExtra("TITLE", tmpTitle);
                intent.putExtra("TODO", tmpTodo);
                startActivityForResult(intent, 1);
            }
        });



        Button button = (Button)view.findViewById(R.id.buttonFirst);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackMethod = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tmpYear = Integer.toString(year);
                        tmpMonth = Integer.toString(month+1);
                        tmpDay = Integer.toString(dayOfMonth);

                        String[]res = listUpdateCheckBox(Integer.toString(year), Integer.toString(month+1), Integer.toString(dayOfMonth));
                        tmpTitle = res[0];
                        tmpTodo = res[1];
                        if(res[0] == null || res[1] == null)
                            showText.setText("title : No / todo : No!");
                        showText.setText("title : " + res[0] + " / todo : " + res[1]);
                    }
                };
                String[]dat = returnDate();
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), callbackMethod, Integer.parseInt(dat[0]), Integer.parseInt(dat[1])-1, Integer.parseInt(dat[2]));
                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                Log.d(TAG, "Error 1");
                String title = data.getStringExtra("TITLE");
                String todo = data.getStringExtra("TODO");
                showText.setText("Title : " + title + "\ntodo : " + todo);
                Log.d(TAG, "Error 3 / title : " + title + " / todo : " + todo);

                try {
                    deleteDB(tmpYear, tmpMonth, tmpDay);
                    String sql = String.format("INSERT INTO dateTodo VALUES('" + tmpYear + "','" + tmpMonth + "','" + tmpDay + "','" + title + "','" + todo + "');");
                    db.execSQL(sql);
                }
                catch (Exception e)
                {
                    Log.d(TAG, e.getMessage());
                }
                Log.d(TAG, "Error 2");

            }
        }
    }

    public void deleteDB(String year, String month, String day)
    {
        String sql = String.format("DELETE FROM dateTodo WHERE year = '" + year + "' AND month = '" + month + "' AND day = '" + day + "';");
        db.execSQL(sql);
    }




    public String[] listUpdateCheckBox(String year, String month, String day)
    {
        String[] str = new String[2];
        cursor = db.rawQuery("SELECT title, todo FROM dateTodo WHERE year = '" + year + "' AND month = '" + month + "' AND day = '" + day + "';", null);
        //startManagingCursor(cursor);
        while (cursor.moveToNext())
        {
            for(int i=0;i<2;i++) str[i] = cursor.getString(i);
        }
        return str;
    }

    public String[] returnDate()
    {
        String[] res = new String[3];
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
        res[0] = year;
        res[1] = month;
        res[2] = day;
        return res;
    }

}