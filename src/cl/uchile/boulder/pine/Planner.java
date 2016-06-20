package cl.uchile.boulder.pine;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Planner {

    private int year, week;
    private ArrayList<TimeBlock> timeBlocks;
    private ArrayList<TimeBlock> promises;
    private String evName;

    private int actualDay;
    private String evDescr;

    private Planner(int y, int w){
        timeBlocks = new ArrayList<TimeBlock>();
        year = y;
        week = w;
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
            Object[] args = {evName, evDescr, year*1000+week*10+tb.dow, tb.minIni, tb.minFin-tb.minIni};
            db.execSQL("insert into unique_event(nom,descr,fecha,minstart,duration) values(?,?,?,?,?)",args);
        }
    }

    private void cancelPromise() {
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
        float hpp = (float) (Math.floor(4f*(actualDay -dwTo)/dur+0.5f)/4f);
        for(int d = actualDay; d<=dwTo && dur>0; d++){
            boolean completed = false;
            int hact = 15;
            while(!completed && hact<23.75-hpp){
                if(isFreeRange(d,60*hact, (int) (60*(hact+hpp)))) {
                    promiseEvent(d, 60*hact, (int) (60*(hact+hpp)));
                    completed = true;
                }
                hact++;
            }
            if(!completed) return false;
        }
        return true;
    }

    private boolean scheduleByHour(float dur, int dwTo) {
        for(int d = actualDay; d<=dwTo && dur>0; d++){
            boolean completed = false;
            int hact = 15;
            while(!completed && hact<22){
                if(isFreeRange(d,60*hact,60*(hact+1))) {
                    promiseEvent(d, 60*hact, 60*(hact+1));
                    completed = true;
                }
                hact++;
            }
            if(!completed) return false;
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


    public void setActualDay(int actualDay) {
        this.actualDay = actualDay;
    }
}
