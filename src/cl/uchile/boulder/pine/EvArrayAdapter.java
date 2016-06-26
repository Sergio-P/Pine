package cl.uchile.boulder.pine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EvArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<UEventBlock> blocks;
    private Context context;

    public EvArrayAdapter(Context ctx, ArrayList<UEventBlock> b) {
        super(ctx, R.layout.dialog_ev_list, new String[b.size()]);
        blocks = b;
        context = ctx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder vh;

        if(item==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.list_ev_item, parent, false);
            vh = new ViewHolder();
            vh.bg = (LinearLayout) item.findViewById(R.id.list_item_bg);
            vh.text = (TextView) item.findViewById(R.id.list_item_text);
            item.setTag(vh);
        }
        else{
            vh = (ViewHolder) item.getTag();
        }

        UEventBlock block = blocks.get(position);

        vh.bg.setBackgroundColor((position%2==1)?0xffffffff:0xffe5d489);
        vh.text.setText(block.getInfo());

        return item;
    }

    static class ViewHolder {
        LinearLayout bg;
        TextView text;
    }
}
