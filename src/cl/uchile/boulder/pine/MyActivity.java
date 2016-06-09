package cl.uchile.boulder.pine;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class MyActivity extends Activity {

    private RelativeLayout blockLayout;
    private boolean built;
    private EventosDB eventosDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        blockLayout = (RelativeLayout) findViewById(R.id.main_layout);
        built = false;
        blockLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!built){
                    DefaultDimensions.BLOCK_HEIGHT_FACTOR = v.getHeight()/(60f*18);
                    DefaultDimensions.MARGIN_NUMBERS = findViewById(R.id.hours_label).getWidth();
                    DefaultDimensions.BLOCK_WIDTH = (v.getWidth() - DefaultDimensions.MARGIN_NUMBERS - 12)/7;
                    buildEventBlocks();
                    built = true;
                }
            }
        });
       eventosDB = new EventosDB(this,"DBPine",null,2);
    }

    private void buildEventBlocks() {
        blockLayout.removeAllViews();

        SQLiteDatabase db = eventosDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select nom, descr, day, minstart, duration from fix_events",null);

        if(cursor.moveToFirst()){
            do{
                String nom = cursor.getString(0);
                String desc = cursor.getString(1);
                int day = cursor.getInt(2);
                int minstart = cursor.getInt(3);
                int dur = cursor.getInt(4);
                EventBlock block = new EventBlock(this,nom,day,minstart,dur);
                blockLayout.addView(block.getBlockView());
            }
            while(cursor.moveToNext());
        }

        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                gotoAddEventActivity();
                break;
        }
        return true;
    }

    private void gotoAddEventActivity() {
        Intent i = new Intent(getBaseContext(),FixEventActivity.class);
        startActivity(i);
    }

}
