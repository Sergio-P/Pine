package cl.uchile.boulder.pine.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventosDB extends SQLiteOpenHelper {

    private final String createTableFixEvent = "create table fix_events("
            + "id integer primary key autoincrement,"
            + "nom text not null,"
            + "descr text not null,"
            + "day integer not null,"
            + "minstart integer not null,"
            + "duration integer not null"
            + ")";

    private final String createTableUniqueEvent = "create table unique_event("
            + "id integer primary key autoincrement,"
            + "nom text not null,"
            + "descr text not null,"
            + "fecha integer not null,"
            + "minstart integer not null,"
            + "duration integer not null,"
            + "autogen integer default 0"
            + ")";

    public EventosDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableFixEvent);
        db.execSQL(createTableUniqueEvent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2) {
            db.execSQL("drop table if exists fix_events");
            db.execSQL(createTableFixEvent);
        }
        db.execSQL("drop table if exists unique_event");
        db.execSQL(createTableUniqueEvent);

    }
}
