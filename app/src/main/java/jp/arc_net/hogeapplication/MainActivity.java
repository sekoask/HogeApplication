package jp.arc_net.hogeapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    /**
     * DB
     */
    private SQLiteDatabase hogeDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextBtn = (Button) findViewById(R.id.button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                EditText editText = (EditText)findViewById(R.id.editText);
                if(editText.getText().length() != 0){
                    intent.putExtra("INPUT_TEXT",editText.getText().toString());
                    ContentValues values = new ContentValues();
                    values.put("message", editText.getText().toString());
                    values.put("insert_tm", System.currentTimeMillis());
                    hogeDb.insert("hoge_message","null",values);
                }else{
                    intent.putExtra("INPUT_TEXT","書き込みなし");
                }
                startActivity(intent);
             }
        });

        Button viewBtn = (Button) findViewById(R.id.viewButton);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("INPUT_TEXT","表示のみ");
                startActivity(intent);
            }
        });

        HogeSqlLiteOpenHelper hogeDbHelper = new HogeSqlLiteOpenHelper(getApplicationContext());
        hogeDb = hogeDbHelper.getWritableDatabase();
    }
}
