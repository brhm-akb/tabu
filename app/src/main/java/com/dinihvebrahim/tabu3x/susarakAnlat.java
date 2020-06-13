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

import org.w3c.dom.Text;

import java.util.Locale;

public class susarakAnlat extends AppCompatActivity {
    private TextView takimIsmi;
    private TextView sonuc;
    private TextView kalanPas;
    private TextView Kelime;
    private Button sayac;

    public static long START_TIME_IN_MILLES = 60000;
    private CountDownTimer countDownTimer;
    private boolean sayacCalisiyorMu;
    private long timeLeftInMillis = START_TIME_IN_MILLES;

    private Button dogru2,pas2;

    int sonucA=0,sonucB=0,Sonuc=0;
    int KALANPAS=3;

    int sirakimde=0;
    int sayi,Sorusec[]=new int[100];

    int dizi=1,kelimeSayisi=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_susarak_anlat);

        UITasarimi();
        cagir();
        oyunBasliyor();

        Kelime=findViewById(R.id.Kelime);
        takimIsmi=findViewById(R.id.TakimIsmi);
        sonuc=findViewById(R.id.Sonuc);
        kalanPas=findViewById(R.id.KalanPas);
        sayac=findViewById(R.id.Sayac);
        dogru2=findViewById(R.id.dogru2);
        pas2=findViewById(R.id.pas2);

        dogru2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeniKelime();
                if(sirakimde%2==0){
                    //cift ise a takımı
                    Sonuc++;
                    sonucA++;
                    sonuc.setText("SONUÇ: "+Sonuc);
                }
                else{
                    //tek ise b takımı
                    Sonuc++;
                    sonucB++;
                    sonuc.setText("SONUÇ: "+Sonuc);
                }

            }
        });

        pas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KALANPAS==0){

                }
                else{
                    yeniKelime();
                    KALANPAS--;
                    kalanPas.setText("KALAN PAS: "+KALANPAS);
                }
            }
        });

        sayac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oyunDurdu();
                sayaciDurdur();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            oyunDurdu();
            sayaciDurdur();
        }

        return super.onKeyDown(keyCode, event);
    } // geri  tuşuna basılınca uyarı gelecek

    @Override
    protected void onStop() {
        super.onStop();
        oyunDurdu();
        sayaciDurdur();


    } // home tuşuna basınca oyun duracak

    @Override
    protected void onRestart() {
        super.onRestart();
        UITasarimi();
    } // oyun arka plana düştükten sonra oyun tekrar açılınca alt ve üstteki navigasyon bölmeleri gidecek

    private void oyunBasliyor(){

        View basla = getLayoutInflater().inflate(R.layout.basla,null);

        TextView ATAKIMI = basla.findViewById(R.id.takim1);
        TextView BTAKIMI = basla.findViewById(R.id.takim2);

        String atakimi = getIntent().getStringExtra("Takim_A");
        String btakimi = getIntent().getStringExtra("Takim_B");

        ATAKIMI.setText(atakimi);
        BTAKIMI.setText(btakimi);

        AlertDialog.Builder baslaAD = new AlertDialog.Builder(susarakAnlat.this);

        baslaAD.setView(basla);

        baslaAD.setCancelable(false);

        baslaAD.setPositiveButton("Başla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sayaciBaslat();
                yeniKelime();
                cagir();
                String aTakimi=getIntent().getStringExtra("Takim_A");
                takimIsmi.setText(aTakimi);
            }
        });

        baslaAD.create().show();


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

        sayac.setText(timeLeftFormatted);
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

        AlertDialog.Builder ad = new AlertDialog.Builder(susarakAnlat.this);

        ad.setView(sonuclar);

        ad.setCancelable(false);


        ad.setPositiveButton("Oyuna Devam Et", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sayaciTekrarEt();
                yeniKelime();
                UITasarimi();
                Sonuc=0;
                KALANPAS=3;
                sonuc.setText("SONUÇ: "+Sonuc);
                kalanPas.setText("KALAN PAS "+KALANPAS);
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

    private void oyunDurdu(){
        AlertDialog.Builder oyunDurdu = new AlertDialog.Builder(susarakAnlat.this);

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
                startActivity(new Intent(susarakAnlat.this,MainActivity.class));
                finish();
            }
        });
        oyunDurdu.create().show();

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


        dizi1[0]="Kitap";       dizi2[0]="Dergi";       dizi3[0]="Telefon";         dizi4[0]="Masa";        dizi5[0]="Sandalye";
        dizi6[0]="Kulaklık";    dizi7[0]="Dans";        dizi8[0]="Müzik";           dizi9[0]="Oynamak";     dizi10[0]="Saç";
        dizi11[0]="Ekmek";      dizi12[0]="Kablo";      dizi13[0]="Makas";          dizi14[0]="Abaküs";     dizi15[0]="Acı";
        dizi16[0]="Afiş";       dizi17[0]="Cımbız";     dizi18[0]="Gelgit";         dizi19[0]="Nişan";      dizi20[0]="Su";
        dizi21[0]="Ağıt";       dizi22[0]="Çıkarma";    dizi23[0]="Gitar";          dizi24[0]="Oruç";       dizi25[0]="Tabut";
        dizi26[0]="Akıl";       dizi27[0]="Çöp";        dizi28[0]="Gömlek";         dizi29[0]="Öksürmek";   dizi30[0]="Tavuk";
        dizi31[0]="Alçı";       dizi32[0]="Dırdır";     dizi33[0]="Gönül";          dizi34[0]="Parfüm";     dizi35[0]="Telefonla Konuşmak";
        dizi36[0]="Asker";      dizi37[0]="Diken";      dizi38[0]="Grip";           dizi39[0]="Peçete";     dizi40[0]="Tesbih";
        dizi41[0]="Ayna";       dizi42[0]="Direksyon";  dizi43[0]="Halay";          dizi44[0]="Pencere";    dizi45[0]="Toka";
        dizi46[0]="Bağlama";    dizi47[0]="Diş Fırçalama";dizi48[0]="Halı";         dizi49[0]="Pijama";     dizi50[0]="Top";
        dizi51[0]="Balık";      dizi52[0]="Düz";        dizi53[0]="Kıvırcık";       dizi54[0]="Işık";       dizi55[0]="Yatak";
        dizi56[0]="Banyo";      dizi57[0]="Eğri";       dizi58[0]="Koku";           dizi59[0]="Jöle";       dizi60[0]="Yorgan";
        dizi61[0]="Bardak";     dizi62[0]="Eldiven";    dizi63[0]="Komodin/Çekmece";dizi64[0]="Kaleci";     dizi65[0]="Daire";
        dizi66[0]="Basketbol";  dizi67[0]="Elektrik";   dizi68[0]="Kutu";           dizi69[0]="Kahvaltı";   dizi70[0]="Poşet";
        dizi71[0]="Batarya";    dizi72[0]="Emzik";      dizi73[0]="Masaj";          dizi74[0]="Pil";        dizi75[0]="Kel";
        dizi76[0]="Bayrak";     dizi77[0]="Eylem";      dizi78[0]="Maske";          dizi79[0]="Sabun";      dizi80[0]="Kelepçe";
        dizi81[0]="Bıyık";      dizi82[0]="Fermuar";    dizi83[0]="Maşa";           dizi84[0]="Saz";        dizi85[0]="Kemam";
        dizi86[0]="Bilgisayar"; dizi87[0]="Fiş";        dizi88[0]="Menü";           dizi89[0]="Selamlaşmak";dizi90[0]="Kemik";
        dizi91[0]="Buklu";      dizi92[0]="Gamze";      dizi93[0]="Mesaj";          dizi94[0]="Serum";      dizi95[0]="Keselemek";
        dizi96[0]="Cetvel";     dizi97[0]="Gazete";     dizi98[0]="Nefes";          dizi99[0]="Sigara";     dizi100[0]="Kırık";


        String Diziler[]={
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


        Kelime.setText(Diziler[Sorusec[dizi]]);

        kelimeSayisi++;
        dizi++;

        if(kelimeSayisi%99==0){ //Kelime Sayısından Bir Eksik
            cagir();
            dizi=1;
        }

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
}