package cl.uchile.boulder.pine;

import android.app.Activity;
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
       eventosDB = new EventosDB(this,"DBPine",null,1);
    }

    private void buildEventBlocks() {
        EventBlock block = new EventBlock(this,"TEST",6,60*15,90);
        blockLayout.addView(block.getBlockView());
        EventBlock block2 = new EventBlock(this,"TEST2",7,60*14,90);
        blockLayout.addView(block2.getBlockView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (featureId){
            case R.id.menu_add:
                ;
                break;
        }
        return true;
    }

}
