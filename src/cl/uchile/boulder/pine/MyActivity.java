package cl.uchile.boulder.pine;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class MyActivity extends Activity {

    private RelativeLayout blockLayout;
    private boolean built;
    private EventosDB eventosDB;
    private ArrayList<View> blocks;
    private int curYear, curWeek;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        blockLayout = (RelativeLayout) findViewById(R.id.main_layout);
        blocks = new ArrayList<>();
        built = false;
        blockLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!built){
                    DefaultDimensions.BLOCK_HEIGHT_FACTOR = v.getHeight()/(60f*18);
                    DefaultDimensions.MARGIN_NUMBERS = findViewById(R.id.hours_label).getWidth() + 4;
                    DefaultDimensions.BLOCK_WIDTH = (v.getWidth() - DefaultDimensions.MARGIN_NUMBERS - 12)/7;
                    buildEventBlocks();
                    built = true;
                }
            }
        });
        eventosDB = new EventosDB(this,"DBPine",null,3);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        curYear = calendar.get(Calendar.YEAR);
        curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        updateTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildEventBlocks();
    }

    private void buildEventBlocks() {

        for(View v : blocks)
            blockLayout.removeView(v);

        SQLiteDatabase db = eventosDB.getReadableDatabase();

        // Fixed Events
        Cursor cursor = db.rawQuery("select nom, descr, day, minstart, duration, id from fix_events",null);
        if(cursor.moveToFirst()){
            do{
                String nom = cursor.getString(0);
                String desc = cursor.getString(1);
                int day = cursor.getInt(2);
                int minstart = cursor.getInt(3);
                int dur = cursor.getInt(4);
                int id = cursor.getInt(5);
                EventBlock block = new EventBlock(this,nom,day,minstart,dur,desc,id,this);
                View v = block.createBlockView();
                blockLayout.addView(v);
                blocks.add(v);
            }
            while(cursor.moveToNext());
        }
        cursor.close();

        // Unique Events
        cursor = db.rawQuery("select nom, descr, fecha, minstart, duration, autogen, id from unique_event",null);
        if(cursor.moveToFirst()){
            do{
                String nom = cursor.getString(0);
                String desc = cursor.getString(1);
                int fecha = cursor.getInt(2);
                int minstart = cursor.getInt(3);
                int dur = cursor.getInt(4);
                boolean agen = cursor.getInt(5) == 1;
                int id = cursor.getInt(6);
                Log.d("PINE",""+fecha);
                if(fecha/1000 == curYear && (fecha%1000)/10 == curWeek) {
                    int day = fecha%10;
                    UEventBlock block = new UEventBlock(this, nom, day, minstart, dur, desc, agen, id, this);
                    View v = block.createBlockView();
                    blockLayout.addView(v);
                    blocks.add(v);
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();


    }

    private void updateTitle(){
        if(curWeek<0){
            curYear--;
            curWeek = 52;
        }
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.YEAR,curYear);
        Log.d("APP",""+curWeek);
        c.set(Calendar.WEEK_OF_YEAR,curWeek);
        c.get(Calendar.DAY_OF_WEEK);
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String ini = ""+c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1);
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        int mf = c.get(Calendar.MONTH) + 1;
        if(mf==1 && curWeek>30){
            curYear++;
            curWeek = 0;
        }
        String fin = ""+c.get(Calendar.DAY_OF_MONTH)+"/"+mf;
        setTitle(ini + " - "+ fin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_fix:
                gotoAddFixEventActivity();
                break;
            case R.id.menu_add_unique:
                startActivity(new Intent(getBaseContext(),UniqueEventActivity.class));
                break;
            case R.id.menu_add_dyn:
                startActivity(new Intent(getBaseContext(),DynEventActivity.class));
                break;
            case R.id.menu_next_week:
                curWeek++;
                updateTitle();
                buildEventBlocks();
                break;
            case R.id.menu_prev_week:
                curWeek--;
                updateTitle();
                buildEventBlocks();
                break;
        }
        return true;
    }

    private void gotoAddFixEventActivity() {
        Intent i = new Intent(getBaseContext(),FixEventActivity.class);
        startActivity(i);
    }

    public void deleteFixed(int id) {
        deleteFromTable("fix_events",id);
    }

    public void deleteUnique(int id) {
        deleteFromTable("unique_event",id);
    }

    private void deleteFromTable(String tbl, int id){
        SQLiteDatabase db = eventosDB.getWritableDatabase();
        Object[] args = {id};
        db.execSQL("delete from "+tbl+" where id = ?", args);
        buildEventBlocks();
    }

}
