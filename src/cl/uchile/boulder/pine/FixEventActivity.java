package cl.uchile.boulder.pine;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class FixEventActivity extends Activity{

    private int minsIni = 14*60 + 30;
    private int minsFin = 16*60;
    private Context context;
    private String[] nombreDias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private Spinner dias;
    private EditText nombre,descr;
    private EventosDB eventosDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fix_event_activity);
        getActionBar().setHomeButtonEnabled(true);
        setTitle("Nuevo Evento Fijo");
        context = this;

        eventosDB = new EventosDB(this,"DBPine",null,3);

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

        dias = (Spinner) findViewById(R.id.ev_dias);
        dias.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,nombreDias));

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
        Object[] args = {nombre.getText().toString(), descr.getText().toString(), dias.getSelectedItemPosition(), minsIni, minsFin-minsIni};
        db.execSQL("insert into fix_events(nom,descr,day,minstart,duration) values(?,?,?,?,?)",args);
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
