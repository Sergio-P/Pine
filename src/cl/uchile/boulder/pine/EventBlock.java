package cl.uchile.boulder.pine;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class EventBlock {

    private String name;
    private String description;
    private Context context;
    private int day;
    private int minStart, duration;
    private int id;
    private MyActivity master;

    public EventBlock(Context ctx, String text, int d, int ms, int dur, String descr, int i, MyActivity mast){
        context = ctx;
        name = text;
        day = d;
        minStart = ms;
        duration = dur;
        description = descr;
        id = i;
        master = mast;
    }

    public View createBlockView(){
        TextView v = new TextView(context);
        v.setText(name);
        v.setWidth(DefaultDimensions.BLOCK_WIDTH);
        v.setX(DefaultDimensions.MARGIN_NUMBERS + (DefaultDimensions.BLOCK_WIDTH+2)*(day));
        v.setY(DefaultDimensions.BLOCK_HEIGHT_FACTOR*(minStart-60*6));
        v.setHeight((int) (DefaultDimensions.BLOCK_HEIGHT_FACTOR*duration));
        v.setBackgroundColor(0xff00cc00);
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
        title.setBackgroundColor(0xff00cc00);
        ((TextView) dialog.findViewById(R.id.dialog_descr)).setText(description);
        int minEnd = minStart + duration;
        String prettyTime = ""+(minStart/60)+":"+((minStart%60<10)?"0":"")+(minStart%60)+" - "+(minEnd/60)+":"+((minEnd%60<10)?"0":"")+(minEnd%60);
        ((TextView) dialog.findViewById(R.id.dialog_time)).setText(prettyTime);

        ImageButton button = (ImageButton) dialog.findViewById(R.id.dialog_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                master.deleteFixed(id);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
