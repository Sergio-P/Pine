package cl.uchile.boulder.pine.planner;

public class TimeBlock {

    public int minIni, minFin, dow;
    //private int dow, week, year;

    /*
    public TimeBlock(int mi, int mf, int dw, int wk, int yr){
        minIni = mi;
        minFin = mf;
        dow = dw;
        week = wk;
        year = yr;
    }
    */

    public TimeBlock(int mi, int mf, int dw){
        minIni = mi;
        minFin = mf;
        dow = dw;
    }


    public boolean hasFreeTimeRange(int mi, int mf, int dw){
        return dow != dw || (minIni>=mf) || (minFin<=mi);
    }

    /*
    public boolean hasDay(int dw, int wk, int yr){
        return dw == dow && wk == week && year ==yr;
    }


    public boolean hasExactTime(int min, int dw, int wk, int yr){
        return hasTime(min) && hasDay(dw,wk,yr);
    }
    */

}
