package cl.uchile.boulder.pine;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class EventBlock {

    private String name;
    private Context context;
    private int day;
    private int minStart, duration;

    public EventBlock(Context ctx, String text, int d, int ms, int dur){
        context = ctx;
        name = text;
        day = d;
        minStart = ms;
        duration = dur;
    }

    public View getBlockView(){
        TextView v = new TextView(context);
        v.setText(name);
        v.setWidth(DefaultDimensions.BLOCK_WIDTH);
        v.setX(DefaultDimensions.MARGIN_NUMBERS + (DefaultDimensions.BLOCK_WIDTH+2)*(day-1));
        v.setY(DefaultDimensions.BLOCK_HEIGHT_FACTOR*(minStart-60*6));
        v.setHeight((int) (DefaultDimensions.BLOCK_HEIGHT_FACTOR*duration));
        v.setBackgroundColor(0xff00cc00);
        return v;
    }


}
