package cl.uchile.boulder.pine.models;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cl.uchile.boulder.pine.utils.DefaultDimensions;
import cl.uchile.boulder.pine.R;
import cl.uchile.boulder.pine.activities.MyActivity;

public class EventBlock {

    protected String name;
    protected String description;
    protected Context context;
    protected int day;
    protected int minStart, duration;
    protected int id;
    protected MyActivity master;
    protected int bgcol;
    private int[] pinesRes = {R.drawable.ic_alertpine, R.drawable.ic_coolpine, R.drawable.ic_main,
            R.drawable.ic_sleeppine, R.drawable.ic_nerdpine};

    public EventBlock(Context ctx, String text, int d, int ms, int dur, String descr, int i, MyActivity mast){
        context = ctx;
        name = text;
        day = d;
        if(day>6 || day<0) day=6;
        minStart = ms;
        duration = dur;
        description = descr;
        id = i;
        master = mast;
        bgcol = 0xffd4b843;
    }

    public View createBlockView(){
        TextView v = new TextView(context);
        v.setText(name);
        v.setWidth(DefaultDimensions.BLOCK_WIDTH);
        v.setX(DefaultDimensions.MARGIN_NUMBERS + (DefaultDimensions.BLOCK_WIDTH+2)*(day));
        v.setY(DefaultDimensions.BLOCK_HEIGHT_FACTOR*(minStart-60*6));
        v.setHeight((int) (DefaultDimensions.BLOCK_HEIGHT_FACTOR*duration));
        v.setBackgroundColor(bgcol);
        v.setTextColor(0xff000000);
        v.setGravity(Gravity.CENTER);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });

        return v;
    }

    private void buildDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event);
        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        title.setText(name);
        title.setBackgroundColor(bgcol);
        ((TextView) dialog.findViewById(R.id.dialog_descr)).setText(description);
        int minEnd = minStart + duration;
        String prettyTime = prettyHora(minStart)+" - "+prettyHora(minEnd);
        ((TextView) dialog.findViewById(R.id.dialog_time)).setText(prettyTime);

        ImageButton button = (ImageButton) dialog.findViewById(R.id.dialog_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogDelete(dialog);
            }
        });

        ImageView icon = (ImageView) dialog.findViewById(R.id.dialog_icon);
        icon.setImageResource(pinesRes[(name.hashCode()*((name.hashCode()<0)?-1:1))%pinesRes.length]);
        dialog.show();
    }

    protected void onDialogDelete(Dialog dialog){
        master.deleteFixed(id);
        dialog.dismiss();
    }

    protected String prettyHora(int mins){
        return ""+(mins/60)+":"+((mins%60<10)?"0":"")+(mins%60);
    }

}
