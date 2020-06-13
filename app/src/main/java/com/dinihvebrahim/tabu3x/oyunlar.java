package com.dinihvebrahim.tabu3x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class oyunlar extends AppCompatActivity {
    private Button konus,ciz,sus,benKimim;
    private TextView BirinciTakim,IkinciTakim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyunlar);

        UITasarimi();

        konus=findViewById(R.id.konus);
        ciz=findViewById(R.id.ciz);
        sus=findViewById(R.id.sus);
        benKimim=findViewById(R.id.benKimim);

        BirinciTakim=findViewById(R.id.BirinciTakim);
        IkinciTakim=findViewById(R.id.IkinciTakim);

        String aTakimi=getIntent().getStringExtra("Takim_A");
        BirinciTakim.setText(aTakimi);

        String bTakimi=getIntent().getStringExtra("Takim_B");
        IkinciTakim.setText(bTakimi);

        konus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent2=new Intent(oyunlar.this,oyuna_basla.class);
                yeniIntent2.putExtra("Takim_A",BirinciTakim.getText().toString());
                yeniIntent2.putExtra("Takim_B",IkinciTakim.getText().toString());
                finish();
                startActivity(yeniIntent2);
            }
        });

        ciz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Yakında",Toast.LENGTH_LONG).show();
            }
        });

        sus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent3=new Intent(oyunlar.this,susarakAnlat.class);
                yeniIntent3.putExtra("Takim_A",BirinciTakim.getText().toString());
                yeniIntent3.putExtra("Takim_B",IkinciTakim.getText().toString());
                finish();
                startActivity(yeniIntent3);
            }
        });

        benKimim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent4=new Intent(oyunlar.this,benKimim.class);
                yeniIntent4.putExtra("Takim_A",BirinciTakim.getText().toString());
                yeniIntent4.putExtra("Takim_B",IkinciTakim.getText().toString());
                finish();
                startActivity(yeniIntent4);
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
            startActivity(new Intent(oyunlar.this,takimlar.class));
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}