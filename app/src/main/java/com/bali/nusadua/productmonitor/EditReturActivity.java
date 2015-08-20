package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.model.Retur;

/**
 * Created by desu on 7/19/15.
 */
public class EditReturActivity extends ActionBarActivity implements android.view.View.OnClickListener {
    private Retur retur = new Retur();
    private EditText returCode, returName, price, qty;
    private Spinner unit;
    private Button btnSave;
    private Double tempPrice = 0d;
    private int currentUnitSelection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_retur);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        returCode = (EditText) findViewById(R.id.retur_code);
        returName = (EditText) findViewById(R.id.retur_name);
        price = (EditText) findViewById(R.id.retur_price);
        qty = (EditText) findViewById(R.id.retur_qty);
        unit = (Spinner) findViewById(R.id.unit_spinner);
        btnSave = (Button) findViewById(R.id.button_save);

        Intent intent = getIntent();
        retur = (Retur) intent.getSerializableExtra(Retur.TABLE);

        returCode.setText(retur.getKode());
        returName.setText(retur.getNamaBarang());
        price.setText(String.valueOf(retur.getHarga()));
        qty.setText(String.valueOf(retur.getQty()));

        currentUnitSelection = retur.getUnit().equalsIgnoreCase(Retur.LOOKUP_DUS) ? 0 : 1;
        unit.setSelection(currentUnitSelection);
        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentUnitSelection != i) {
                    if (i == Order.LOOKUP_DUS_index) {
                        Double newPrice = retur.getHarga() * 12;
                        newPrice = newPrice + tempPrice;
                        retur.setHarga(newPrice);
                    } else {
                        Double newPrice = retur.getHarga() / 12;
                        tempPrice = retur.getHarga() - (newPrice * 12);
                        retur.setHarga(newPrice);
                    }
                }
                price.setText(String.valueOf(retur.getHarga()));
                currentUnitSelection = i;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        btnSave.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_retur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you medirecords_adminspecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();

                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.button_save)) {
            Retur resultRetur = new Retur();
            resultRetur.setId(retur.getId());
            resultRetur.setKode(returCode.getText().toString());
            resultRetur.setNamaBarang(returName.getText().toString());
            resultRetur.setHarga(Double.parseDouble(price.getText().toString()));
            resultRetur.setQty(Integer.parseInt(qty.getText().toString()));
            resultRetur.setUnit(unit.getSelectedItem().toString());
            resultRetur.setKodeOutlet(retur.getKodeOutlet());

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Retur.TABLE, resultRetur);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
