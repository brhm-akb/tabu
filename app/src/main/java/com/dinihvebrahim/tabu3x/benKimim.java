package com.dinihvebrahim.tabu3x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class benKimim extends AppCompatActivity {

    public static long START_TIME_IN_MILLES = 60000;
    private CountDownTimer countDownTimer;
    private boolean sayacCalisiyorMu;
    private long timeLeftInMillis = START_TIME_IN_MILLES;

    private TextView takimIsmi,sonuc4,kelime4;
    private Button durdur;

    int sonuc=0,sonucA=0,sonucB=0;
    int sirakimde=0;

    String diziler[][];
    int sayi,Sorusec[]=new int[100]; //Kelime Sayısı Kadar
    int dizi=1;
    int kelimeSayisi=1;
    int KALANPAS=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ben_kimim);

        UITasarimi();
        cagir();
        oyunBasliyor();

        takimIsmi=findViewById(R.id.takimIsmi4);
        sonuc4=findViewById(R.id.sonuc4);
        kelime4=findViewById(R.id.Kelime4);
        durdur=findViewById(R.id.sayac4);



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UITasarimi();
    }// oyun arka plana düştükten sonra oyun tekrar açılınca alt ve üstteki navigasyon bölmeleri gidecek

    @Override
    protected void onStop() {
        super.onStop();

    }// home tuşuna basınca oyun duracak

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){// ses kısma tuşu
            yeniKelime();
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){// ses arttırma tuşu
            sonuc++;
            sonuc4.setText("Sonuc: "+sonuc);
            yeniKelime();
            if(sirakimde%2==0){
                sonucA++;
            }
            if(sirakimde%2==1){
                sonucB++;
            }

            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_BACK){
            oyunDurdu();
            sayaciDurdur();

        }


        return super.onKeyDown(keyCode, event);
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

    private void oyunBasliyor(){

        View basla = getLayoutInflater().inflate(R.layout.basla,null);

        TextView ATAKIMI = basla.findViewById(R.id.takim1);
        TextView BTAKIMI = basla.findViewById(R.id.takim2);

        String atakimi = getIntent().getStringExtra("Takim_A");
        String btakimi = getIntent().getStringExtra("Takim_B");


        ATAKIMI.setText(atakimi);
        BTAKIMI.setText(btakimi);

        AlertDialog.Builder baslaAD = new AlertDialog.Builder(benKimim.this);

        baslaAD.setView(basla);

        baslaAD.setCancelable(false);


        baslaAD.setPositiveButton("Başla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String aTakimi=getIntent().getStringExtra("Takim_A");
                takimIsmi.setText(aTakimi);
                UITasarimi();
                geriSayim();
            }
        });

        baslaAD.create().show();

    }//oyun başlayınca hazır sekmesi gelecek

    private void oyunDurdu(){

        AlertDialog.Builder oyunDurdu = new AlertDialog.Builder(benKimim.this);

        oyunDurdu.setIcon(R.drawable.ic_baseline_pause_24);
        oyunDurdu.setTitle("Oyun Durdu");

        oyunDurdu.setCancelable(false);

        oyunDurdu.setPositiveButton("Oyuna Devam Et", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sayaciBaslat();
                UITasarimi();
            }
        });
        oyunDurdu.setNegativeButton("Menüye Dön", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(benKimim.this,MainActivity.class));
                finish();
            }
        });
        oyunDurdu.create().show();
    }

    private void sayaciBaslat(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                yeniSayac();
            }

            @Override
            public void onFinish() {
                sayacCalisiyorMu=false;
                sureBitti();
            }
        }.start();
        sayacCalisiyorMu=true;
    }

    private void sayaciDurdur(){
        countDownTimer.cancel();
        sayacCalisiyorMu=false;
    }

    private void sayaciTekrarEt(){
        timeLeftInMillis = START_TIME_IN_MILLES;
        sayaciBaslat();
    }

    private void yeniSayac(){
        int minute = (int) (timeLeftInMillis / 1000) / 60;
        int second = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minute,second);

        durdur.setText(timeLeftFormatted);
    }

    private void sureBitti(){
        View sonuclar = getLayoutInflater().inflate(R.layout.sonuc,null);

        TextView ATAKIMI = sonuclar.findViewById(R.id.ATAKIMI);
        TextView BTAKIMI = sonuclar.findViewById(R.id.BTAKIMI);
        TextView ASONUC = sonuclar.findViewById(R.id.ASONUC);
        TextView BSONUC = sonuclar.findViewById(R.id.BSONUC);

        String atakimi = getIntent().getStringExtra("Takim_A");
        String btakimi = getIntent().getStringExtra("Takim_B");

        ATAKIMI.setText(atakimi);
        BTAKIMI.setText(btakimi);
        ASONUC.setText(""+sonucA);
        BSONUC.setText(""+sonucB);

        AlertDialog.Builder ad = new AlertDialog.Builder(benKimim.this);

        ad.setView(sonuclar);

        ad.setCancelable(false);


        ad.setPositiveButton("Oyuna Devam Et", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sayaciTekrarEt();
                yeniKelime();
                UITasarimi();
                sonuc=0;
                KALANPAS=3;
                sonuc4.setText("SONUÇ: "+sonuc);
                sirakimde++;
                if(sirakimde%2==0){
                    String atakimi = getIntent().getStringExtra("Takim_A");
                    takimIsmi.setText(atakimi);
                }
                else{
                    String btakimi = getIntent().getStringExtra("Takim_B");
                    takimIsmi.setText(btakimi);
                }
            }
        });

        ad.create().show();
    }

    private void yeniKelime(){

        String dizi0[]= new String[1];
        String dizi1[]= new String[1]; String dizi2[]= new String[1]; String dizi3[]= new String[1]; String dizi4[]= new String[1]; String dizi5[]= new String[1];
        String dizi6[]= new String[1]; String dizi7[]= new String[1]; String dizi8[]= new String[1]; String dizi9[]= new String[1]; String dizi10[]= new String[1];
        String dizi11[]= new String[1]; String dizi12[]= new String[1]; String dizi13[]= new String[1]; String dizi14[]= new String[1]; String dizi15[]= new String[1];
        String dizi16[]= new String[1]; String dizi17[]= new String[1]; String dizi18[]= new String[1]; String dizi19[]= new String[1]; String dizi20[]= new String[1];
        String dizi21[]= new String[1]; String dizi22[]= new String[1]; String dizi23[]= new String[1]; String dizi24[]= new String[1]; String dizi25[]= new String[1];
        String dizi26[]= new String[1]; String dizi27[]= new String[1]; String dizi28[]= new String[1]; String dizi29[]= new String[1]; String dizi30[]= new String[1];
        String dizi31[]= new String[1]; String dizi32[]= new String[1]; String dizi33[]= new String[1]; String dizi34[]= new String[1]; String dizi35[]= new String[1];
        String dizi36[]= new String[1]; String dizi37[]= new String[1]; String dizi38[]= new String[1]; String dizi39[]= new String[1]; String dizi40[]= new String[1];
        String dizi41[]= new String[1]; String dizi42[]= new String[1]; String dizi43[]= new String[1]; String dizi44[]= new String[1]; String dizi45[]= new String[1];
        String dizi46[]= new String[1]; String dizi47[]= new String[1]; String dizi48[]= new String[1]; String dizi49[]= new String[1]; String dizi50[]= new String[1];
        String dizi51[]= new String[1]; String dizi52[]= new String[1]; String dizi53[]= new String[1]; String dizi54[]= new String[1]; String dizi55[]= new String[1];
        String dizi56[]= new String[1]; String dizi57[]= new String[1]; String dizi58[]= new String[1]; String dizi59[]= new String[1]; String dizi60[]= new String[1];
        String dizi61[]= new String[1]; String dizi62[]= new String[1]; String dizi63[]= new String[1]; String dizi64[]= new String[1]; String dizi65[]= new String[1];
        String dizi66[]= new String[1]; String dizi67[]= new String[1]; String dizi68[]= new String[1]; String dizi69[]= new String[1]; String dizi70[]= new String[1];
        String dizi71[]= new String[1]; String dizi72[]= new String[1]; String dizi73[]= new String[1]; String dizi74[]= new String[1]; String dizi75[]= new String[1];
        String dizi76[]= new String[1]; String dizi77[]= new String[1]; String dizi78[]= new String[1]; String dizi79[]= new String[1]; String dizi80[]= new String[1];
        String dizi81[]= new String[1]; String dizi82[]= new String[1]; String dizi83[]= new String[1]; String dizi84[]= new String[1]; String dizi85[]= new String[1];
        String dizi86[]= new String[1]; String dizi87[]= new String[1]; String dizi88[]= new String[1]; String dizi89[]= new String[1]; String dizi90[]= new String[1];
        String dizi91[]= new String[1]; String dizi92[]= new String[1]; String dizi93[]= new String[1]; String dizi94[]= new String[1]; String dizi95[]= new String[1];
        String dizi96[]= new String[1]; String dizi97[]= new String[1]; String dizi98[]= new String[1]; String dizi99[]= new String[1]; String dizi100[]= new String[1];

        dizi1[0]="Acun Ilıcalı"; dizi2[0]="Ahmet Çakar"; dizi3[0]="Ahmet Kaya"; dizi4[0]="Albert Einstein"; dizi5[0]="Aleyna Tilki";
        dizi6[0]="Anne"; dizi7[0]="Arda Turan"; dizi8[0]="Armut"; dizi9[0]="Asker"; dizi10[0]="Aslan";
        dizi11[0]="Ayı"; dizi12[0]="Aziz Yıldırım"; dizi13[0]="Baba"; dizi14[0]="Balina"; dizi15[0]="Bardak";
        dizi16[0]="Barış Manço"; dizi17[0]="Batman"; dizi18[0]="Bill Gates"; dizi19[0]="Canan Karatay"; dizi20[0]="Cem Yılmaz";
        dizi21[0]="Ceza"; dizi22[0]="Cristiano Ronaldo"; dizi23[0]="Cüneyt Özdemir"; dizi24[0]="CZN Burak"; dizi25[0]="Demet Akalın";
        dizi26[0]="Doğukan Manço"; dizi27[0]="Donald Trump"; dizi28[0]="Duman"; dizi29[0]="Edis"; dizi30[0]="Emre Belözoğlu";
        dizi31[0]="Engin Altan Düzyatan"; dizi32[0]="Eser Yenenler"; dizi33[0]="Eski Sevgilim"; dizi34[0]="Fahrettin Koca"; dizi35[0]="Fatih Terim";
        dizi36[0]="Fernando Muslera"; dizi37[0]="Goethe"; dizi38[0]="Graham Bell"; dizi39[0]="Hadise"; dizi40[0]="Halil Sezai";
        dizi41[0]="Haluk Levent"; dizi42[0]="Harry Potter"; dizi43[0]="Hulk"; dizi44[0]="İbrahim Büyükak"; dizi45[0]="İlber Ortaylı";
        dizi46[0]="İrem Derici"; dizi47[0]="Joker"; dizi48[0]="Kalem"; dizi49[0]="Kaplan"; dizi50[0]="Karınca";
        dizi51[0]="Kertenkele"; dizi52[0]="Kartal"; dizi53[0]="Kıvanç Tatlıtuğ"; dizi54[0]="Killa Hakan"; dizi55[0]="Kobe Braynt";
        dizi56[0]="Lionel Messi"; dizi57[0]="Manuş Baba"; dizi58[0]="Mark Zuckerberg"; dizi59[0]="Maymun"; dizi60[0]="Merih Demiral";
        dizi61[0]="MFÖ"; dizi62[0]="Mona Lisa"; dizi63[0]="Muhammed Salah"; dizi64[0]="Murat Boz"; dizi65[0]="Mustafa Ceceli";
        dizi66[0]="Nasrettin Hoca"; dizi67[0]="Nazım Hikmet Ran"; dizi68[0]="Necip Fazıl Kısakürek"; dizi69[0]="Nihat Doğan"; dizi70[0]="Nusret";
        dizi71[0]="Oğuzhan Koç"; dizi72[0]="Oktay Kaynarca"; dizi73[0]="Öğretmen"; dizi74[0]="Örümcek Adam"; dizi75[0]="Papağan";
        dizi76[0]="Patates"; dizi77[0]="Polat Alemdar"; dizi78[0]="Polis"; dizi79[0]="Recep İvedik"; dizi80[0]="Rıza Baba";
        dizi81[0]="Sabri Sarıoğlu"; dizi82[0]="Sagopa Kajmer"; dizi83[0]="Sandalye"; dizi84[0]="Sezai Karakoç"; dizi85[0]="Sokrates";
        dizi86[0]="Steve Jobs"; dizi87[0]="Şahan Gökbakar"; dizi88[0]="Şevket Çoruh"; dizi89[0]="Şeyma Subaşı"; dizi90[0]="Tarkan";
        dizi91[0]="Tavuk"; dizi92[0]="Top"; dizi93[0]="Uzaylı"; dizi94[0]="Vladimir Putin"; dizi95[0]="Volkan Demirel";
        dizi96[0]="Yılan"; dizi97[0]="Yıldız Tilbe"; dizi98[0]="Yılmaz Erdoğan"; dizi99[0]="Yılamz Morgül"; dizi100[0]="Yusuf Güney";



        String Diziler[]={
                dizi0[0],
                dizi1[0],dizi2[0],dizi3[0],dizi4[0],dizi5[0],
                dizi6[0],dizi7[0],dizi8[0],dizi9[0],dizi10[0],
                dizi11[0],dizi12[0],dizi13[0],dizi14[0],dizi15[0],
                dizi16[0],dizi17[0],dizi18[0],dizi19[0],dizi20[0],
                dizi21[0],dizi22[0],dizi23[0],dizi24[0],dizi25[0],
                dizi26[0],dizi27[0],dizi28[0],dizi29[0],dizi30[0],
                dizi31[0],dizi32[0],dizi33[0],dizi34[0],dizi35[0],
                dizi36[0],dizi37[0],dizi38[0],dizi39[0],dizi40[0],
                dizi41[0],dizi42[0],dizi43[0],dizi44[0],dizi45[0],
                dizi46[0],dizi47[0],dizi48[0],dizi49[0],dizi50[0],
                dizi51[0],dizi52[0],dizi53[0],dizi54[0],dizi55[0],
                dizi56[0],dizi57[0],dizi58[0],dizi59[0],dizi60[0],
                dizi61[0],dizi62[0],dizi63[0],dizi64[0],dizi65[0],
                dizi66[0],dizi67[0],dizi68[0],dizi69[0],dizi70[0],
                dizi71[0],dizi72[0],dizi73[0],dizi74[0],dizi75[0],
                dizi76[0],dizi77[0],dizi78[0],dizi79[0],dizi80[0],
                dizi81[0],dizi82[0],dizi83[0],dizi84[0],dizi85[0],
                dizi86[0],dizi87[0],dizi88[0],dizi89[0],dizi90[0],
                dizi91[0],dizi92[0],dizi93[0],dizi94[0],dizi95[0],
                dizi96[0],dizi97[0],dizi98[0],dizi99[0],dizi100[0],
        };


        kelime4.setText(Diziler[Sorusec[dizi]]);

        kelimeSayisi++;
        dizi++;

        if(kelimeSayisi%99==0){ //Kelime Sayısından Bir Eksik
            cagir();
            dizi=1;
        }

        durdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oyunDurdu();
                sayaciDurdur();

            }
        });

    }

    private void cagir(){
        for (int i = 1; i < 100; i++){  //Kelime Sayısı Kadar
            sayi = (int) (Math.random() * 100);
            for (int j = 0; j <= i; j++){
                if (Sorusec[j] == sayi) {
                    sayi = (int) (Math.random() * 100);
                    j=0;
                }
            }
            Sorusec[i] = sayi;
        }

    }

    private void geriSayim(){

        new CountDownTimer(4000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                kelime4.setText(""+millisUntilFinished/1000);

            }

            @Override
            public void onFinish() {
                sayaciBaslat();
                yeniKelime();
                cagir();
            }
        }.start();
    }
}