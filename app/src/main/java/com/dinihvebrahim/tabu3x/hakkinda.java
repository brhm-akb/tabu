    package com.dinihvebrahim.tabu3x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class hakkinda extends AppCompatActivity {
    private Button instagram,twitter,gmail,geriGit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkinda);

        UITasarimi();

        instagram=findViewById(R.id.Instagram);
        twitter=findViewById(R.id.Twitter);
        gmail=findViewById(R.id.gmail);
        geriGit=findViewById(R.id.geriGit);

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insta= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tabu3x/?hl=tr"));
                startActivity(insta);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twit= new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/brhm_akb"));
                startActivity(twit);
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                emailintent.setType("plain/text");
                emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"tabu3xiletisim@gmail.com" });
                emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailintent, "Send mail..."));
            }
        });

        geriGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(hakkinda.this,MainActivity.class));
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
            startActivity(new Intent(hakkinda.this,MainActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}