package cl.uchile.boulder.pine;

import android.app.Dialog;
import android.content.Context;


public class UEventBlock extends EventBlock {

    private boolean autoGen;

    public UEventBlock(Context ctx, String text, int d, int ms, int dur, String descr, boolean autoGen, int i, MyActivity mast) {
        super(ctx, text, d, ms, dur, descr, i, mast);
        this.autoGen = autoGen;
        if(!autoGen)
            bgcol = 0xff2398f4;
        else
            bgcol = 0xff12cd23;
    }

    @Override
    protected void onDialogDelete(Dialog dialog) {
        if(!autoGen)
            master.deleteUnique(id);
        dialog.dismiss();
    }
}
