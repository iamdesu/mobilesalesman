package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by desu sudarsana on 4/4/2015.
 */
public class AmbilDataActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_data);
    }

    public void onBtnKeluarClick(View view) {
        Intent intent = new Intent(AmbilDataActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
