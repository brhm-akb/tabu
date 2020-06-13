package com.dinihvebrahim.tabu3x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class takimlar extends AppCompatActivity {
    private Button menu1,basla;
    private EditText TakimA,TakimB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takimlar);

        UITasarimi();

        menu1=findViewById(R.id.menu1);
        basla=findViewById(R.id.basla);
        TakimA=findViewById(R.id.TakimA);
        TakimB=findViewById(R.id.TakimB);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent1=new Intent(takimlar.this,MainActivity.class);
                startActivity(yeniIntent1);
                finish();
            }
        });
        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent2=new Intent(takimlar.this,oyunlar.class);
                yeniIntent2.putExtra("Takim_A",TakimA.getText().toString());
                yeniIntent2.putExtra("Takim_B",TakimB.getText().toString());
                startActivity(yeniIntent2);
                finish();

            }
        });
    }

    private void UITasarimi(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UITasarimi();
    } // oyun arka plana düştükten sonra oyun tekrar açılınca alt ve üstteki navigasyon bölmeleri gidecek

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(takimlar.this,MainActivity.class));
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}