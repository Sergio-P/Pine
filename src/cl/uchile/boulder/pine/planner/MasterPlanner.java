package cl.uchile.boulder.pine.planner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cl.uchile.boulder.pine.planner.Planner;

import java.util.Calendar;

public class MasterPlanner {

    private final int curYear, curWeek, curDow;
    private SQLiteDatabase db;
    private Context context;

    public MasterPlanner(Context ctx, SQLiteDatabase sdb){
        db = sdb;
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        curDow = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        curYear = calendar.get(Calendar.YEAR);
        curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        context = ctx;
    };

    public void findPlan(String name, String descr, float dur, int wk, int yr, int dwTo){
        if(wk==curWeek){
            Log.d("PINE","Plan for single week");
            Planner planner = new Planner(context,curYear,curWeek,curDow);
            fillPlanner(planner,curWeek,curYear);
            planner.planEvent(name,descr,dur,dwTo,db);
        }
        else{
            float wdur = dur/(wk-curWeek+1);
            Log.d("PINE","Plan for multi week using wdur="+wdur);
            for(int w = curWeek; w<=wk; w++){
                Planner planner = new Planner(context,curYear,w,(w==curWeek)?curDow:0);
                fillPlanner(planner,w,curYear);
                planner.planEvent(name,descr,wdur,(w==wk)?dwTo:6,db);
            }
        }
    }

    private void fillPlanner(Planner planner, int wk, int yr) {
        Cursor cursor = db.rawQuery("select day, minstart, duration from fix_events",null);
        if(cursor.moveToFirst()){
            do{
                int day = cursor.getInt(0);
                int minstart = cursor.getInt(1);
                int dur = cursor.getInt(2);
                planner.addBlock(minstart,minstart+dur,day);
            }
            while(cursor.moveToNext());
        }
        cursor.close();

        cursor = db.rawQuery("select fecha, minstart, duration from unique_event",null);
        if(cursor.moveToFirst()){
            do{
                int fecha = cursor.getInt(0);
                int minstart = cursor.getInt(1);
                int dur = cursor.getInt(2);
                if(fecha/1000 == curYear && (fecha%1000)/10 == curWeek) {
                    int day = fecha%10;
                    planner.addBlock(minstart,minstart+dur,day);
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
    }

}
