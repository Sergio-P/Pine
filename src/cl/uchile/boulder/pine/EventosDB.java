package cl.uchile.boulder.pine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventosDB extends SQLiteOpenHelper {

    private final String createTableFixEvent = "create table fix_events("
            + "id integer primary key autoincrement,"
            + "descr text not null,"
            + "day integer not null,"
            + "minstart integer not null,"
            + "duration integer not null"
            + ")";

    private final String createTableDynEvent = "create table dyn_event("
            + "id integer primary key autoincrement,"
            + "descr text not null,"
            + "day integer not null,"
            + "minstart integer not null,"
            + "duration integer not null"
            + ")";

    public EventosDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableFixEvent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists fix_events");
        db.execSQL(createTableFixEvent);
    }
}
