package com.chaohu.wemana.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaohu.wemana.R;

public class WeManaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_mana);
        Button send = (Button) findViewById(R.id.send_message);
        final EditText message = (EditText) findViewById(R.id.edit_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = getIntent();
                Bundle bd = new Bundle();
                bd.putString("weight",message.getText().toString());
                it.putExtras(bd);
                setResult(0x123,it);
                finish();
            }
        });
    }

}
