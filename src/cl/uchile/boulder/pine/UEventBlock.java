package cl.uchile.boulder.pine;

import android.app.Dialog;
import android.content.Context;


public class UEventBlock extends EventBlock {

    public UEventBlock(Context ctx, String text, int d, int ms, int dur, String descr, int i, MyActivity mast) {
        super(ctx, text, d, ms, dur, descr, i, mast);
        bgcol = 0xff0087f3;
    }

    @Override
    protected void onDialogDelete(Dialog dialog) {
        master.deleteUnique(id);
        dialog.dismiss();
    }
}
