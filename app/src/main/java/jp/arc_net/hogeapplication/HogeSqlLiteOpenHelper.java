package jp.arc_net.hogeapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASK on 2016/08/02.
 */
public class HogeSqlLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB ="hoge_sqllite.db";
    private static final int DB_VERSION =3;
    private static final String CREATE_TABLE = "create table hoge_message(id integer primary key autoincrement, message text not null,insert_tm integer not null, latitude real, longitude real, altitude real);";
    private static final String DROP_TABLE = "drop table hoge_message;";


    public HogeSqlLiteOpenHelper(Context context) {
        super(context, DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
