package com.dinihvebrahim.tabu3x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button takimButon;
    private Button ayarlar;
    private Button hakkinda;
    private Button magaza;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UITasarimi();



        takimButon=findViewById(R.id.button1);
        takimButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent=new Intent(MainActivity.this,takimlar.class);
                startActivity(yeniIntent);
                finish();
            }
        });

        ayarlar=findViewById(R.id.dogru);
        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Yakında",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        hakkinda=findViewById(R.id.yasak);
        hakkinda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,hakkinda.class));
                finish();
            }
        });

        magaza=findViewById(R.id.pas);
        magaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Yakında",Toast.LENGTH_LONG).show();
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

}