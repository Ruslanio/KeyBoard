package com.example.ruslanio.keyboard;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruslanio.keyboard.database.helper.DBHelper;


public class MainActivity extends AppCompatActivity{

    private TextView mResult;

    private Button mGetBb;

    private DBHelper mDBHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new DBHelper(getApplicationContext());

        mGetBb = (Button) findViewById(R.id.btn_get_db);
        mResult = (TextView) findViewById(R.id.result);

        mGetBb.setOnClickListener(btn -> {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.TEXT_ENTITY_TABLE_NAME, null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                int id = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_ID);
                int text = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT);
                int status = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_STATUS);

                StringBuilder builder = new StringBuilder();

                do {
                    builder.append("ID = ").append(cursor.getInt(id)).append("\n")
                            .append("TEXT = ").append(cursor.getString(text)).append("\n")
                            .append("STATUS = ").append(cursor.getInt(status)).append("\n");
                } while (cursor.moveToNext());

                mResult.setText(builder.toString());
            } else {
                Toast.makeText(this,"no data",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.close();
    }


}
