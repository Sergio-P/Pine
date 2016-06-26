package cl.uchile.boulder.pine;

import android.app.Dialog;
import android.content.Context;


public class UEventBlock extends EventBlock {

    private boolean autoGen;
    private int week;

    public UEventBlock(Context ctx, String text, int d, int ms, int dur, String descr, boolean autoGen, int wk, int i, MyActivity mast) {
        super(ctx, text, d, ms, dur, descr, i, mast);
        this.autoGen = autoGen;
        if(!autoGen)
            bgcol = 0xff2398f4;
        else
            bgcol = 0xff12cd23;
        week = wk;
    }

    @Override
    protected void onDialogDelete(Dialog dialog) {
        if(!autoGen)
            master.deleteUnique(id);
        dialog.dismiss();
    }

    public String getInfo(){
        return name+" - "+master.prettyFecha(day,week)+" ("+prettyHora(minStart)+"-"+prettyHora(minStart+duration)+")";
    }
}
