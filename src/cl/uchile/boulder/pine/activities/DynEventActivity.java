package cl.uchile.boulder.pine.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import cl.uchile.boulder.pine.utils.EventosDB;
import cl.uchile.boulder.pine.planner.MasterPlanner;
import cl.uchile.boulder.pine.R;

import java.util.Calendar;

public class DynEventActivity extends Activity{

    private int minsIni = 14*60 + 30;
    private int minsFin = 16*60;
    private Context context;
    private EditText nombre,descr,trbj;
    private EventosDB eventosDB;
    private int year,week,dow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dyn_event_activity);
        //getActionBar().setHomeButtonEnabled(true);
        setTitle("Nuevo Evento Dinámico");
        context = this;

        eventosDB = new EventosDB(this,"DBPine",null,4);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final Button fecha = (Button) findViewById(R.id.ev_fecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog;
                dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearC, int monthOfYear, int dayOfMonth) {
                        year = yearC;
                        Calendar c = Calendar.getInstance();
                        c.set(yearC,monthOfYear,dayOfMonth);
                        c.setFirstDayOfWeek(Calendar.MONDAY);
                        c.get(Calendar.DAY_OF_MONTH);
                        dow = c.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
                        if(dow<0) dow=6;
                        week = c.get(Calendar.WEEK_OF_YEAR);
                        fecha.setText(""+dayOfMonth+" - "+monthOfYear+" - "+year);
                    }
                },year,month,day);
                dialog.show();
            }
        });

        nombre = (EditText) findViewById(R.id.ev_nombre);
        descr = (EditText) findViewById(R.id.ev_desc);
        trbj = (EditText) findViewById(R.id.ev_horas_trbj);

        Button addBtn = (Button) findViewById(R.id.ev_agregar_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void addItem(){
        MasterPlanner masterPlanner = new MasterPlanner(this,eventosDB.getWritableDatabase());
        masterPlanner.findPlan(nombre.getText().toString(),descr.getText().toString(),Float.parseFloat(trbj.getText().toString()),week,year,dow);
        finish();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onMenuItemSelected(featureId,item);
        }
    }
}
