package cl.uchile.boulder.pine;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class UniqueEventActivity extends Activity{

    private int minsIni = 14*60 + 30;
    private int minsFin = 16*60;
    private Context context;
    private EditText nombre,descr;
    private EventosDB eventosDB;
    private int year,week,dow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unique_event_activity);
        getActionBar().setHomeButtonEnabled(true);
        setTitle("Nuevo Evento Único");
        context = this;

        eventosDB = new EventosDB(this,"DBPine",null,4);

        final Button horaInicio = (Button) findViewById(R.id.ev_hora_ini);
        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        minsIni = 60*hourOfDay + minute;
                        horaInicio.setText(hourOfDay+":"+((minute<10)?"0":"")+minute);
                    }
                },minsIni/60,minsIni%60,true);
                dialog.setTitle("Hora inicio");
                dialog.show();
            }
        });

        final Button horaFin = (Button) findViewById(R.id.ev_hora_fin);
        horaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        minsFin = 60*hourOfDay + minute;
                        horaFin.setText(hourOfDay+":"+((minute<10)?"0":"")+minute);
                    }
                },minsFin/60,minsFin%60,true);
                dialog.setTitle("Hora fin");
                dialog.show();
            }
        });

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
                        fecha.setText(""+dayOfMonth+" - "+(monthOfYear+1)+" - "+year);
                    }
                },year,month,day);
                dialog.show();
            }
        });

        nombre = (EditText) findViewById(R.id.ev_nombre);
        descr = (EditText) findViewById(R.id.ev_desc);

        Button addBtn = (Button) findViewById(R.id.ev_agregar_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void addItem(){
        SQLiteDatabase db = eventosDB.getWritableDatabase();
        Object[] args = {nombre.getText().toString(), descr.getText().toString(), year*1000+week*10+dow, minsIni, minsFin-minsIni};
        db.execSQL("insert into unique_event(nom,descr,fecha,minstart,duration) values(?,?,?,?,?)",args);
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
