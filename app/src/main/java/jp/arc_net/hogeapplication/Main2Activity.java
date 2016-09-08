package jp.arc_net.hogeapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {

    private static final int MAX_VIEW_ROWS = 50;

    /**
     * DB
     */
    private SQLiteDatabase hogeDb;

    private ListView listView;
    private ArrayAdapter<String> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        TextView textView = (TextView) findViewById(R.id.sendTextView);
        textView.setText(intent.getStringExtra("INPUT_TEXT"));

        Button backBtn = (Button) findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        HogeSqlLiteOpenHelper hogeDbHelper = new HogeSqlLiteOpenHelper(getApplicationContext());
        hogeDb = hogeDbHelper.getReadableDatabase();
        Cursor cursor = hogeDb.rawQuery("SELECT id,message,insert_tm, latitude, longitude, altitude FROM hoge_message ORDER BY insert_tm DESC;", null);

        listView = (ListView) findViewById(R.id.listView);
        listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        cursor.moveToFirst();
        int maxLength = cursor.getCount();
        if (maxLength > MAX_VIEW_ROWS) {
            maxLength = MAX_VIEW_ROWS;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (int i = 0; i < maxLength; i++) {
            StringBuilder rows = new StringBuilder();
            rows.append(cursor.getInt(0));
            rows.append(":");
            rows.append(cursor.getString(1));
            rows.append(":");
            rows.append(dateFormat.format(new Date(cursor.getLong(2))));
            rows.append(":緯度");
            rows.append(cursor.getDouble(3));
            rows.append(":経度");
            rows.append(cursor.getDouble(4));
            rows.append(":高度");
            rows.append(cursor.getDouble(5));
            listViewAdapter.add(rows.toString());
            cursor.moveToNext();
        }
        listView.setAdapter(listViewAdapter);


    }
}
