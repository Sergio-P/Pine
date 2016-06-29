package cl.uchile.boulder.pine.planner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Planner {

    private int year, week;
    private ArrayList<TimeBlock> timeBlocks;
    private ArrayList<TimeBlock> promises;
    private String evName;
    private Context context;
    private char horario_pref;

    private int actualDay;
    private String evDescr;

    public Planner(Context ctx, int y, int w, int dw){
        timeBlocks = new ArrayList<TimeBlock>();
        year = y;
        week = w;
        actualDay = dw;
        context = ctx;
        horario_pref = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_horario","T").charAt(0);
    }

    public void addBlock(int mi, int mf, int dw){
        timeBlocks.add(new TimeBlock(mi,mf,dw));
    }

    public void startPromise(String evname, String descr){
        promises = new ArrayList<TimeBlock>();
        evName = evname;
        evDescr = descr;
    }

    public void planEvent(String name, String descr, float dur, int dwTo, SQLiteDatabase db){
        startPromise(name, descr);
        boolean sch = scheduleEvent(dur,dwTo);
        if(sch)
            commitPromise(db);
        else
            cancelPromise();
    }

    private void commitPromise(SQLiteDatabase db) {
        for(TimeBlock tb : promises){
            Object[] args = {evName, evDescr, year*1000+week*10+tb.dow, tb.minIni, tb.minFin-tb.minIni,1};
            db.execSQL("insert into unique_event(nom,descr,fecha,minstart,duration,autogen) values(?,?,?,?,?,?)",args);
        }
    }

    private void cancelPromise() {
        Log.d("PINE","Promise Canceled");
        Toast.makeText(context,"Promise canceled",Toast.LENGTH_LONG).show();
        promises = null;
    }


    private boolean scheduleEvent(float dur, int dwTo){
        if(dwTo - actualDay >= dur)
            return scheduleByHour(dur,dwTo);
        else{
            return scheduleByEqual(dur,dwTo);
        }
    }

    private boolean scheduleByEqual(float dur, int dwTo) {
        float hpp = (float) (Math.floor(4f*dur/(dwTo-actualDay)+0.5f)/4f);
        boolean completed = false;
        for(int d = actualDay; d<=dwTo && dur>0; d++){
            completed = false;
            if(horario_pref == 'T') {
                int hact = 15;
                while (!completed && hact < 23.75 - hpp) {
                    if (isFreeRange(d, 60 * hact, (int) (60 * (hact + hpp)))) {
                        promiseEvent(d, 60 * hact, (int) (60 * (hact + hpp)));
                        completed = true;
                    }
                    hact++;
                }
                hact = 12;
                while (!completed && hact > 6) {
                    if (isFreeRange(d, 60 * hact, (int) (60 * (hact + hpp)))) {
                        promiseEvent(d, 60 * hact, (int) (60 * (hact + hpp)));
                        completed = true;
                    }
                    hact--;
                }
            }
            else if(horario_pref == 'M'){
                int hact = 12;
                while (!completed && hact > 6) {
                    if (isFreeRange(d, 60 * hact, (int) (60 * (hact + hpp)))) {
                        promiseEvent(d, 60 * hact, (int) (60 * (hact + hpp)));
                        completed = true;
                    }
                    hact--;
                }
                hact = 15;
                while (!completed && hact < 23.75 - hpp) {
                    if (isFreeRange(d, 60 * hact, (int) (60 * (hact + hpp)))) {
                        promiseEvent(d, 60 * hact, (int) (60 * (hact + hpp)));
                        completed = true;
                    }
                    hact++;
                }
            }
            else{
                int hact = 20;
                while (!completed && hact < 23.75 - hpp) {
                    if (isFreeRange(d, 60 * hact, (int) (60 * (hact + hpp)))) {
                        promiseEvent(d, 60 * hact, (int) (60 * (hact + hpp)));
                        completed = true;
                    }
                    hact++;
                }
                hact = 20;
                while (!completed && hact > 6) {
                    if (isFreeRange(d, 60 * hact, (int) (60 * (hact + hpp)))) {
                        promiseEvent(d, 60 * hact, (int) (60 * (hact + hpp)));
                        completed = true;
                    }
                    hact--;
                }
            }
            if(!completed && dwTo != d){
                hpp += hpp/(dwTo-d);
            }
            dur -= hpp;
        }
        return completed;
    }

    private boolean scheduleByHour(float dur, int dwTo) {
        for(int d = actualDay; d<=dwTo && dur>0; d++){
            boolean completed = false;
            if(horario_pref == 'T'){
                int hact = 15;
                while(!completed && hact<22){
                    if(isFreeRange(d,60*hact,60*(hact+1))) {
                        promiseEvent(d, 60*hact, 60*(hact+1));
                        completed = true;
                    }
                    hact++;
                }
                hact = 12;
                while(!completed && hact>7){
                    if(isFreeRange(d,60*hact,60*(hact+1))) {
                        promiseEvent(d, 60*hact, 60*(hact+1));
                        completed = true;
                    }
                    hact--;
                }
            }
            else if(horario_pref == 'M'){
                int hact = 12;
                while(!completed && hact>7){
                    if(isFreeRange(d,60*hact,60*(hact+1))) {
                        promiseEvent(d, 60*hact, 60*(hact+1));
                        completed = true;
                    }
                    hact--;
                }
                hact = 15;
                while(!completed && hact<22){
                    if(isFreeRange(d,60*hact,60*(hact+1))) {
                        promiseEvent(d, 60*hact, 60*(hact+1));
                        completed = true;
                    }
                    hact++;
                }
            }
            else{
                int hact = 20;
                while(!completed && hact<22){
                    if(isFreeRange(d,60*hact,60*(hact+1))) {
                        promiseEvent(d, 60*hact, 60*(hact+1));
                        completed = true;
                    }
                    hact++;
                }
                hact = 20;
                while(!completed && hact>7){
                    if(isFreeRange(d,60*hact,60*(hact+1))) {
                        promiseEvent(d, 60*hact, 60*(hact+1));
                        completed = true;
                    }
                    hact--;
                }
            }
            if(!completed) return false;
            dur-=1;
        }
        return true;
    }

    private void promiseEvent(int dw, int mi, int mf) {
        promises.add(new TimeBlock(mi,mf,dw));
    }

    private boolean isFreeRange(int d, int mi, int mf){
        for (TimeBlock tb : timeBlocks){
            if(!tb.hasFreeTimeRange(mi,mf,d))
                return false;
        }
        return true;
    }

}
