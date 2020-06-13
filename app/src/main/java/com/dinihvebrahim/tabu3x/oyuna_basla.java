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

public class oyuna_basla extends AppCompatActivity {
    public static long START_TIME_IN_MILLES = 90000;
    private CountDownTimer countDownTimer;
    private boolean sayacCalisiyorMu;
    private long timeLeftInMillis = START_TIME_IN_MILLES;
    private Button durdur;

    private TextView takimIsmi,skor,pass;
    private TextView anaKelime,yasak1,yasak2,yasak3,yasak4,yasak5;

    private Button dogru,pas,yasak;

    int sonuc=0,sonucA=0,sonucB=0;
    int sirakimde=0;

    String diziler[][];
    int sayi,Sorusec[]=new int[501]; //Kelime Sayısı Kadar
    int dizi=1;
    int kelimeSayisi=1;
    int KALANPAS=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyuna_basla);


        UITasarimi();
        cagir();
        oyunBasliyor();

        takimIsmi=findViewById(R.id.takimIsmi);
        skor=findViewById(R.id.skor);
        pass=findViewById(R.id.KALANPAS);

        takimIsmi=findViewById(R.id.takimIsmi);
        anaKelime=findViewById(R.id.anaKelime);
        yasak1=findViewById(R.id.yasak1);
        yasak2=findViewById(R.id.yasak2);
        yasak3=findViewById(R.id.yasak3);
        yasak4=findViewById(R.id.yasak4);
        yasak5=findViewById(R.id.yasak5);

        dogru=findViewById(R.id.dogru);
        pas=findViewById(R.id.pas);
        yasak=findViewById(R.id.yasak);

        durdur=findViewById(R.id.durdur);

        dogru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeniKelime();
                if(sirakimde%2==0){
                    //cift ise a takımı
                    sonuc++;
                    sonucA++;
                    skor.setText("SONUÇ: "+sonuc);
                }
                else{
                    //tek ise b takımı
                    sonuc++;
                    sonucB++;
                    skor.setText("SONUÇ: "+sonuc);
                }

            }
        });

        pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KALANPAS==0){

                }
                else{
                    yeniKelime();
                    KALANPAS--;
                    pass.setText("KALAN PAS: "+KALANPAS);
                }
            }
        });

        yasak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeniKelime();
                if(sirakimde%2==0){
                    //cift ise a takımı
                    sonuc--;
                    sonucA--;
                    skor.setText("SONUÇ: "+sonuc);
                }
                else{
                    //tek ise b takımı
                    sonuc--;
                    sonucB--;
                    skor.setText("SONUÇ: "+sonuc);
                }

            }
        });

        durdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oyunDurdu();
                sayaciDurdur();
            }
        });

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

        AlertDialog.Builder baslaAD = new AlertDialog.Builder(oyuna_basla.this);

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


    }// oyun başlamadan önce hazır sekmesi gelecek

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

    private void oyunDurdu(){
        AlertDialog.Builder oyunDurdu = new AlertDialog.Builder(oyuna_basla.this);

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
                startActivity(new Intent(oyuna_basla.this,MainActivity.class));
                finish();
            }
        });
        oyunDurdu.create().show();

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

        AlertDialog.Builder ad = new AlertDialog.Builder(oyuna_basla.this);

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
                skor.setText("SONUÇ: "+sonuc);
                pass.setText("KALAN PAS "+KALANPAS);
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

        String dizi0[]= new String[6];  String dizi1[]= new String[6];  String dizi2[]= new String[6];  String dizi3[]= new String[6];  String dizi4[]= new String[6];
        String dizi5[]= new String[6];  String dizi6[]= new String[6];  String dizi7[]= new String[6];  String dizi8[]= new String[6];  String dizi9[]= new String[6];
        String dizi10[]= new String[6]; String dizi11[]= new String[6]; String dizi12[]= new String[6]; String dizi13[]= new String[6]; String dizi14[]= new String[6];
        String dizi15[]= new String[6]; String dizi16[]= new String[6]; String dizi17[]= new String[6]; String dizi18[]= new String[6]; String dizi19[]= new String[6];
        String dizi20[]= new String[6]; String dizi21[]= new String[6]; String dizi22[]= new String[6]; String dizi23[]= new String[6]; String dizi24[]= new String[6];
        String dizi25[]= new String[6]; String dizi26[]= new String[6]; String dizi27[]= new String[6]; String dizi28[]= new String[6]; String dizi29[]= new String[6];
        String dizi30[]= new String[6]; String dizi31[]= new String[6]; String dizi32[]= new String[6]; String dizi33[]= new String[6]; String dizi34[]= new String[6];
        String dizi35[]= new String[6]; String dizi36[]= new String[6]; String dizi37[]= new String[6]; String dizi38[]= new String[6]; String dizi39[]= new String[6];
        String dizi40[]= new String[6]; String dizi41[]= new String[6]; String dizi42[]= new String[6]; String dizi43[]= new String[6]; String dizi44[]= new String[6];
        String dizi45[]= new String[6]; String dizi46[]= new String[6]; String dizi47[]= new String[6]; String dizi48[]= new String[6]; String dizi49[]= new String[6];
        String dizi50[]= new String[6]; String dizi51[]= new String[6]; String dizi52[]= new String[6]; String dizi53[]= new String[6]; String dizi54[]= new String[6];
        String dizi55[]= new String[6]; String dizi56[]= new String[6]; String dizi57[]= new String[6]; String dizi58[]= new String[6]; String dizi59[]= new String[6];
        String dizi60[]= new String[6]; String dizi61[]= new String[6]; String dizi62[]= new String[6]; String dizi63[]= new String[6]; String dizi64[]= new String[6];
        String dizi65[]= new String[6]; String dizi66[]= new String[6]; String dizi67[]= new String[6]; String dizi68[]= new String[6]; String dizi69[]= new String[6];
        String dizi70[]= new String[6]; String dizi71[]= new String[6]; String dizi72[]= new String[6]; String dizi73[]= new String[6]; String dizi74[]= new String[6];
        String dizi75[]= new String[6]; String dizi76[]= new String[6]; String dizi77[]= new String[6]; String dizi78[]= new String[6]; String dizi79[]= new String[6];
        String dizi80[]= new String[6]; String dizi81[]= new String[6]; String dizi82[]= new String[6]; String dizi83[]= new String[6]; String dizi84[]= new String[6];
        String dizi85[]= new String[6]; String dizi86[]= new String[6]; String dizi87[]= new String[6]; String dizi88[]= new String[6]; String dizi89[]= new String[6];
        String dizi90[]= new String[6]; String dizi91[]= new String[6]; String dizi92[]= new String[6]; String dizi93[]= new String[6]; String dizi94[]= new String[6];
        String dizi95[]= new String[6]; String dizi96[]= new String[6]; String dizi97[]= new String[6]; String dizi98[]= new String[6]; String dizi99[]= new String[6];
        String dizi100[]= new String[6]; String dizi101[]= new String[6]; String dizi102[]= new String[6]; String dizi103[]= new String[6]; String dizi104[]= new String[6];
        String dizi105[]= new String[6]; String dizi106[]= new String[6]; String dizi107[]= new String[6]; String dizi108[]= new String[6]; String dizi109[]= new String[6];
        String dizi110[]= new String[6]; String dizi111[]= new String[6]; String dizi112[]= new String[6]; String dizi113[]= new String[6]; String dizi114[]= new String[6];
        String dizi115[]= new String[6]; String dizi116[]= new String[6]; String dizi117[]= new String[6]; String dizi118[]= new String[6]; String dizi119[]= new String[6];
        String dizi120[]= new String[6]; String dizi121[]= new String[6]; String dizi122[]= new String[6]; String dizi123[]= new String[6]; String dizi124[]= new String[6];
        String dizi125[]= new String[6]; String dizi126[]= new String[6]; String dizi127[]= new String[6]; String dizi128[]= new String[6]; String dizi129[]= new String[6];
        String dizi130[]= new String[6]; String dizi131[]= new String[6]; String dizi132[]= new String[6]; String dizi133[]= new String[6]; String dizi134[]= new String[6];
        String dizi135[]= new String[6]; String dizi136[]= new String[6]; String dizi137[]= new String[6]; String dizi138[]= new String[6]; String dizi139[]= new String[6];
        String dizi140[]= new String[6]; String dizi141[]= new String[6]; String dizi142[]= new String[6]; String dizi143[]= new String[6]; String dizi144[]= new String[6];
        String dizi145[]= new String[6]; String dizi146[]= new String[6]; String dizi147[]= new String[6]; String dizi148[]= new String[6]; String dizi149[]= new String[6];
        String dizi150[]= new String[6]; String dizi151[]= new String[6]; String dizi152[]= new String[6]; String dizi153[]= new String[6]; String dizi154[]= new String[6];
        String dizi155[]= new String[6]; String dizi156[]= new String[6]; String dizi157[]= new String[6]; String dizi158[]= new String[6]; String dizi159[]= new String[6];
        String dizi160[]= new String[6]; String dizi161[]= new String[6]; String dizi162[]= new String[6]; String dizi163[]= new String[6]; String dizi164[]= new String[6];
        String dizi165[]= new String[6]; String dizi166[]= new String[6]; String dizi167[]= new String[6]; String dizi168[]= new String[6]; String dizi169[]= new String[6];
        String dizi170[]= new String[6]; String dizi171[]= new String[6]; String dizi172[]= new String[6]; String dizi173[]= new String[6]; String dizi174[]= new String[6];
        String dizi175[]= new String[6]; String dizi176[]= new String[6]; String dizi177[]= new String[6]; String dizi178[]= new String[6]; String dizi179[]= new String[6];
        String dizi180[]= new String[6]; String dizi181[]= new String[6]; String dizi182[]= new String[6]; String dizi183[]= new String[6]; String dizi184[]= new String[6];
        String dizi185[]= new String[6]; String dizi186[]= new String[6]; String dizi187[]= new String[6]; String dizi188[]= new String[6]; String dizi189[]= new String[6];
        String dizi190[]= new String[6]; String dizi191[]= new String[6]; String dizi192[]= new String[6]; String dizi193[]= new String[6]; String dizi194[]= new String[6];
        String dizi195[]= new String[6]; String dizi196[]= new String[6]; String dizi197[]= new String[6]; String dizi198[]= new String[6]; String dizi199[]= new String[6];
        String dizi200[]= new String[6]; String dizi201[]= new String[6]; String dizi202[]= new String[6]; String dizi203[]= new String[6]; String dizi204[]= new String[6];
        String dizi205[]= new String[6]; String dizi206[]= new String[6]; String dizi207[]= new String[6]; String dizi208[]= new String[6]; String dizi209[]= new String[6];
        String dizi210[]= new String[6]; String dizi211[]= new String[6]; String dizi212[]= new String[6]; String dizi213[]= new String[6]; String dizi214[]= new String[6];
        String dizi215[]= new String[6]; String dizi216[]= new String[6]; String dizi217[]= new String[6]; String dizi218[]= new String[6]; String dizi219[]= new String[6];
        String dizi220[]= new String[6]; String dizi221[]= new String[6]; String dizi222[]= new String[6]; String dizi223[]= new String[6]; String dizi224[]= new String[6];
        String dizi225[]= new String[6]; String dizi226[]= new String[6]; String dizi227[]= new String[6]; String dizi228[]= new String[6]; String dizi229[]= new String[6];
        String dizi230[]= new String[6]; String dizi231[]= new String[6]; String dizi232[]= new String[6]; String dizi233[]= new String[6]; String dizi234[]= new String[6];
        String dizi235[]= new String[6]; String dizi236[]= new String[6]; String dizi237[]= new String[6]; String dizi238[]= new String[6]; String dizi239[]= new String[6];
        String dizi240[]= new String[6]; String dizi241[]= new String[6]; String dizi242[]= new String[6]; String dizi243[]= new String[6]; String dizi244[]= new String[6];
        String dizi245[]= new String[6]; String dizi246[]= new String[6]; String dizi247[]= new String[6]; String dizi248[]= new String[6]; String dizi249[]= new String[6];
        String dizi250[]= new String[6]; String dizi251[]= new String[6]; String dizi252[]= new String[6]; String dizi253[]= new String[6]; String dizi254[]= new String[6];
        String dizi255[]= new String[6]; String dizi256[]= new String[6]; String dizi257[]= new String[6]; String dizi258[]= new String[6]; String dizi259[]= new String[6];
        String dizi260[]= new String[6]; String dizi261[]= new String[6]; String dizi262[]= new String[6]; String dizi263[]= new String[6]; String dizi264[]= new String[6];
        String dizi265[]= new String[6]; String dizi266[]= new String[6]; String dizi267[]= new String[6]; String dizi268[]= new String[6]; String dizi269[]= new String[6];
        String dizi270[]= new String[6]; String dizi271[]= new String[6]; String dizi272[]= new String[6]; String dizi273[]= new String[6]; String dizi274[]= new String[6];
        String dizi275[]= new String[6]; String dizi276[]= new String[6]; String dizi277[]= new String[6]; String dizi278[]= new String[6]; String dizi279[]= new String[6];
        String dizi280[]= new String[6]; String dizi281[]= new String[6]; String dizi282[]= new String[6]; String dizi283[]= new String[6]; String dizi284[]= new String[6];
        String dizi285[]= new String[6]; String dizi286[]= new String[6]; String dizi287[]= new String[6]; String dizi288[]= new String[6]; String dizi289[]= new String[6];
        String dizi290[]= new String[6]; String dizi291[]= new String[6]; String dizi292[]= new String[6]; String dizi293[]= new String[6]; String dizi294[]= new String[6];
        String dizi295[]= new String[6]; String dizi296[]= new String[6]; String dizi297[]= new String[6]; String dizi298[]= new String[6]; String dizi299[]= new String[6];
        String dizi300[]= new String[6]; String dizi301[]= new String[6]; String dizi302[]= new String[6]; String dizi303[]= new String[6]; String dizi304[]= new String[6];
        String dizi305[]= new String[6]; String dizi306[]= new String[6]; String dizi307[]= new String[6]; String dizi308[]= new String[6]; String dizi309[]= new String[6];
        String dizi310[]= new String[6]; String dizi311[]= new String[6]; String dizi312[]= new String[6]; String dizi313[]= new String[6]; String dizi314[]= new String[6];
        String dizi315[]= new String[6]; String dizi316[]= new String[6]; String dizi317[]= new String[6]; String dizi318[]= new String[6]; String dizi319[]= new String[6];
        String dizi320[]= new String[6]; String dizi321[]= new String[6]; String dizi322[]= new String[6]; String dizi323[]= new String[6]; String dizi324[]= new String[6];
        String dizi325[]= new String[6]; String dizi326[]= new String[6]; String dizi327[]= new String[6]; String dizi328[]= new String[6]; String dizi329[]= new String[6];
        String dizi330[]= new String[6]; String dizi331[]= new String[6]; String dizi332[]= new String[6]; String dizi333[]= new String[6]; String dizi334[]= new String[6];
        String dizi335[]= new String[6]; String dizi336[]= new String[6]; String dizi337[]= new String[6]; String dizi338[]= new String[6]; String dizi339[]= new String[6];
        String dizi340[]= new String[6]; String dizi341[]= new String[6]; String dizi342[]= new String[6]; String dizi343[]= new String[6]; String dizi344[]= new String[6];
        String dizi345[]= new String[6]; String dizi346[]= new String[6]; String dizi347[]= new String[6]; String dizi348[]= new String[6]; String dizi349[]= new String[6];
        String dizi350[]= new String[6]; String dizi351[]= new String[6]; String dizi352[]= new String[6]; String dizi353[]= new String[6]; String dizi354[]= new String[6];
        String dizi355[]= new String[6]; String dizi356[]= new String[6]; String dizi357[]= new String[6]; String dizi358[]= new String[6]; String dizi359[]= new String[6];
        String dizi360[]= new String[6]; String dizi361[]= new String[6]; String dizi362[]= new String[6]; String dizi363[]= new String[6]; String dizi364[]= new String[6];
        String dizi365[]= new String[6]; String dizi366[]= new String[6]; String dizi367[]= new String[6]; String dizi368[]= new String[6]; String dizi369[]= new String[6];
        String dizi370[]= new String[6]; String dizi371[]= new String[6]; String dizi372[]= new String[6]; String dizi373[]= new String[6]; String dizi374[]= new String[6];
        String dizi375[]= new String[6]; String dizi376[]= new String[6]; String dizi377[]= new String[6]; String dizi378[]= new String[6]; String dizi379[]= new String[6];
        String dizi380[]= new String[6]; String dizi381[]= new String[6]; String dizi382[]= new String[6]; String dizi383[]= new String[6]; String dizi384[]= new String[6];
        String dizi385[]= new String[6]; String dizi386[]= new String[6]; String dizi387[]= new String[6]; String dizi388[]= new String[6]; String dizi389[]= new String[6];
        String dizi390[]= new String[6]; String dizi391[]= new String[6]; String dizi392[]= new String[6]; String dizi393[]= new String[6]; String dizi394[]= new String[6];
        String dizi395[]= new String[6]; String dizi396[]= new String[6]; String dizi397[]= new String[6]; String dizi398[]= new String[6]; String dizi399[]= new String[6];
        String dizi400[]= new String[6]; String dizi401[]= new String[6]; String dizi402[]= new String[6]; String dizi403[]= new String[6]; String dizi404[]= new String[6];
        String dizi405[]= new String[6]; String dizi406[]= new String[6]; String dizi407[]= new String[6]; String dizi408[]= new String[6]; String dizi409[]= new String[6];
        String dizi410[]= new String[6]; String dizi411[]= new String[6]; String dizi412[]= new String[6]; String dizi413[]= new String[6]; String dizi414[]= new String[6];
        String dizi415[]= new String[6]; String dizi416[]= new String[6]; String dizi417[]= new String[6]; String dizi418[]= new String[6]; String dizi419[]= new String[6];
        String dizi420[]= new String[6]; String dizi421[]= new String[6]; String dizi422[]= new String[6]; String dizi423[]= new String[6]; String dizi424[]= new String[6];
        String dizi425[]= new String[6]; String dizi426[]= new String[6]; String dizi427[]= new String[6]; String dizi428[]= new String[6]; String dizi429[]= new String[6];
        String dizi430[]= new String[6]; String dizi431[]= new String[6]; String dizi432[]= new String[6]; String dizi433[]= new String[6]; String dizi434[]= new String[6];
        String dizi435[]= new String[6]; String dizi436[]= new String[6]; String dizi437[]= new String[6]; String dizi438[]= new String[6]; String dizi439[]= new String[6];
        String dizi440[]= new String[6]; String dizi441[]= new String[6]; String dizi442[]= new String[6]; String dizi443[]= new String[6]; String dizi444[]= new String[6];
        String dizi445[]= new String[6]; String dizi446[]= new String[6]; String dizi447[]= new String[6]; String dizi448[]= new String[6]; String dizi449[]= new String[6];
        String dizi450[]= new String[6]; String dizi451[]= new String[6]; String dizi452[]= new String[6]; String dizi453[]= new String[6]; String dizi454[]= new String[6];
        String dizi455[]= new String[6]; String dizi456[]= new String[6]; String dizi457[]= new String[6]; String dizi458[]= new String[6]; String dizi459[]= new String[6];
        String dizi460[]= new String[6]; String dizi461[]= new String[6]; String dizi462[]= new String[6]; String dizi463[]= new String[6]; String dizi464[]= new String[6];
        String dizi465[]= new String[6]; String dizi466[]= new String[6]; String dizi467[]= new String[6]; String dizi468[]= new String[6]; String dizi469[]= new String[6];
        String dizi470[]= new String[6]; String dizi471[]= new String[6]; String dizi472[]= new String[6]; String dizi473[]= new String[6]; String dizi474[]= new String[6];
        String dizi475[]= new String[6]; String dizi476[]= new String[6]; String dizi477[]= new String[6]; String dizi478[]= new String[6]; String dizi479[]= new String[6];
        String dizi480[]= new String[6]; String dizi481[]= new String[6]; String dizi482[]= new String[6]; String dizi483[]= new String[6]; String dizi484[]= new String[6];
        String dizi485[]= new String[6]; String dizi486[]= new String[6]; String dizi487[]= new String[6]; String dizi488[]= new String[6]; String dizi489[]= new String[6];
        String dizi490[]= new String[6]; String dizi491[]= new String[6]; String dizi492[]= new String[6]; String dizi493[]= new String[6]; String dizi494[]= new String[6];
        String dizi495[]= new String[6]; String dizi496[]= new String[6]; String dizi497[]= new String[6]; String dizi498[]= new String[6]; String dizi499[]= new String[6];
        String dizi500[]= new String[6];









        dizi0[0]="Aşı";         dizi0[1]="Serum";       dizi0[2]="Tedavi";      dizi0[3]="Virüs";       dizi0[4]="Grip";            dizi0[5]="İlaç";
        dizi1[0]="Telefon";     dizi1[1]="Kamera";      dizi1[2]="Konuşmak";    dizi1[3]="Mesajlaşmak"; dizi1[4]="Graham Bell";     dizi1[5]="Aramak";
        dizi2[0]="Abaküs";      dizi2[1]="Boncuk";      dizi2[2]="İlkokul";     dizi2[3]="Matematik";   dizi2[4]="Toplama";         dizi2[5]="Ders";
        dizi3[0]="Abanmak";     dizi3[1]="Yaslanmak";   dizi3[2]="Zorlamak";    dizi3[3]="Çökmek";      dizi3[4]="Direnmek";        dizi3[5]="Vurmak";
        dizi4[0]="Abla";        dizi4[1]="Kardeş";      dizi4[2]="Kız";         dizi4[3]="Anne";        dizi4[4]="Yenge";           dizi4[5]="Çocuk";
        dizi5[0]="Adalet";      dizi5[1]="Hukuk";       dizi5[2]="Hak";         dizi5[3]="Adil";        dizi5[4]="Mahkeme";         dizi5[5]="Hakim";
        dizi6[0]="Adem";        dizi6[1]="İlk insan";   dizi6[2]="Peygamber";   dizi6[3]="Cennet";      dizi6[4]="Yokluk";          dizi6[5]="Hiç";
        dizi7[0]="Ağaç";        dizi7[1]="Fidan";       dizi7[2]="Orman";       dizi7[3]="Kağıt";       dizi7[4]="Bahçe";           dizi7[5]="Meyve";
        dizi8[0]="Akıl";        dizi8[1]="Bellek";      dizi8[2]="Zeka";        dizi8[3]="Kafa";        dizi8[4]="Hafıza";          dizi8[5]="Düşünme";
        dizi9[0]="Acı";         dizi9[1]="Biber";       dizi9[2]="Urfa";        dizi9[3]="Adana";       dizi9[4]="Yakmak";          dizi9[5]="Gerçek";
        dizi10[0]="Ad";         dizi10[1]="İsim";       dizi10[2]="Tanışma";    dizi10[3]="Kullanıcı";  dizi10[4]="Kimlik";         dizi10[5]="Telefon";
        dizi11[0]="Adsız";      dizi11[1]="İsimsiz";    dizi11[2]="Sahipsiz";   dizi11[3]="Kimlik";     dizi11[4]="Belirsiz";       dizi11[5]="Gizemli Kimse";
        dizi12[0]="Ağabey";     dizi12[1]="Erkek";      dizi12[2]="Kardeş";     dizi12[3]="Baba";       dizi12[4]="Çocuk";          dizi12[5]="Aile";
        dizi13[0]="Anten";      dizi13[1]="Uydu";       dizi13[2]="Televizyon"; dizi13[3]="Kumanda";    dizi13[4]="Kanal";          dizi13[5]="Kablo";
        dizi14[0]="Akraba";     dizi14[1]="Aile";       dizi14[2]="Kan Bağı";   dizi14[3]="Ben";        dizi14[4]="Soy";            dizi14[5]="Evlilik";
        dizi15[0]="Acun";       dizi15[1]="TV 8";       dizi15[2]="Ilıcalı";    dizi15[3]="Dünya";      dizi15[4]="Evren";          dizi15[5]="Para";
        dizi16[0]="Ahmak";      dizi16[1]="Aptal";      dizi16[2]="Bön";        dizi16[3]="Budala";     dizi16[4]="Akılsız";        dizi16[5]="Kötü Huylu";
        dizi17[0]="Akşam";      dizi17[1]="Ezan";       dizi17[2]="Gündüz";     dizi17[3]="Gece";       dizi17[4]="Yatsı";          dizi17[5]="Yemek";
        dizi18[0]="Ambar";      dizi18[1]="Yük";        dizi18[2]="Depo";       dizi18[3]="Fazlalık";   dizi18[4]="Stok";           dizi18[5]="Saklamak";
        dizi19[0]="Amblem";     dizi19[1]="Logo";       dizi19[2]="Temsil";     dizi19[3]="Forma";      dizi19[4]="Marka";          dizi19[5]="Simge";
        dizi20[0]="Arıza";      dizi20[1]="Tamir";      dizi20[2]="Aksaklık";   dizi20[3]="Bozukluk";   dizi20[4]="Sakatlık";       dizi20[5]="Tedavi";
        dizi21[0]="Aslan";      dizi21[1]="Orman";      dizi21[2]="Kral";       dizi21[3]="Hayvam";     dizi21[4]="İsim";           dizi21[5]="Galatasaray";
        dizi22[0]="Aydın";      dizi22[1]="Ege Bölgesi";dizi22[2]="Şehir";      dizi22[3]="Filozof";    dizi22[4]="Filozof";        dizi22[5]="Entelektüel";
        dizi23[0]="Aptes";      dizi23[1]="Namaz";      dizi23[2]="Ezan";       dizi23[3]="Cami";       dizi23[4]="İmam";           dizi23[5]="Vakit";
        dizi24[0]="Aciz";       dizi24[1]="Güçsüz";     dizi24[2]="Zayıf";      dizi24[3]="Beceriksiz"; dizi24[4]="Muhtaç";         dizi24[5]="Kabiliyetsiz";
        dizi25[0]="Afiş";       dizi25[1]="Reklam";     dizi25[2]="Duyuru";     dizi25[3]="Asmak";      dizi25[4]="Tabela";         dizi25[5]="Durak";
        dizi26[0]="Amir";       dizi26[1]="Üst";        dizi26[2]="Müdür";      dizi26[3]="Şef";        dizi26[4]="Yetkili";        dizi26[5]="Polis";
        dizi27[0]="Aziz";       dizi27[1]="Ermiş";      dizi27[2]="Muazzez";    dizi27[3]="Eren";       dizi27[4]="Sevgili";        dizi27[5]="Değerli";
        dizi28[0]="Ağıt";       dizi28[1]="Ağlamak";    dizi28[2]="Ölmek";      dizi28[3]="Acı";        dizi28[4]="Ezgi";           dizi28[5]="Mersiye";
        dizi29[0]="Aferin";     dizi29[1]="Tebrik";     dizi29[2]="Övme";       dizi29[3]="Takdir";     dizi29[4]="Beğenme";        dizi29[5]="Alkış";
        dizi30[0]="Açı";        dizi30[1]="Geometri";   dizi30[2]="Derece";     dizi30[3]="Dik";        dizi30[4]="Kesişim";        dizi30[5]="Görüş";
        dizi31[0]="Asker";      dizi31[1]="Şehit";      dizi31[2]="Gazi";       dizi31[3]="Savaşçı";    dizi31[4]="Ordu";           dizi31[5]="Subay";
        dizi32[0]="Ayna";       dizi32[1]="Cam";        dizi32[2]="Görüntü";    dizi32[3]="Saç Taramak";dizi32[4]="Diş Fırçalamak"; dizi32[5]="Kendimize Bakmak";
        dizi33[0]="Anı";        dizi33[1]="Hatıra";     dizi33[2]="Öykü";       dizi33[3]="Yaşanmışlık";dizi33[4]="Yadigar";        dizi33[5]="Olay";
        dizi34[0]="Ayak";       dizi34[1]="Organ";      dizi34[2]="Yürümek";    dizi34[3]="Parmak";     dizi34[4]="Terlik";         dizi34[5]="El";
        dizi35[0]="Affetmek";   dizi35[1]="Bağışlama";  dizi35[2]="Özür";       dizi35[3]="Pişmanlık";  dizi35[4]="Hoş Görmek";     dizi35[5]="Büyüklük";
        dizi36[0]="Alemdar";    dizi36[1]="Bayrak";     dizi36[2]="Sancak";     dizi36[3]="Önder";      dizi36[4]="Lider";          dizi36[5]="Polat";
        dizi37[0]="Alkol";      dizi37[1]="İçki";       dizi37[2]="Şarap";      dizi37[3]="Bira";       dizi37[4]="Kolonya";        dizi37[5]="Renksiz";
        dizi38[0]="Baba";       dizi38[1]="Anne";       dizi38[2]="Ağabey";     dizi38[3]="Evin Reisi"; dizi38[4]="Amca";           dizi38[5]="Enişte";
        dizi39[0]="Bacı";       dizi39[1]="Abla";       dizi39[2]="Karddeş";    dizi39[3]="Kız";        dizi39[4]="Hanım";          dizi39[5]="Kadın";
        dizi40[0]="Bagaj";      dizi40[1]="Yük";        dizi40[2]="Araba";      dizi40[3]="Arka Kısım"; dizi40[4]="Araç";           dizi40[5]="Kasa";
        dizi41[0]="Bağ";        dizi41[1]="Ayakkabı";   dizi41[2]="İp";         dizi41[3]="Üzüm";       dizi41[4]="Bahçe";          dizi41[5]="Sargı";
        dizi42[0]="Bağcık";     dizi42[1]="Ayakkabı";   dizi42[2]="İp";         dizi42[3]="Bağlamak";   dizi42[4]="Parmak";         dizi42[5]="Krampon";
        dizi43[0]="Bağış";      dizi43[1]="Yardım";     dizi43[2]="Toplamak";   dizi43[3]="Hibe";       dizi43[4]="Teberru";        dizi43[5]="Para";
        dizi44[0]="Bağlama";    dizi44[1]="Saz";        dizi44[2]="Türkü";      dizi44[3]="Aşık Veysel";dizi44[4]="Neşet Ertaş";    dizi44[5]="Söylemek";
        dizi45[0]="Bahar";      dizi45[1]="İlk";        dizi45[2]="Son";        dizi45[3]="Güz";        dizi45[4]="Papatya";        dizi45[5]="Gençlik Dönemi";
        dizi46[0]="Baharat";    dizi46[1]="Yemek";      dizi46[2]="Aktar";      dizi46[3]="Bitki";      dizi46[4]="Biber";          dizi46[5]="Tuz";
        dizi47[0]="Bahçe";      dizi47[1]="Ev";         dizi47[2]="Yeşillik";   dizi47[3]="Papatya";    dizi47[4]="Mangal";         dizi47[5]="Piknik";
        dizi48[0]="Bahçivan";   dizi48[1]="Bahçe";      dizi48[2]="Ev";         dizi48[3]="Çalışmak";   dizi48[4]="Villa";          dizi48[5]="Konak";
        dizi49[0]="Baht";       dizi49[1]="Talih";      dizi49[2]="Şans";       dizi49[3]="Nasip";      dizi49[4]="Kısmet";         dizi49[5]="Kader";
        dizi50[0]="Bakan";      dizi50[1]="Milletvekili"; dizi50[2]="Kabine";   dizi50[3]="Meclis";     dizi50[4]="Nazır";          dizi50[5]="Gören";
        dizi51[0]="Bakiye";     dizi51[1]="Bütçe";      dizi51[2]="Toplam";     dizi51[3]="Kalıntı";    dizi51[4]="Para";           dizi51[5]="Arta Kalan";
        dizi52[0]="Bakkal";     dizi52[1]="Dükkan";     dizi52[2]="Borç Defteri"; dizi52[3]="Market";   dizi52[4]="Satmak";         dizi52[5]="Abur-Cubur";
        dizi53[0]="Bal";        dizi53[1]="Arı";        dizi53[2]="Petek";      dizi53[3]="Kavanoz";    dizi53[4]="Tatlı";          dizi53[5]="İltifat";
        dizi54[0]="Baldır";     dizi54[1]="Bacak";      dizi54[2]="Yağlı";      dizi54[3]="Etli";       dizi54[4]="Yumuşak";        dizi54[5]="Arka Taraf";
        dizi55[0]="Balık";      dizi55[1]="Yunus";      dizi55[2]="Deniz";      dizi55[3]="Göl";        dizi55[4]="Yüzmek";         dizi55[5]="Tutmak";
        dizi56[0]="Balon";      dizi56[1]="Oyuncak";    dizi56[2]="Helyum";     dizi56[3]="Uçmak";      dizi56[4]="Cam Kap";        dizi56[5]="Boş Söz";
        dizi57[0]="Banka";      dizi57[1]="Para";       dizi57[2]="Ekonomi";    dizi57[3]="Faiz";       dizi57[4]="Kart";           dizi57[5]="Kredi";
        dizi58[0]="Banyo";      dizi58[1]="Yıkanmak";   dizi58[2]="Temizlenmek";dizi58[3]="Küvet";      dizi58[4]="Duşakabin";      dizi58[5]="Lavabo";
        dizi59[0]="Bardak";     dizi59[1]="Su";         dizi59[2]="Çay";        dizi59[3]="İçmek";      dizi59[4]="Tabak";          dizi59[5]="Pet";
        dizi60[0]="Barut";      dizi60[1]="Silah";      dizi60[2]="Mermi";      dizi60[3]="Bomba";      dizi60[4]="Patlayıcı";      dizi60[5]="TNT";
        dizi61[0]="Basketbol";  dizi61[1]="Futbol";     dizi61[2]="Pota";       dizi61[3]="3'lük";      dizi61[4]="Smaç";           dizi61[5]="Blok";
        dizi62[0]="Baş";        dizi62[1]="Kafa";       dizi62[2]="Yüz";        dizi62[3]="Temel";      dizi62[4]="Yönetici";       dizi62[5]="İlk yer";
        dizi63[0]="Başlık";     dizi63[1]="Para";       dizi63[2]="Kız İstemek";dizi63[3]="Şapka";      dizi63[4]="Külah";          dizi63[5]="Takke";
        dizi64[0]="Batur";      dizi64[1]="Yiğit";      dizi64[2]="Bahadır";    dizi64[3]="Kahraman";   dizi64[4]="Alp";            dizi64[5]="Üstünlük Kazanan";
        dizi65[0]="Bay";        dizi65[1]="Erkek";      dizi65[2]="Bey";        dizi65[3]="Adam";       dizi65[4]="Zengin";         dizi65[5]="Hitap";
        dizi66[0]="Bayan";      dizi66[1]="Kadın";      dizi66[2]="Kız";        dizi66[3]="Hanımefendi";dizi66[4]="Saygı";          dizi66[5]="Hitap";
        dizi67[0]="Bayram";     dizi67[1]="Kurban";     dizi67[2]="Ramazan";    dizi67[3]="Tatil";      dizi67[4]="Oruç";           dizi67[5]="Namaz";
        dizi68[0]="Bedava";     dizi68[1]="Ücretsiz";   dizi68[2]="Beleş";      dizi68[3]="Karşılıksız";dizi68[4]="Emeksiz";        dizi68[5]="Parasız";
        dizi69[0]="Bel";        dizi69[1]="Sırt";       dizi69[2]="Omurga";     dizi69[3]="Karın";      dizi69[4]="Kambur";         dizi69[5]="Fıtık";
        dizi70[0]="Bere";       dizi70[1]="Şapka";      dizi70[2]="Kafa";       dizi70[3]="Giymek";     dizi70[4]="Soğuk";          dizi70[5]="Başlık";
        dizi71[0]="Bez";        dizi71[1]="Temizlik";   dizi71[2]="Kumaş";      dizi71[3]="Örtü";       dizi71[4]="Çaput";          dizi71[5]="Pamuk";
        dizi72[0]="Bıçak";      dizi72[1]="Kesmek";     dizi72[2]="Jilet";      dizi72[3]="Keskin";     dizi72[4]="Öldürmek";       dizi72[5]="Saplamak";
        dizi73[0]="Boğulmak";   dizi73[1]="Yüzememek";  dizi73[2]="Deniz";      dizi73[3]="Simit";      dizi73[4]="Heyecanlanmak";  dizi73[5]="Nefes Alamamak";
        dizi74[0]="Bol";        dizi74[1]="Fazlasıyla"; dizi74[2]="Büyük";      dizi74[3]="Geniş";      dizi74[4]="Beden";          dizi74[5]="Olağandan Çok";
        dizi75[0]="Bozuk";      dizi75[1]="Tamir";      dizi75[2]="Çalışmayan"; dizi75[3]="İşlemeyen";  dizi75[4]="Kokmuş";         dizi75[5]="İşlevsiz";
        dizi76[0]="Baca";       dizi76[1]="Soba";       dizi76[2]="Duman";      dizi76[3]="Dam";        dizi76[4]="Şömine";         dizi76[5]="Ateş";
        dizi77[0]="Bıyık";      dizi77[1]="Pala";       dizi77[2]="Sakal";      dizi77[3]="Hilal";      dizi77[4]="Bıyık";          dizi77[5]="Ağız";
        dizi78[0]="Cadde";      dizi78[1]="Sokak";      dizi78[2]="Araba";      dizi78[3]="Yol";        dizi78[4]="Mahalle";        dizi78[5]="Adres";
        dizi79[0]="Cadı";       dizi79[1]="Huysuz";     dizi79[2]="Çirkin";     dizi79[3]="Kocakarı";   dizi79[4]="Büyücü";         dizi79[5]="Bakımsız";
        dizi80[0]="Cahil";      dizi80[1]="İlber Ortaylı";dizi80[2]="Bilmemiş"; dizi80[3]="Eğtimsiz";   dizi80[4]="Deneyimsiz";     dizi80[5]="Bilgisiz";
        dizi81[0]="Cambaz";     dizi81[1]="Hokkabaz";   dizi81[2]="Palyaço";    dizi81[3]="Sihirbaz";   dizi81[4]="Akrobat";        dizi81[5]="Hileci";
        dizi82[0]="Cami";       dizi82[1]="Namaz";      dizi82[2]="İbadethane"; dizi82[3]="Ayasofya";   dizi82[4]="Aptes";          dizi82[5]="İmam";
        dizi83[0]="Can";        dizi83[1]="Yaşam";      dizi83[2]="İsim";       dizi83[3]="Hayat";      dizi83[4]="İnsan";          dizi83[5]="İçten";
        dizi84[0]="Casus";      dizi84[1]="Ajan";       dizi84[2]="Gizmen";     dizi84[3]="Çaşıt";      dizi84[4]="Bilgi Aktarmak"; dizi84[5]="Gizli";
        dizi85[0]="Cehennem";   dizi85[1]="Sıcak";      dizi85[2]="Soğuk";      dizi85[3]="Ceza";       dizi85[4]="Ölüm";           dizi85[5]="Şeytan";
        dizi86[0]="Ceket";      dizi86[1]="Mont";       dizi86[2]="Kapşon";     dizi86[3]="Giysi";      dizi86[4]="Gömlek";         dizi86[5]="Elbise";
        dizi87[0]="Cem Yılmaz"; dizi87[1]="Aktör";      dizi87[2]="Oyuncu";     dizi87[3]="Stand-up";   dizi87[4]="Komedyen";       dizi87[5]="Yazar";
        dizi88[0]="Cemre";      dizi88[1]="Mart";       dizi88[2]="Dizi";       dizi88[3]="Nisan";      dizi88[4]="Sıcaklığın Yükselişi"; dizi88[5]="Hava";
        dizi89[0]="Cenaze";     dizi89[1]="Ölü";        dizi89[2]="Morg";       dizi89[3]="Taziye";     dizi89[4]="Tabut";          dizi89[5]="Mezar";
        dizi90[0]="Cennet";     dizi90[1]="Ödül";       dizi90[2]="Melek";      dizi90[3]="Firdevs";    dizi90[4]="Ölüm";           dizi90[5]="Şehit";
        dizi91[0]="Cerrah";     dizi91[1]="Doktor";     dizi91[2]="Ameliyat";   dizi91[3]="Beyin";      dizi91[4]="Operatör";       dizi91[5]="Neşter";
        dizi92[0]="Ceset";      dizi92[1]="Ölü";        dizi92[2]="Kan";        dizi92[3]="Naaş";       dizi92[4]="Kadavra";        dizi92[5]="Leş";
        dizi93[0]="Cesur";      dizi93[1]="Yürekli";    dizi93[2]="Korkusuz";   dizi93[3]="Yiğit";      dizi93[4]="Risk";           dizi93[5]="Dayanıklı";
        dizi94[0]="Cevap";      dizi94[1]="Yanıt";      dizi94[2]="Soru";       dizi94[3]="Problem";    dizi94[4]="Sonuç Vermek";   dizi94[5]="Karşılık";
        dizi95[0]="Ceylan";     dizi95[1]="Hayvan";     dizi95[2]="İsim";       dizi95[3]="Gazal";      dizi95[4]="Ceren";          dizi95[5]="Süzgün Olan";
        dizi96[0]="Ceza";       dizi96[1]="Suç";        dizi96[2]="Mahkeme";    dizi96[3]="Hapis Yatmak";dizi96[4]="Yaptırım";      dizi96[5]="Dostoyevski";
        dizi97[0]="Cezaevi";    dizi97[1]="Suçlu";      dizi97[2]="Gardiyan";   dizi97[3]="Hapishane";  dizi97[4]="Yargı";          dizi97[5]="Jandarma";
        dizi98[0]="Cımbız";     dizi98[1]="Kıl";        dizi98[2]="Küçük Maşa"; dizi98[3]="Tırnak Makası";dizi98[4]="En İnce";      dizi98[5]="Bakım";
        dizi99[0]="Cıva";       dizi99[1]="Hg";         dizi99[2]="Termometre"; dizi99[3]="Element";    dizi99[4]="Kimya";          dizi99[5]="Katı";
        dizi100[0]="Ciddi";     dizi100[1]="Şaka";      dizi100[2]="Gerçek";    dizi100[3]="Sağlam";    dizi100[4]="Önem Verilen";  dizi100[5]="Ağırbaşlı";
        dizi101[0]="Ciğer";     dizi101[1]="Organ";     dizi101[2]="Yürek";     dizi101[3]="İltifat";   dizi101[4]="Göğüs";         dizi101[5]="Korkusuz";
        dizi102[0]="Cimri";     dizi102[1]="Tutumlu";   dizi102[2]="Para Harcamayan";dizi102[3]="Birikimci";dizi102[4]="Kısmak";    dizi102[5]="Para";
        dizi103[0]="Cisim";     dizi103[1]="Madde";     dizi103[2]="Beden";     dizi103[3]="Gövde";     dizi103[4]="Vücut";         dizi103[5]="Somut";
        dizi104[0]="Coğrafya";  dizi104[1]="Ders";      dizi104[2]="Lise";      dizi104[3]="Yeryüzü";   dizi104[4]="Bilim Dalı";    dizi104[5]="Gökyüzü";
        dizi105[0]="Coşku";     dizi105[1]="Vecit";     dizi105[2]="Heyecan";   dizi105[3]="Sevinç";    dizi105[4]="Yücelmek";      dizi105[5]="Duygu";
        dizi106[0]="Cumhuriyet";dizi106[1]="29 Ekim";   dizi106[2]="Milletvekili";dizi106[3]="Seçim";   dizi106[4]="Parlemento";    dizi106[5]="Kendi Kendini Yönetmek";
        dizi107[0]="Cüzdan";    dizi107[1]="Para";      dizi107[2]="Kimlik";    dizi107[3]="Arka Cep";  dizi107[4]="Kart";          dizi107[5]="Çanta";
        dizi108[0]="Dağ";       dizi108[1]="Eğri";      dizi108[2]="Dik";       dizi108[3]="Yüksek";    dizi108[4]="Ağrı";          dizi108[5]="Everest";
        dizi109[0]="Dahil";     dizi109[1]="Hariç";     dizi109[2]="Birlikte";  dizi109[3]="Eklemek";   dizi109[4]="Katmak";        dizi109[5]="İçinde";
        dizi110[0]="Dal";       dizi110[1]="Ağaç";      dizi110[2]="Meyve";     dizi110[3]="Kuş";       dizi110[4]="Yaprak";        dizi110[5]="Odun";
        dizi111[0]="Dalga";     dizi111[1]="Deniz";     dizi111[2]="Gelgit";    dizi111[3]="Sörf";      dizi111[4]="Fizik";         dizi111[5]="Saç Modeli";
        dizi112[0]="Dalmak";    dizi112[1]="Yüzmek";    dizi112[2]="Hayal Kurma";dizi112[3]="Aniden Girme";dizi112[4]="Uyumak";     dizi112[5]="Kendinden Geçmek";
        dizi113[0]="Dama";      dizi113[1]="Satranç";   dizi113[2]="Oyun";      dizi113[3]="Tahta";     dizi113[4]="Taş";           dizi113[5]="Tüketmek";
        dizi114[0]="Damar";     dizi114[1]="Kan";       dizi114[2]="İğne";      dizi114[3]="Hemşire";   dizi114[4]="Serum";         dizi114[5]="Hastane";
        dizi115[0]="Dambıl";    dizi115[1]="Ağırlık";   dizi115[2]="Kaldırmak"; dizi115[3]="Halter";    dizi115[4]="Ekipman";       dizi115[5]="Fitness";
        dizi116[0]="Daktilo";   dizi116[1]="Yazmak";    dizi116[2]="Klavye";    dizi116[3]="Mürekkep";  dizi116[4]="Makine";        dizi116[5]="William Austin Burt";
        dizi117[0]="Darbe";     dizi117[1]="Asker";     dizi117[2]="Hükümet";   dizi117[3]="15 Temmuz"; dizi117[4]="Sarsıcı";       dizi117[5]="Devirmek";
        dizi118[0]="Dava";      dizi118[1]="Mahkeme";   dizi118[2]="Hakim";     dizi118[3]="Düşünce";   dizi118[4]="Ülkü";          dizi118[5]="Sorun";
        dizi119[0]="Davet";     dizi119[1]="Toplantı";  dizi119[2]="Çağrı";     dizi119[3]="İstemek";   dizi119[4]="Düğün";         dizi119[5]="Toplamak";
        dizi120[0]="Davul";     dizi120[1]="Zurna";     dizi120[2]="Düğün";     dizi120[3]="Eğlence";   dizi120[4]="Çalgı";         dizi120[5]="Halay";
        dizi121[0]="Dayak";     dizi121[1]="Ceza";      dizi121[2]="Dövmek";    dizi121[3]="Uzun Sopa"; dizi121[4]="Kol";           dizi121[5]="Destek";
        dizi122[0]="Defans";    dizi122[1]="Futbolcu";  dizi122[2]="Top";       dizi122[3]="Stadyum";   dizi122[4]="Kaleci";        dizi122[5]="Forvet";
        dizi123[0]="Değişmek";  dizi123[1]="Takas Yapmak";dizi123[2]="Alışveriş";dizi123[3]="Başkalaştırmak";dizi123[4]="İade";     dizi123[5]="Yer";
        dizi124[0]="Dekan";     dizi124[1]="Üniversite";dizi124[2]="Profesör";  dizi124[3]="Yönetmek";  dizi124[4]="Rektör";        dizi124[5]="Uzman";
        dizi125[0]="Deli";      dizi125[1]="Aklı Olmayan";dizi125[2]="Aşırı Taşkın";dizi125[3]="Cesur"; dizi125[4]="Çok Sevinmek";  dizi125[5]="Çok Üzülmek";
        dizi126[0]="Demet";     dizi126[1]="Işık";      dizi126[2]="Deste";     dizi126[3]="Çiçek";     dizi126[4]="Bağlam";        dizi126[5]="Sinir Telleri";
        dizi127[0]="Demir";     dizi127[1]="Fe";        dizi127[2]="Maden";     dizi127[3]="Sert";      dizi127[4]="Sağlam";        dizi127[5]="Pas";
        dizi128[0]="Demlik";    dizi128[1]="Çay";       dizi128[2]="Kap";       dizi128[3]="Porselen";  dizi128[4]="Semaver";       dizi128[5]="Bardak";
        dizi129[0]="Deniz";     dizi129[1]="Sahil";     dizi129[2]="Kumsal";    dizi129[3]="Okyanus";   dizi129[4]="Kıyı";          dizi129[5]="Gemi";
        dizi130[0]="Depo";      dizi130[1]="Ambar";     dizi130[2]="Eşya Saklamak";dizi130[3]="Biriktirmek";dizi130[4]="Korumak";   dizi130[5]="Yük";
        dizi131[0]="Depozito";  dizi131[1]="Ön Ödeme";  dizi131[2]="Güvence";   dizi131[3]="Garanti";   dizi131[4]="Kira";          dizi131[5]="Ev";
        dizi132[0]="Dernek";    dizi132[1]="Düğün";     dizi132[2]="Cemaat";    dizi132[3]="Cemiyet";   dizi132[4]="Topluluk";      dizi132[5]="Birleşme";
        dizi133[0]="Deve";      dizi133[1]="Çöl";       dizi133[2]="Kervan";    dizi133[3]="Eğri";      dizi133[4]="Yağ";           dizi133[5]="Su";
        dizi134[0]="Devşirme";  dizi134[1]="Yeniçeri Ocağı"; dizi134[2]="Gayrimüslim"; dizi134[3]="Toplamak"; dizi134[4]="Yetiştirmek";dizi134[5]="Turnacıbaşı";
        dizi135[0]="Diken";     dizi135[1]="Gül";       dizi135[2]="Çalı";      dizi135[3]="Sivri";     dizi135[4]="Batıcı";        dizi135[5]="Kirpi";
        dizi136[0]="Dil";       dizi136[1]="Ağız";      dizi136[2]="Uzun";      dizi136[3]="Anlaşmak";  dizi136[4]="Şive";          dizi136[5]="Konuşmak";
        dizi137[0]="Direk";     dizi137[1]="Tedaş";     dizi137[2]="ELektrik";  dizi137[3]="Ayak";      dizi137[4]="Sütun";         dizi137[5]="Kolon";
        dizi138[0]="Dirsek";    dizi138[1]="Kol";       dizi138[2]="Köşe";      dizi138[3]="Arpacık";   dizi138[4]="Açı";           dizi138[5]="Boru";
        dizi139[0]="Diyet";     dizi139[1]="Kilo Vermek";dizi139[2]="Perhiz";   dizi139[3]="Rejim";     dizi139[4]="Para Vermek";   dizi139[5]="Ceza";
        dizi140[0]="Düğün";     dizi140[1]="Dernek";    dizi140[2]="Davetiye";  dizi140[3]="Gelin-Damat";dizi140[4]="Oyun";         dizi140[5]="Pasta";
        dizi141[0]="Dün";       dizi141[1]="Bugün";     dizi141[2]="Yarın";     dizi141[3]="Geçmiş";    dizi141[4]="Kısa Süre Önce";dizi141[5]="Geride Kalmış";
        dizi142[0]="Düz";       dizi142[1]="Ova";       dizi142[2]="Çıkıntısız";dizi142[3]="Kıvrım";    dizi142[4]="Eğik Olmayan";  dizi142[5]="Yüksek";
        dizi143[0]="Ebe";       dizi143[1]="Hemşire";   dizi143[2]="Doğum";     dizi143[3]="Kadın";     dizi143[4]="Çocuk";         dizi143[5]="Hastane";
        dizi144[0]="Ebedi";     dizi144[1]="Sonsuz";    dizi144[2]="Ölümsüz";   dizi144[3]="Allah";     dizi144[4]="Soyut";         dizi144[5]="Canlı";
        dizi145[0]="Ebeveyn";   dizi145[1]="Anne";      dizi145[2]="Baba";      dizi145[3]="Aile";      dizi145[4]="Kardeş";        dizi145[5]="Sorumluluk";
        dizi146[0]="Eczane";    dizi146[1]="İlaç";      dizi146[2]="İğne";      dizi146[3]="Doktor";    dizi146[4]="Rapor";         dizi146[5]="Tedavi";
        dizi147[0]="Ego";       dizi147[1]="Bencillik"; dizi147[2]="Üstünlük";  dizi147[3]="Hodbinlik"; dizi147[4]="Ben";           dizi147[5]="Felsefi";
        dizi148[0]="Egzoz";     dizi148[1]="Araba";     dizi148[2]="Duman";     dizi148[3]="Benzin";    dizi148[4]="Boru";          dizi148[5]="Tekerlek";
        dizi149[0]="Eğitim";    dizi149[1]="Okul";      dizi149[2]="Ders";      dizi149[3]="Öğrenci";   dizi149[4]="Ahlak";         dizi149[5]="Terbiye";
        dizi150[0]="Eğitmen";   dizi150[1]="Öğretmen";  dizi150[2]="Okul";      dizi150[3]="Ders";      dizi150[4]="Bilgili";       dizi150[5]="Kurs";
        dizi151[0]="Eğri";      dizi151[1]="Düz";       dizi151[2]="Yay";       dizi151[3]="Doğru";     dizi151[4]="Eğimli";        dizi151[5]="Kırık";
        dizi152[0]="Eklem";     dizi152[1]="Kemik";     dizi152[2]="Mafsal";    dizi152[3]="Dirsek";    dizi152[4]="Diz";           dizi152[5]="Oynar";
        dizi153[0]="Ekmek";     dizi153[1]="Fırın";     dizi153[2]="Kepek";     dizi153[3]="Pide";      dizi153[4]="Yemek";         dizi153[5]="Buğday";
        dizi154[0]="Ekşi";      dizi154[1]="Limon";     dizi154[2]="Acı";       dizi154[3]="Tad";       dizi154[4]="Dil";           dizi154[5]="Bozulmuş";
        dizi155[0]="El";        dizi155[1]="Ayak";      dizi155[2]="Organ";     dizi155[3]="Parmak";    dizi155[4]="Tutmak";        dizi155[5]="Alkış";
        dizi156[0]="Elbise";    dizi156[1]="Giymek";    dizi156[2]="Tekstil";   dizi156[3]="Terzi";     dizi156[4]="Örtmek";        dizi156[5]="Mağaza";
        dizi157[0]="Eldiven";   dizi157[1]="Kış";       dizi157[2]="El";        dizi157[3]="Yün";       dizi157[4]="Deri";          dizi157[5]="Kumaş";
        dizi158[0]="Elek";      dizi158[1]="Ayırmak";   dizi158[2]="Un";        dizi158[3]="Kum";       dizi158[4]="Tel";           dizi158[5]="İncelemek";
        dizi159[0]="Elektrik";  dizi159[1]="Fizik";     dizi159[2]="Tedaş";     dizi159[3]="Direk";     dizi159[4]="Kablo";         dizi159[5]="Kontrol Kalemi";
        dizi160[0]="Emanet";    dizi160[1]="Güven";     dizi160[2]="Eşya Bırakmak";dizi160[3]="Otogar"; dizi160[4]="Havalimanı";    dizi160[5]="Depozito";
        dizi161[0]="Emeklilik"; dizi161[1]="65 Yaş";    dizi161[2]="Çalışmayı Bırakmak"; dizi161[3]="Yaşlılık"; dizi161[4]="Süresi Dolmak"; dizi161[5]="Kendine Vakit Ayırmak";
        dizi162[0]="Emir";      dizi162[1]="Buyruk";    dizi162[2]="KOmut";     dizi162[3]="Amir";      dizi162[4]="Üst";           dizi162[5]="Müdür";
        dizi163[0]="Emzik";     dizi163[1]="Ağlamak";   dizi163[2]="Bebek";     dizi163[3]="Biberon";   dizi163[4]="Süt";           dizi163[5]="Kauçuk";
        dizi164[0]="Enerji";    dizi164[1]="Güç";       dizi164[2]="Erke";      dizi164[3]="Elektrik";  dizi164[4]="Baraj";         dizi164[5]="Benzin";
        dizi165[0]="Engebe";    dizi165[1]="Dağ";       dizi165[2]="Ova";       dizi165[3]="Çukur";     dizi165[4]="Tümsek";        dizi165[5]="Güçlük";
        dizi166[0]="Enişte";    dizi166[1]="Erkek";     dizi166[2]="Damat";     dizi166[3]="Yenge";     dizi166[4]="Doblo";         dizi166[5]="Abla";
        dizi167[0]="Enkaz";     dizi167[1]="Deprem";    dizi167[2]="Yıkılma";   dizi167[3]="Çöküntü";   dizi167[4]="Döküntü";       dizi167[5]="Yıkıntı";
        dizi168[0]="Enteresan"; dizi168[1]="İlginç";    dizi168[2]="Dikkat Çekici";dizi168[3]="Değişik";dizi168[4]="Garip";         dizi168[5]="Alışagelmemiş";
        dizi169[0]="Ergen";     dizi169[1]="Çocuk";     dizi169[2]="Olgun";     dizi169[3]="Evlenmemiş";dizi169[4]="Erişkin";       dizi169[5]="Yaş";
        dizi170[0]="Erik";      dizi170[1]="Ekşi";      dizi170[2]="Yeşil";     dizi170[3]="Tuz";       dizi170[4]="Meyve";         dizi170[5]="Top";
        dizi171[0]="Erkek";     dizi171[1]="Kadın";     dizi171[2]="Adam";      dizi171[3]="Yetişkin";  dizi171[4]="Cinsiyet";      dizi171[5]="Koca";
        dizi172[0]="Esirgemek"; dizi172[1]="Kayırmak";  dizi172[2]="Korumak";   dizi172[3]="Göstermemek";dizi172[4]="Kaçırmak";     dizi172[5]="Kaçınmak";
        dizi173[0]="Eski";      dizi173[1]="Yeni";      dizi173[2]="Geçmiş";    dizi173[3]="Tarihi";    dizi173[4]="Uzun Süreli";   dizi173[5]="Yıpranmış";
        dizi174[0]="Espiri";    dizi174[1]="Komik";     dizi174[2]="Nükte";     dizi174[3]="İnce Anlam";dizi174[4]="Derin Anlam";   dizi174[5]="Düşündüren";
        dizi175[0]="Ev";        dizi175[1]="Yurt";      dizi175[2]="Aile";      dizi175[3]="Yaşamak";   dizi175[4]="Yatmak";        dizi175[5]="Oda";
        dizi176[0]="Evlat";     dizi176[1]="Çocuk";     dizi176[2]="Oğlan";     dizi176[3]="Kız";       dizi176[4]="Soy";           dizi176[5]="Hitap";
        dizi177[0]="Eylem";     dizi177[1]="Fiil";      dizi177[2]="Protesto";  dizi177[3]="Amel";      dizi177[4]="Aksiyon";       dizi177[5]="Hareket";
        dizi178[0]="Fenerbahçe";dizi178[1]="Lefter";    dizi178[2]="Alex";      dizi178[3]="Futbol";    dizi178[4]="Galatasaray";   dizi178[5]="Kadıköy";
        dizi179[0]="Fakir";     dizi179[1]="Parası Olmayan"; dizi179[2]="Yoksul";dizi179[3]="Yokluk";   dizi179[4]="Zengin";        dizi179[5]="Az Gelirli";
        dizi180[0]="Fes";       dizi180[1]="Şapka";     dizi180[2]="Kafa";      dizi180[3]="Osmanlı";   dizi180[4]="Başlık";        dizi180[5]="Sarık";
        dizi181[0]="Fert";      dizi181[1]="Birey";     dizi181[2]="Toplum";    dizi181[3]="Millet";    dizi181[4]="Varlık";        dizi181[5]="Tek";
        dizi182[0]="Fizik";     dizi182[1]="Ders";      dizi182[2]="Einstein";  dizi182[3]="Yer Çekimi";dizi182[4]="Sayısal";       dizi182[5]="Dış Görünüş";
        dizi183[0]="Facia";     dizi183[1]="Deprem";    dizi183[2]="Heyelan";   dizi183[3]="Tsunami";   dizi183[4]="Yıkım";         dizi183[5]="Bela";
        dizi184[0]="Faiz";      dizi184[1]="Banka";     dizi184[2]="Ekonomi";   dizi184[3]="Kredi";     dizi184[4]="Harcama";       dizi184[5]="Bela";
        dizi185[0]="Fincan";    dizi185[1]="Kahve";     dizi185[2]="Çay";       dizi185[3]="Porselen Takımı";dizi185[4]="Küçük Kap";dizi185[5]="Hediyelik";
        dizi186[0]="Fatiha";    dizi186[1]="Kuran'ı Kerim";dizi186[2]="İlk";    dizi186[3]="Sûre";      dizi186[4]="Ayet";          dizi186[5]="Namaz";
        dizi187[0]="Ferace";    dizi187[1]="Giysi";     dizi187[2]="Tesettür";  dizi187[3]="Hırka";     dizi187[4]="Mont";          dizi187[5]="Elbise";
        dizi188[0]="Frikik";    dizi188[1]="Futbol";    dizi188[2]="Faul";      dizi188[3]="Serbes Vuruş";dizi188[4]="Baraj";       dizi188[5]="Hakan Çalhanoğlu";
        dizi189[0]="Formül";    dizi189[1]="Kural";     dizi189[2]="Yöntem";    dizi189[3]="Cebir";     dizi189[4]="İfade";         dizi189[5]="Matematik";
        dizi190[0]="Fermuar";   dizi190[1]="Zincir";    dizi190[2]="Ceket";     dizi190[3]="Pantolon";  dizi190[4]="Diş";           dizi190[5]="Kumaş";
        dizi191[0]="Fetva";     dizi191[1]="Karar";     dizi191[2]="Emir";      dizi191[3]="Yanıt";     dizi191[4]="Hüküm";         dizi191[5]="Müftü";
        dizi192[0]="Felsefe";   dizi192[1]="Aristo";    dizi192[2]="Soru Sormak";dizi192[3]="Sorgulamak";dizi192[4]="Filozof";      dizi192[5]="Bilim";
        dizi193[0]="Fatih";     dizi193[1]="Galip";     dizi193[2]="Kazanan";   dizi193[3]="Fethetmek"; dizi193[4]="İstanbul";      dizi193[5]="Hükümdar";
        dizi194[0]="Fıkra";     dizi194[1]="Nasrettin Hoca";dizi194[2]="Komik"; dizi194[3]="Düşündürücü";dizi194[4]="Bölümce";      dizi194[5]="Gülmek";
        dizi195[0]="Fatih Terim"; dizi195[1]="Galatasaray";dizi195[2]="İmparator";dizi195[3]="Aslan";   dizi195[4]="Teknik Direktör";dizi195[5]="Antrenör";
        dizi196[0]="Fabrika";   dizi196[1]="İşçi";      dizi196[2]="Üretim";    dizi196[3]="Sanayi";    dizi196[4]="Madde";         dizi196[5]="Tüketim";
        dizi197[0]="Fesat";     dizi197[1]="Arabozucu"; dizi197[2]="Kötümser";  dizi197[3]="Kargaşa";   dizi197[4]="Karşılık";      dizi197[5]="Kötü Düşünce";
        dizi198[0]="Fırça";     dizi198[1]="Temizlik";  dizi198[2]="Boya";      dizi198[3]="Azar";      dizi198[4]="Paylama";       dizi198[5]="Kir Almak";
        dizi199[0]="Fazla";     dizi199[1]="Ekstra";    dizi199[2]="Bonus";     dizi199[3]="Aşkın";     dizi199[4]="Gereğinden Çok";dizi199[5]="Ölçüyü Kaçırmak";
        dizi200[0]="Far";       dizi200[1]="Işık";      dizi200[2]="Araba";     dizi200[3]="Araç";      dizi200[4]="Karanlık";      dizi200[5]="Akşam";
        dizi201[0]="Fön Makinesi";dizi201[1]="Saç Kurutmak"; dizi201[2]="Şekil Vermek";dizi201[3]="Kabartmak"; dizi201[4]="Banyo";  dizi201[5]="Maşa";
        dizi202[0]="Fayda";     dizi202[1]="Zarar";     dizi202[2]="Yarar";     dizi202[3]="Kâr";       dizi202[4]="Etki";          dizi202[5]="İşe Yaramak";
        dizi203[0]="Filozof";   dizi203[1]="Felsefe";   dizi203[2]="Sakin Kişi";dizi203[3]="Alim";      dizi203[4]="Düşünür";       dizi203[5]="Aristo";
        dizi204[0]="Fotoğraf";  dizi204[1]="Kamera";    dizi204[2]="Video";     dizi204[3]="Öz Çekim";  dizi204[4]="Kaydetmek";     dizi204[5]="Anı";
        dizi205[0]="Fotokopi";  dizi205[1]="Çıktı";     dizi205[2]="Renkli";    dizi205[3]="Renksiz";   dizi205[4]="PDF";           dizi205[5]="Kopya";
        dizi206[0]="Fark";      dizi206[1]="Üstün Olmak";dizi206[2]="Ayrım";    dizi206[3]="Toplamak";  dizi206[4]="Benzer";        dizi206[5]="Özellik";
        dizi207[0]="Fırın";     dizi207[1]="Ekmek";     dizi207[2]="Ramazan";   dizi207[3]="Yemek";     dizi207[4]="Pide";          dizi207[5]="Sıcak";
        dizi208[0]="Fiş";       dizi208[1]="Kablo";     dizi208[2]="Elektrik";  dizi208[3]="Priz";      dizi208[4]="Akım";          dizi208[5]="Sıra";
        dizi209[0]="Futbolcu";  dizi209[1]="Kaleci";    dizi209[2]="Defans";    dizi209[3]="Forvet";    dizi209[4]="Top";           dizi209[5]="Stadyum";
        dizi210[0]="Ferman";    dizi210[1]="Osmanlı";   dizi210[2]="Karar";     dizi210[3]="Buyruk";    dizi210[4]="Emir";          dizi210[5]="Tanzimat";
        dizi211[0]="Fosil";     dizi211[1]="Ölü";       dizi211[2]="Taşıl";     dizi211[3]="Kalıntı";   dizi211[4]="Geçmiş";        dizi211[5]="Arkeoloji";
        dizi212[0]="Fener";     dizi212[1]="Işık";      dizi212[2]="Elektrik";  dizi212[3]="Deniz";     dizi212[4]="Gemi";          dizi212[5]="Aydınlanmak";
        dizi213[0]="Gaip";      dizi213[1]="Yitik";     dizi213[2]="Kayıp";     dizi213[3]="Görünmez";  dizi213[4]="Bilinmez";      dizi213[5]="Hayali";
        dizi214[0]="Galaksi";   dizi214[1]="Samanyolu"; dizi214[2]="Gezegen";   dizi214[3]="Yıldızlar"; dizi214[4]="Güneş Sistemi"; dizi214[5]="Dünya";
        dizi215[0]="Galata";    dizi215[1]="İstanbul";  dizi215[2]="Kule";      dizi215[3]="Tarihi Eser";dizi215[4]="Köprü";        dizi215[5]="Beyoğlu";
        dizi216[0]="Galatasaray";dizi216[1]="Aslan";    dizi216[2]="Futbol";    dizi216[3]="Avrupa Fatihi";dizi216[4]="Fenerbahçe"; dizi216[5]="Ali Samiyen";
        dizi217[0]="Galibiyet"; dizi217[1]="Kazanmak";  dizi217[2]="Mağlubiyet";dizi217[3]="Zafer";     dizi217[4]="Yenme";         dizi217[5]="Yengi";
        dizi218[0]="Gamze";     dizi218[1]="Yüz";       dizi218[2]="Çukur";     dizi218[3]="Kız İsmi";  dizi218[4]="Gülümsemek";    dizi218[5]="Yanak";
        dizi219[0]="Garson";    dizi219[1]="Lokanta";   dizi219[2]="Hizmet";    dizi219[3]="Otel";      dizi219[4]="Müşteri";       dizi219[5]="Kahvehane";
        dizi220[0]="Gayri";     dizi220[1]="Resmi";     dizi220[2]="Ayrı";      dizi220[3]="Başka";     dizi220[4]="Öteki";         dizi220[5]="Artık";
        dizi221[0]="Gaz";       dizi221[1]="Kimya";     dizi221[2]="Yellenmek"; dizi221[3]="Mazot";     dizi221[4]="Katı";          dizi221[5]="Sıvı";
        dizi222[0]="Gazete";    dizi222[1]="Manşet";    dizi222[2]="Magazin";   dizi222[3]="Bulmaca";   dizi222[4]="Kupon";         dizi222[5]="Dergi";
        dizi223[0]="Gazi";      dizi223[1]="Şehit";     dizi223[2]="Yaralı";    dizi223[3]="Asker";     dizi223[4]="Gaza";          dizi223[5]="Direniş";
        dizi224[0]="Gelecek";   dizi224[1]="Geçmiş";    dizi224[2]="İstikbal";  dizi224[3]="İlerisi";   dizi224[4]="Bilinmeyen Zaman";dizi224[5]="Bugün";
        dizi225[0]="Gelin";     dizi225[1]="Damat";     dizi225[2]="Düğün";     dizi225[3]="Halay";     dizi225[4]="Takı";          dizi225[5]="Pasta";
        dizi226[0]="Gelir";     dizi226[1]="Gider";     dizi226[2]="Maaş";      dizi226[3]="Para";      dizi226[4]="Ekonomi";       dizi226[5]="Vardiat";
        dizi227[0]="Gen";       dizi227[1]="DNA";       dizi227[2]="Biyoloji";  dizi227[3]="Kuşak";     dizi227[4]="Soy";           dizi227[5]="Hücre";
        dizi228[0]="Geniş";     dizi228[1]="Dar";       dizi228[2]="Bol";       dizi228[3]="Çok";       dizi228[4]="Tasasız";       dizi228[5]="Rahat";
        dizi229[0]="Geometri";  dizi229[1]="Matematik"; dizi229[2]="Çokgen";    dizi229[3]="Ders";      dizi229[4]="Alan";          dizi229[5]="Çevre";
        dizi230[0]="Gevşek";    dizi230[1]="Rahat";     dizi230[2]="İlgizis";   dizi230[3]="Geveze";    dizi230[4]="İlgisiz";       dizi230[5]="Sıkı";
        dizi231[0]="Gıda";      dizi231[1]="Yemek";     dizi231[2]="Besin";     dizi231[3]="Yiyecek";   dizi231[4]="Baklagil";      dizi231[5]="Sebze";
        dizi232[0]="Gıybet";    dizi232[1]="Dedikodu";  dizi232[2]="Kötüleme";  dizi232[3]="Yerme";     dizi232[4]="Çekiştirme";    dizi232[5]="Konuşmak";
        dizi233[0]="Gider";     dizi233[1]="Gelir";     dizi233[2]="Harcama";   dizi233[3]="Harcama";   dizi233[4]="Masraf";        dizi233[5]="Maaş";
        dizi234[0]="Gitar";     dizi234[1]="Haluk Levent";dizi234[2]="Şarkı";   dizi234[3]="Çalgı";     dizi234[4]="Keman";         dizi213[5]="Konser";
        dizi235[0]="Giysi";     dizi235[1]="Elbise";    dizi235[2]="Kazak";     dizi235[3]="Pantolon";  dizi235[4]="Çeket";         dizi235[5]="Mont";
        dizi236[0]="Gizemli";   dizi236[1]="Sır";       dizi236[2]="Esrarengiz";dizi236[3]="Giz Dolu";  dizi236[4]="Bilinmeyen";    dizi236[5]="Saklı";
        dizi237[0]="Gol";       dizi237[1]="Futbol";    dizi237[2]="Şut";       dizi237[3]="Direk";     dizi237[4]="Kaleci";        dizi237[5]="Futbolcu";
        dizi238[0]="Göç";       dizi238[1]="Taşınmak";  dizi238[2]="Gurbet";    dizi238[3]="Mevsim Değişikliği";dizi238[4]="Beyin"; dizi238[5]="Yerleşmek";
        dizi239[0]="Gömlek";    dizi239[1]="Ütü";       dizi239[2]="Takım Elbise";dizi239[3]="Kravat";  dizi239[4]="Yakalı";        dizi239[5]="Giysi";
        dizi240[0]="Gönül";     dizi240[1]="Kalp";      dizi240[2]="Yürek";     dizi240[3]="Sevgi";     dizi240[4]="İç Dünya";      dizi240[5]="Sevda";
        dizi241[0]="Görev";     dizi241[1]="Sorumluluk";dizi241[2]="İşlev";     dizi241[3]="Fonksiyon"; dizi241[4]="Vazife";        dizi241[5]="Üstlenmek";
        dizi242[0]="Gözlük";    dizi242[1]="Takmak";    dizi242[2]="Numara";    dizi242[3]="Bozukluk";  dizi242[4]="Hastalık";      dizi242[5]="Miyop";
        dizi243[0]="Grip";      dizi243[1]="Virüs";     dizi243[2]="Kış";       dizi243[3]="İğne";      dizi243[4]="Nezle";         dizi243[5]="Soğuk Algınlığı";
        dizi244[0]="Gurur";     dizi244[1]="Şeref";     dizi244[2]="Onur";      dizi244[3]="Kurum";     dizi244[4]="Övünme";        dizi244[5]="Büyüklenme";
        dizi245[0]="Gül";       dizi245[1]="Çiçek";     dizi245[2]="Papatya";   dizi245[3]="Kokulu";    dizi245[4]="Tebessüm";      dizi245[5]="Bitki";
        dizi246[0]="Güreş";     dizi246[1]="Yağ";       dizi246[2]="Er Meydanı";dizi246[3]="Edirne";    dizi246[4]="Spor";          dizi246[5]="Minder";
        dizi247[0]="Haber";     dizi247[1]="Olay";      dizi247[2]="Bilgi";     dizi247[3]="Duyurmak";  dizi247[4]="Bildirmek";     dizi247[5]="Ajans";
        dizi248[0]="Hacı";      dizi248[1]="Kabe";      dizi248[2]="Mekke";     dizi248[3]="Medine";    dizi248[4]="Hac";           dizi248[5]="Kurban Bayramı";
        dizi249[0]="Haciz";     dizi249[1]="Borç";      dizi249[2]="İcra";      dizi249[3]="Banla";     dizi249[4]="El Koymak";     dizi249[5]="Para";
        dizi250[0]="Hafız";     dizi250[1]="Ezberlemek";dizi250[2]="Hafıza";    dizi250[3]="Kuran'ı Kerim";dizi250[4]="koruyan";    dizi250[5]="Gözeten";
        dizi251[0]="Hak";       dizi251[1]="Hukuk";     dizi251[2]="Adalet";    dizi251[3]="Pay";       dizi251[4]="Doğru";         dizi251[5]="Gerçek";
        dizi252[0]="Hakem";     dizi252[1]="Maç";       dizi252[2]="Karşılaşma";dizi252[3]="Yargıç";    dizi252[4]="Yönetmek";      dizi252[5]="Bilirkişi";
        dizi253[0]="Hakim";     dizi253[1]="Mahkeme";   dizi253[2]="Avukat";    dizi253[3]="Savcı";     dizi253[4]="Bilge";         dizi253[5]="Yargıç";
        dizi254[0]="Halay";     dizi254[1]="Düğün";     dizi254[2]="Oynamak";   dizi254[3]="Davul";     dizi254[4]="Zurna";         dizi254[5]="Kol Kola";
        dizi255[0]="Halk";      dizi255[1]="Millet";    dizi255[2]="Topluluk";  dizi255[3]="Soy";       dizi255[4]="Yaşamak";       dizi255[5]="İnsanlar";
        dizi256[0]="Haluk Levent";dizi256[1]="Ahbap";   dizi256[2]="Giter";     dizi256[3]="Sanatçı";   dizi256[4]="Anadolu Rak";   dizi256[5]="Akdeniz Akşamları";
        dizi257[0]="Huzur";     dizi257[1]="Erinç";     dizi257[2]="Rahat";     dizi257[3]="Hazır Bulunma";dizi257[4]="Dinginlik";  dizi257[5]="Gönül Rahatlığı";
        dizi258[0]="Hamsi";     dizi258[1]="Trabzon";   dizi258[2]="Balık";     dizi258[3]="Tutmak";    dizi258[4]="Deniz";         dizi258[5]="Tava";
        dizi259[0]="Hangar";    dizi259[1]="Ambar";     dizi259[2]="Depo";      dizi259[3]="Uçak";      dizi259[4]="Büyük Yer";     dizi259[5]="Araba";
        dizi260[0]="Hanım";     dizi260[1]="Efendi";    dizi260[2]="Kadın";     dizi260[3]="Eş ";       dizi260[4]="Hitap";         dizi260[5]="İsim";
        dizi261[0]="Hapis";     dizi261[1]="Tutuklu";   dizi261[2]="Ceza";      dizi261[3]="Elde Tutma";dizi261[4]="Mahkeme";       dizi261[5]="Tavla";
        dizi262[0]="Hapşırma";  dizi262[1]="Yellenmek"; dizi262[2]="Hıçkırmak"; dizi262[3]="Ağız";      dizi262[4]="Aksırmak";      dizi262[5]="Burun";
        dizi263[0]="Harf";      dizi263[1]="Kelime";    dizi263[2]="Hece";      dizi263[3]="Cümle";     dizi263[4]="Alfabe";        dizi263[5]="Paragraf";
        dizi264[0]="Hasat";     dizi264[1]="Bahçe";     dizi264[2]="Toplamak";  dizi264[3]="Ürün";      dizi264[4]="Orak";          dizi264[5]="Tırpan";
        dizi265[0]="Hastane";   dizi265[1]="Doktor";    dizi265[2]="Hemşire";   dizi265[3]="Polokilinik";dizi265[4]="Sıra";         dizi265[5]="Muayene";
        dizi266[0]="Hava";      dizi266[1]="Rüzgar";    dizi266[2]="Nefes";     dizi266[3]="Oksijen";   dizi266[4]="Esinti";        dizi266[5]="Ortam";
        dizi267[0]="Havalimanı";dizi267[1]="Uçak";      dizi267[2]="Seyehat";   dizi267[3]="Kaptan";    dizi267[4]="Bilet";         dizi267[5]="Hostes";
        dizi268[0]="Hayal";     dizi268[1]="Rüya";      dizi268[2]="Düş";       dizi268[3]="İmge";      dizi268[4]="Gerçekleşmemiş";dizi268[5]="Dalmak";
        dizi269[0]="Hazine";    dizi269[1]="Altın";     dizi269[2]="Kazmak";    dizi269[3]="Harita";    dizi269[4]="Mücevher";      dizi269[5]="Saklamak";
        dizi270[0]="Helikopter";dizi270[1]="Uçak";      dizi270[2]="Uçmak";     dizi270[3]="Asker";     dizi270[4]="Pervane";       dizi270[5]="Kanat";
        dizi271[0]="Hırs";      dizi271[1]="Sonu Gelmez";dizi271[2]="Tutku";    dizi271[3]="Kızgınlık"; dizi271[4]="Öfke";          dizi271[5]="Aşırı İstek";
        dizi272[0]="Hız";       dizi272[1]="Sürat";     dizi272[2]="İvme";      dizi272[3]="Gaza Basmak";dizi272[4]="Çabuk";        dizi272[5]="Kaza";
        dizi273[0]="Hicret";    dizi273[1]="Göç";       dizi273[2]="Taşınmak";  dizi273[3]="Gitmek";    dizi273[4]="Takvim";        dizi273[5]="Hz. Muhammed";
        dizi274[0]="Hikaye";    dizi274[1]="Öykü";      dizi274[2]="Anı";       dizi274[3]="Olay";      dizi274[4]="Edebiyat";      dizi274[5]="Roman";
        dizi275[0]="Hile";      dizi275[1]="Dolap";     dizi275[2]="Düzen";     dizi275[3]="Yalan";     dizi275[4]="Çıkar";         dizi275[5]="Avantaj";
        dizi276[0]="Hizmet";    dizi276[1]="İş Yapma";  dizi276[2]="Görev";     dizi276[3]="Bakım";     dizi276[4]="Çalışma";       dizi276[5]="Yardım";
        dizi277[0]="Hoparlör";  dizi277[1]="Ses";       dizi277[2]="Kulaklık";  dizi277[3]="Radyo";     dizi277[4]="Konuşmak";      dizi277[5]="Duymak";
        dizi278[0]="Hukuk";     dizi278[1]="Adalet";    dizi278[2]="Avukat";    dizi278[3]="Yasa";      dizi278[4]="Hak";           dizi278[5]="Okul";
        dizi279[0]="Ihlamur";   dizi279[1]="Çay";       dizi279[2]="Papatya";   dizi279[3]="Limon";     dizi279[4]="Çiçek";         dizi279[5]="Soğuk Algınlığı";
        dizi280[0]="Ilık";      dizi280[1]="Sıcak";     dizi280[2]="Soğuk";     dizi280[3]="Kaynar";    dizi280[4]="Eşit";          dizi280[5]="Denge";
        dizi281[0]="Irak";      dizi281[1]="Uzak";      dizi281[2]="Yakın";     dizi281[3]="İran";      dizi281[4]="Sınır";         dizi281[5]="Komşu";
        dizi282[0]="Isırgan";   dizi282[1]="Bitki";     dizi282[2]="Kaşındırıcı";dizi282[3]="Sert Tüylü";dizi282[4]="Yakıcı";       dizi282[5]="Etçil";
        dizi283[0]="Islak";     dizi283[1]="Kuru";      dizi283[2]="Yaş";       dizi283[3]="Mendil";    dizi283[4]="Su";            dizi283[5]="Bez";
        dizi284[0]="Israr";     dizi284[1]="Direnmek";  dizi284[2]="Dayatmak";  dizi284[3]="Çok İstemek";dizi284[4]="Üsteleme";     dizi284[5]="Üstünde Çok Durma";
        dizi285[0]="Issız";     dizi285[1]="Yalnız";    dizi285[2]="Tenha";     dizi285[3]="Ara Sokak"; dizi285[4]="Yaban";         dizi285[5]="Bilinmeyen Yer";
        dizi286[0]="Işık";      dizi286[1]="Erke";      dizi286[2]="Aydınlık";  dizi286[3]="Lamba";     dizi286[4]="Ampül";         dizi286[5]="Fener";
        dizi287[0]="Ilımlı";    dizi287[1]="Ölçülü";    dizi287[2]="Mutedil";   dizi287[3]="İtidal";    dizi287[4]="Ölçülü";        dizi287[5]="Orta Yol";
        dizi288[0]="Izgara";    dizi288[1]="Mangal";    dizi288[2]="Et";        dizi288[3]="Balık";     dizi288[4]="Pişirim";       dizi288[5]="Ateş";
        dizi289[0]="İade";      dizi289[1]="Geri Verme";dizi289[2]="Mukabele";  dizi289[3]="Reddetme";  dizi289[4]="Sipariş";       dizi289[5]="Karşılığını Verme";
        dizi290[0]="İbrik";     dizi290[1]="Alüminyum"; dizi290[2]="Su";        dizi290[3]="Kulp";      dizi290[4]="Çeşme";         dizi290[5]="Taşımak";
        dizi291[0]="İçki";      dizi291[1]="Alkol";     dizi291[2]="Şarap";     dizi291[3]="Sarhoşluk"; dizi291[4]="İrade Kaybı";   dizi291[5]="Dengesizlik";
        dizi292[0]="İçlik";     dizi292[1]="Kış";       dizi292[2]="Sıcak";     dizi292[3]="Pantolon";  dizi292[4]="Termal";        dizi292[5]="yün";
        dizi293[0]="İddia";     dizi293[1]="Yarışmak";  dizi293[2]="Amaç";      dizi293[3]="Erek";      dizi293[4]="Sav";           dizi293[5]="Tez";
        dizi294[0]="İdman";     dizi294[1]="Antreman";  dizi294[2]="Isınmak";   dizi294[3]="Alışmak";   dizi294[4]="Futbol";        dizi294[5]="Spor Tesisleri";
        dizi295[0]="İdrak";     dizi295[1]="Anlayış";   dizi295[2]="Kavrayış";  dizi295[3]="Kavuşma";   dizi295[4]="Ulaşma";        dizi295[5]="Algı";
        dizi296[0]="İfşa";      dizi296[1]="Yayma";     dizi296[2]="Açığa Vurmak";dizi296[3]="Açıklama";dizi296[4]="Ortaya Dökme";  dizi296[5]="Gizlilik";
        dizi297[0]="İğne";      dizi297[1]="İp";        dizi297[2]="Toplu";     dizi297[3]="Şiş";       dizi297[4]="Dikiş";         dizi297[5]="Sivri";
        dizi298[0]="İhtişam";   dizi298[1]="Büyüklük";  dizi298[2]="Ululuk";    dizi298[3]="Gözalıcılık";dizi298[4]="Görkem";       dizi298[5]="Gösteriş";
        dizi299[0]="İkindi";    dizi299[1]="öğle";      dizi299[2]="Akşam";     dizi299[3]="İmsakiye";  dizi299[4]="Güneş";         dizi299[5]="Ezan";
        dizi300[0]="İkizler";   dizi300[1]="Burç";      dizi300[2]="Kardeş";    dizi300[3]="Yumurta";   dizi300[4]="Üçüz";          dizi300[5]="Benzerlik";
        dizi301[0]="İl";        dizi301[1]="Şehir";     dizi301[2]="İlçe";      dizi301[3]="Belediye";  dizi301[4]="Köy";           dizi301[5]="Valilik";
        dizi302[0]="İlber Ortaylı";dizi302[1]="Tarih";  dizi302[2]="Osmanlı";   dizi302[3]="Konuşmacı"; dizi302[4]="Celal Şengör";  dizi302[5]="Akademisyen";
        dizi303[0]="İllüzyon";  dizi303[1]="Sihirbaz";  dizi303[2]="Yanılsama"; dizi303[3]="Gözbağı";   dizi303[4]="Aref";          dizi303[5]="Atdatmaca";
        dizi304[0]="İmrenmek";  dizi304[1]="Kıskanmak"; dizi304[2]="Çok İstemek";dizi304[3]="Hoşlanmak";dizi304[4]="Gıpta Etmek";   dizi304[5]="Benzemeyi İstemek";
        dizi305[0]="İmsak";     dizi305[1]="Ezan";      dizi305[2]="Ramazan";   dizi305[3]="Sabah";     dizi305[4]="Akşam";         dizi305[5]="Sahur";
        dizi306[0]="İmza";      dizi306[1]="Yazar";     dizi306[2]="Kitap";     dizi306[3]="Belge";     dizi306[4]="Benzersiz";     dizi306[5]="Şahsa Özel";
        dizi307[0]="İnat";      dizi307[1]="Diretme";   dizi307[2]="Karşı Çıkma";dizi307[3]="Terslik";  dizi307[4]="Aykırılık";     dizi307[5]="Direnim";
        dizi308[0]="İnce";      dizi308[1]="Kalın";     dizi308[2]="Dar";       dizi308[3]="Uzun";      dizi308[4]="Cılız";         dizi308[5]="Zarif";
        dizi309[0]="İnci";      dizi309[1]="Elmas";     dizi309[2]="Altın";     dizi309[3]="İstiridye"; dizi309[4]="Deniz";         dizi309[5]="Süs";
        dizi310[0]="İnsan";     dizi310[1]="Birey";     dizi310[2]="Beşer";     dizi310[3]="Âdemoğlu";  dizi310[4]="Canlı";         dizi310[5]="Varlık";
        dizi311[0]="İp";        dizi311[1]="İğne";      dizi311[2]="Böcek";     dizi311[3]="Bağlamak";  dizi311[4]="İdam";          dizi311[5]="Boğmak";
        dizi312[0]="İsmet Özel";dizi312[1]="Şair";      dizi312[2]="Yazar";     dizi312[3]="Konuşmacı"; dizi312[4]="Türklük";       dizi312[5]="istiklal Marşı Derneği";
        dizi313[0]="İsot";      dizi313[1]="Biber";     dizi313[2]="Şanlıurfa"; dizi313[3]="Acı";       dizi313[4]="Toz";           dizi313[5]="Çiğ Köfte";
        dizi314[0]="İstihdam";  dizi314[1]="Çalıştırma";dizi314[2]="Görevlendirme";dizi314[3]="İş Alanı";dizi314[4]="Ekonomi";      dizi314[5]="İşlendirme";
        dizi315[0]="İstikbal";  dizi315[1]="Karşılama"; dizi315[2]="Gelecek";   dizi315[3]="Geçmiş";    dizi315[4]="Ati";           dizi315[5]="Gerçekleşmemiş";
        dizi316[0]="İstiklal";  dizi316[1]="Bağımsızlık";dizi316[2]="Savaş";    dizi316[3]="Marş";      dizi316[4]="Kurtuluş";      dizi316[5]="Yönetmek";
        dizi317[0]="İvme";      dizi317[1]="Hız";       dizi317[2]="Sürat";     dizi317[3]="Zaman";     dizi317[4]="Yavaşlamak";    dizi317[5]="Fizik";
        dizi318[0]="İzci";      dizi318[1]="Kamp";      dizi318[2]="Orman";     dizi318[3]="Doğa";      dizi318[4]="Oymakbaşı";     dizi318[5]="Birlik";
        dizi319[0]="Jakuzi";    dizi319[1]="Banyo";     dizi319[2]="Küvet";     dizi319[3]="Masaj";     dizi319[4]="Duşakabin";     dizi319[5]="Duş";
        dizi320[0]="Jelatin";   dizi320[1]="Bant";      dizi320[2]="Saydam";    dizi320[3]="Ambalaj";   dizi320[4]="Korumak";       dizi320[5]="Kokusuz";
        dizi321[0]="Jelibon";   dizi321[1]="Sakızımsı"; dizi321[2]="Şeker";     dizi321[3]="Kolalı";    dizi321[4]="Meyveli";       dizi321[5]="Çocuk";
        dizi322[0]="Jeneratör"; dizi322[1]="Enerji";    dizi322[2]="Elektrik";  dizi322[3]="Motor";     dizi322[4]="Akü";           dizi322[5]="Batarya";
        dizi323[0]="Jet";       dizi323[1]="Uçak";      dizi323[2]="Hızlı";     dizi323[3]="Uçmak";     dizi323[4]="Seyahat";       dizi323[5]="Pilot";
        dizi324[0]="Jöle";      dizi324[1]="Saç";       dizi324[2]="Sürmek";    dizi324[3]="Tatlı";     dizi324[4]="Şekerleme";     dizi324[5]="Et Suyu";
        dizi325[0]="Kader";     dizi325[1]="Baht";      dizi325[2]="Kaza";      dizi325[3]="Yazgı";     dizi325[4]="Alınyazısı";    dizi325[5]="Talih";
        dizi326[0]="Kadro";     dizi326[1]="Takım";     dizi326[2]="Çizelge";   dizi326[3]="Yetki";     dizi326[4]="Sorumluluk";    dizi326[5]="memuriyet";
        dizi327[0]="Kafatası";  dizi327[1]="Beyin";     dizi327[2]="Yüz";       dizi327[3]="Kemik";     dizi327[4]="Omurga";        dizi327[5]="Saç";
        dizi328[0]="Kafes";     dizi328[1]="Kuş";       dizi328[2]="Dövüş";     dizi328[3]="Koyacak";   dizi328[4]="Siper";         dizi328[5]="Pencere";
        dizi329[0]="Kağıt";     dizi329[1]="Kitap";     dizi329[2]="Defter";    dizi329[3]="Yazı";      dizi329[4]="Ağaç";          dizi329[5]="A4";
        dizi330[0]="Kahvaltı";  dizi330[1]="Peynir";    dizi330[2]="Zeytin";    dizi330[3]="Çay";       dizi330[4]="Sabah";         dizi330[5]="Serpme";
        dizi331[0]="Kale";      dizi331[1]="Korunaklı Yer";dizi331[2]="Mazgal"; dizi331[3]="Güçlü Kimse";dizi331[4]="Futbol";       dizi331[5]="Satranç";
        dizi332[0]="Kalem";     dizi332[1]="Silah";     dizi332[2]="Açacak";    dizi332[3]="Silgi";     dizi332[4]="Sınav";         dizi332[5]="Defter";
        dizi333[0]="Kapkaç";    dizi333[1]="Hırsız";    dizi333[2]="Çalmak";    dizi333[3]="Polis";     dizi333[4]="Hile";          dizi333[5]="Soygun";
        dizi334[0]="Kar";       dizi334[1]="Kış";       dizi334[2]="Beyaz";     dizi334[3]="Kazanmak";  dizi334[4]="Para Kazancı";  dizi334[5]="Yarar";
        dizi335[0]="Karakol";   dizi335[1]="Polis";     dizi335[2]="Emniyet";   dizi335[3]="Mahalle";   dizi335[4]="Bekçi";         dizi335[5]="Komiser";
        dizi336[0]="Kask";      dizi336[1]="Baret";     dizi336[2]="Miğfer";    dizi336[3]="Başlık";    dizi336[4]="Bisiklet";      dizi336[5]="Motosiklet";
        dizi337[0]="Kaş";       dizi337[1]="Göz";       dizi337[2]="Yüz";       dizi337[3]="Sürme";     dizi337[4]="Kıl";           dizi337[5]="Kirpi";
        dizi338[0]="Katı";      dizi338[1]="Sıvı";      dizi338[2]="Gaz";       dizi338[3]="Sert";      dizi338[4]="Kırıcı";        dizi338[5]="Hoşgörüsüz";
        dizi339[0]="Kayınpeder";dizi339[1]="Kaynana";   dizi339[2]="Kayınço";   dizi339[3]="Baldız";    dizi339[4]="Baba";          dizi339[5]="Eş";
        dizi341[0]="Kaz";       dizi340[1]="Tavuk";     dizi340[2]="Hayvan";    dizi340[3]="Göl";       dizi340[4]="Budala";        dizi340[5]="Saf";
        dizi341[0]="Kazma";     dizi341[1]="Toprak";    dizi341[2]="Bitki";     dizi341[3]="Kürek";     dizi341[4]="Görgüsüz";      dizi341[5]="Kaba";
        dizi342[0]="Kek";       dizi342[1]="Islak";     dizi342[2]="Pasta";     dizi342[3]="Kurabiye";  dizi342[4]="Fırın";         dizi342[5]="Hamur";
        dizi343[0]="Kel";       dizi343[1]="Saç";       dizi343[2]="Kafa";      dizi343[3]="Dökülmek";  dizi343[4]="Armut";         dizi343[5]="Genetik";
        dizi344[0]="Kelepçe";   dizi344[1]="Suçlu";     dizi344[2]="Polis";     dizi344[3]="Yakalamak"; dizi344[4]="Hırsızlık";     dizi344[5]="Cinayet";
        dizi345[0]="Kepek";     dizi345[1]="Ekmek";     dizi345[2]="Un";        dizi345[3]="Saç";       dizi345[4]="Pire";          dizi345[5]="Bit";
        dizi346[0]="Kılavuz";   dizi346[1]="Yol Gösteren";dizi346[2]="Elkitabı";dizi346[3]="Üstün Kimse";dizi346[4]="Bilgili";      dizi346[5]="Topluma Işık Tutan";
        dizi347[0]="Kilim";     dizi347[1]="Döşeme";    dizi347[2]="Divan";     dizi347[3]="Desenli";   dizi347[4]="Yün";           dizi347[5]="Halı";
        dizi348[0]="Kitap";     dizi348[1]="Bilgi";     dizi348[2]="Defter";    dizi348[3]="Ders";      dizi348[4]="Kalem";         dizi348[5]="Kağıt";
        dizi349[0]="Kapı";      dizi349[1]="Ev";        dizi349[2]="Bahçe";     dizi349[3]="Sınır";     dizi349[4]="Oda";           dizi349[5]="Balkon";
        dizi350[0]="Kontör";    dizi350[1]="Telefon";   dizi350[2]="Fatura";    dizi350[3]="Dakika";    dizi350[4]="SMS";           dizi350[5]="Konuşmak";
        dizi351[0]="Korniş";    dizi351[1]="Perde";     dizi351[2]="Asmak";     dizi351[3]="Merdiven";  dizi351[4]="Pencere";       dizi351[5]="Tavan";
        dizi352[0]="Koyun";     dizi352[1]="Kuzu";      dizi352[2]="Keçi";      dizi352[3]="Süt";       dizi352[4]="İnek";          dizi352[5]="Kurban";
        dizi353[0]="Köz";       dizi353[1]="Kömür";     dizi353[2]="Odun";      dizi353[3]="Nargile";   dizi353[4]="Mangal";        dizi353[5]="Ateş";
        dizi354[0]="Kundak";    dizi354[1]="Bebek";     dizi354[2]="Sarmak";    dizi354[3]="Battaniye"; dizi354[4]="Yangın";        dizi354[5]="Yakmak";
        dizi355[0]="Kültür";    dizi355[1]="Ekinç";     dizi355[2]="Gelenek";   dizi355[3]="Yaşayış";   dizi355[4]="Düşünüş";       dizi355[5]="Toplumsal";
        dizi356[0]="Labaratuar";dizi356[1]="Deney";     dizi356[2]="İncelemek"; dizi356[3]="İlaç";      dizi356[4]="Araştırma";     dizi356[5]="Bilim İnsanı";
        dizi357[0]="Labirent";  dizi357[1]="Bulmaca";   dizi357[2]="Karmaşa";   dizi357[3]="Giriş";     dizi357[4]="Çıkış";         dizi357[5]="Olanaksız Durum";
        dizi358[0]="Lağım";     dizi358[1]="Kanalizasyon";dizi358[2]="Atık Su"; dizi358[3]="Ark";       dizi358[4]="Boru";          dizi358[5]="Yeraltı";
        dizi359[0]="Lahmacun";  dizi359[1]="Pide";      dizi359[2]="Ayran";     dizi359[3]="Kıyma";     dizi359[4]="Fırın";         dizi359[5]="Meze";
        dizi360[0]="Lakap";     dizi360[1]="Takma Ad";  dizi360[2]="Özellik";   dizi360[3]="Mahlas";    dizi360[4]="Ün";            dizi360[5]="Kullanıcı";
        dizi361[0]="Lale";      dizi361[1]="Devir";     dizi361[2]="Çiçek";     dizi361[3]="Gül";       dizi361[4]="Süs";           dizi361[5]="Kadeh";
        dizi362[0]="Lanet";     dizi362[1]="Kargıma";   dizi362[2]="Kargış";    dizi362[3]="Beddua";    dizi362[4]="Sihir";         dizi362[5]="Hakaret";
        dizi363[0]="Laz";       dizi363[1]="Karadeniz"; dizi363[2]="Trabzon";   dizi363[3]="Ağız";      dizi363[4]="Şive";          dizi363[5]="Hamsi";
        dizi364[0]="Leblebi";   dizi364[1]="Çorum";     dizi364[2]="Nohut";     dizi364[3]="Kavurmak";  dizi364[4]="Beyaz";         dizi364[5]="Sarı";
        dizi365[0]="Lehim";     dizi365[1]="Kaynak";    dizi365[2]="Metal";     dizi365[3]="Isı Verme"; dizi365[4]="Tel";           dizi365[5]="Birbirine Tutturma";
        dizi366[0]="Leke";      dizi366[1]="Doğum";     dizi365[2]="Kalan İz";  dizi366[3]="Yağ";       dizi366[4]="Çamaşır Suyu";  dizi366[5]="Renk Değişikliği";
        dizi367[0]="lif";       dizi367[1]="Kese";      dizi367[2]="Banyo";     dizi367[3]="Sabun";     dizi367[4]="Kas";           dizi367[5]="Bağlar";
        dizi368[0]="Limon";     dizi368[1]="Ekşi";      dizi368[2]="Sarı";      dizi368[3]="Turunçgil"; dizi368[4]="Akdeniz";       dizi368[5]="Kokulu";
        dizi369[0]="Lisan";     dizi369[1]="Sözlük";    dizi369[2]="Dil";       dizi369[3]="Konuşmak";  dizi369[4]="Anlaşmak";      dizi369[5]="Dillenmek";
        dizi370[0]="Lise";      dizi370[1]="Okul";      dizi370[2]="Sınav";     dizi370[3]="Ders";      dizi370[4]="Ortaokul";      dizi370[5]="Üniversite";
        dizi371[0]="Liste";     dizi371[1]="Çizelge";   dizi371[2]="Dizelge";   dizi371[3]="Dosya";     dizi371[4]="Kayıt Tutmak";  dizi371[5]="Alt Alta Yazmak";
        dizi372[0]="Lodos";     dizi372[1]="Deniz";     dizi372[2]="Kuru";      dizi372[3]="Sıcak";     dizi372[4]="Rüzgar";        dizi372[5]="Esmek";
        dizi373[0]="Lokanta";   dizi373[1]="Yemek";     dizi373[2]="Garson";    dizi373[3]="Sipariş";   dizi373[4]="Menü";          dizi373[5]="Hesap";
        dizi374[0]="Lunapark";  dizi374[1]="Çocuk";     dizi374[2]="Eğlenmek";  dizi374[3]="Gondol";    dizi374[4]="Korku Tüneli";  dizi374[5]="Çarpışan Araba";
        dizi375[0]="Lügat";     dizi375[1]="Sözlük";    dizi375[2]="Sözcük";    dizi375[3]="Kelime";    dizi375[4]="Lisan";         dizi375[5]="Divan";
        dizi376[0]="Magazin";   dizi376[1]="Ünlü";      dizi376[2]="Gazete";    dizi376[3]="Dergi";     dizi376[4]="Pazar";         dizi376[5]="Haber";
        dizi377[0]="Mağaza";    dizi377[1]="Dükkan";    dizi377[2]="AVM";       dizi377[3]="Giyim";     dizi377[4]="Spor";          dizi377[5]="Çarşı";
        dizi378[0]="Mağlubiyet";dizi378[1]="Yenilmek";  dizi378[2]="Galibiyet"; dizi378[3]="Yarışma";   dizi378[4]="Karşılaşma";    dizi378[5]="Beraberlik";
        dizi379[0]="Makas";     dizi379[1]="Tırnak";    dizi379[2]="Kesmek";    dizi379[3]="Kırpmak";   dizi379[4]="Yanak";         dizi379[5]="Kuaför";
        dizi380[0]="Makine";    dizi380[1]="Araç";      dizi380[2]="Otomobil";  dizi380[3]="Taşıt";     dizi380[4]="Dişli";         dizi380[5]="İşçilik";
        dizi381[0]="Makyaj";    dizi381[1]="Ruj";       dizi381[2]="Rimel";     dizi381[3]="Sürme";     dizi381[4]="Fondöten";      dizi381[5]="Boya";
        dizi382[0]="Mal";       dizi382[1]="Eşya";      dizi382[2]="Depo";      dizi382[3]="Ticaret";   dizi382[4]="Varlık";        dizi382[5]="Hakaret";
        dizi383[0]="Malikane";  dizi383[1]="Konak";     dizi383[2]="Köşk";      dizi383[3]="Villa";     dizi383[4]="Bahçe";         dizi383[5]="Havuz";
        dizi384[0]="Masaj";     dizi384[1]="Masör";     dizi384[2]="Sırt";      dizi384[3]="Omuz";      dizi384[4]="Rahatlama";     dizi384[5]="Yoğurmak";
        dizi385[0]="Masal";     dizi385[1]="Hikaye";    dizi385[2]="Öykü";      dizi385[3]="Roman";     dizi385[4]="Edebiyat";      dizi385[5]="Olağanüstü";
        dizi386[0]="Medeniyet"; dizi386[1]="Uygarlık";  dizi386[2]="Şehirli";   dizi386[3]="Yönetmek";  dizi386[4]="Muasır";        dizi386[5]="Tek Dişi Kalmış Canavar";
        dizi387[0]="Merinos";   dizi387[1]="Koyun";     dizi387[2]="Kıvırcık";  dizi387[3]="Kuzu";      dizi387[4]="Halı";          dizi387[5]="Mobilya";
        dizi388[0]="Menü";      dizi388[1]="Yemek";     dizi388[2]="Restaurant";dizi388[3]="Lokanta";   dizi388[4]="Sipariş";       dizi388[5]="Garson";
        dizi389[0]="Mesaj";     dizi389[1]="Telefon";   dizi389[2]="Dakika";    dizi389[3]="Konuşma";   dizi389[4]="Yazma";         dizi389[5]="Gönderme";
        dizi390[0]="Meşrutiyet";dizi390[1]="Parlemento";dizi390[2]="Hükümet";   dizi390[3]="Osmanlı";   dizi390[4]="Anayasa";       dizi390[5]="Yönetim Biçimi";
        dizi391[0]="Minder";    dizi391[1]="Şilte";     dizi391[2]="Pamuk";     dizi391[3]="Oturmak";   dizi391[4]="Yumuşak";       dizi391[5]="Yaygı";
        dizi392[0]="Muasır";    dizi392[1]="Medeniyet"; dizi392[2]="Çağdaş";    dizi392[3]="Seviye";    dizi392[4]="Devir";         dizi392[5]="Aynı Yüzyıl";
        dizi393[0]="Muhabir";   dizi393[1]="Haber";     dizi393[2]="Gazeteci";  dizi393[3]="Spiker";    dizi393[4]="Ajans";         dizi393[5]="Konuşmacı";
        dizi394[0]="Muhasebe";  dizi394[1]="Ticaret";   dizi394[2]="Ekonomi";   dizi394[3]="Hesaplaşma";dizi394[4]="Analiz";        dizi394[5]="Para";
        dizi395[0]="Müfettiş";  dizi395[1]="Denetmen";  dizi395[2]="Okul";      dizi395[3]="Teftiş";    dizi395[4]="Personel";      dizi395[5]="Memur";
        dizi396[0]="Mühür";     dizi396[1]="Kaşe";      dizi396[2]="Mürekkep";  dizi396[3]="Damga";     dizi396[4]="İşaret";        dizi396[5]="İmza";
        dizi397[0]="Müjde";     dizi397[1]="Haber";     dizi397[2]="Muştu";     dizi397[3]="Sevindirici";dizi397[4]="Güzel Bilgi";  dizi397[5]="Elim";
        dizi398[0]="Mülk";      dizi398[1]="Mal";       dizi398[2]="Dükkan";    dizi398[3]="Arsa";      dizi398[4]="Yapı";          dizi398[5]="Arazi";
        dizi399[0]="Münaşaka";  dizi399[1]="Tartışma";  dizi399[2]="Münazara";  dizi399[3]="Savunma";   dizi399[4]="Ağız Kavgası";  dizi399[5]="Çekişmek";
        dizi400[0]="Münazara";  dizi400[1]="Karşılaşma";dizi400[2]="Tez";       dizi400[3]="Tartışma";  dizi400[4]="Fikir";         dizi400[5]="Münaşaka";
        dizi401[0]="Mürekkep";  dizi401[1]="Kalem";     dizi401[2]="Yazıcı";    dizi401[3]="Yazmak";    dizi401[4]="Sıvı";          dizi401[5]="Leke";
        dizi402[0]="Müslüman";  dizi402[1]="İslam";     dizi402[2]="Mümin";     dizi402[3]="Hristiyan"; dizi402[4]="Yahudi";        dizi402[5]="Din";
        dizi403[0]="Mütevazı";  dizi403[1]="Sade";      dizi403[2]="iddiasız";  dizi403[3]="Kibirsiz";  dizi403[4]="Gösterişsiz";   dizi403[5]="Alçakgönüllü";
        dizi404[0]="Müzik";     dizi404[1]="Nota";      dizi404[2]="Şarkı";     dizi404[3]="Söylemek";  dizi404[4]="Sanatçı";       dizi404[5]="Öğretmen";
        dizi405[0]="Nadir";     dizi405[1]="Seyrek";    dizi405[2]="Az";        dizi405[3]="Sık";       dizi405[4]="Az Bulunan";    dizi405[5]="Sık Rastlanmayan";
        dizi406[0]="Nakış";     dizi406[1]="Süs";       dizi406[2]="İşleme";    dizi406[3]="Örgü";      dizi406[4]="İplik";         dizi406[5]="Dikiş";
        dizi407[0]="Nal";       dizi407[1]="At";        dizi407[2]="Öküz";      dizi407[3]="Demir";     dizi407[4]="Çivi";          dizi407[5]="Ayak";
        dizi408[0]="Nam";       dizi408[1]="Ün";        dizi408[2]="Şan";       dizi408[3]="Şöhret";    dizi408[4]="Tanınmak";      dizi408[5]="Ünvan";
        dizi409[0]="Namaz";     dizi409[1]="Ezan";      dizi409[2]="Cami";      dizi349[3]="Yatsı";     dizi409[4]="İkindi";        dizi409[5]="Aptes";
        dizi410[0]="Nankör";    dizi410[1]="Kedi";      dizi410[2]="Hayırsız";  dizi410[3]="Unutmak";   dizi410[4]="iyilikbilmez";  dizi410[5]="Kıymet Bilmemek";
        dizi411[0]="Nar";       dizi411[1]="Çiçek";     dizi411[2]="Ekşi";      dizi411[3]="Tatlı";     dizi411[4]="Meyve";         dizi411[5]="Tane Tane";
        dizi412[0]="Narkoz";    dizi412[1]="Uyuşturucu";dizi412[2]="Ameliyat";  dizi412[3]="Serum";     dizi412[4]="İlaç";          dizi412[5]="Uyku";
        dizi413[0]="Nasır";     dizi413[1]="Sert";      dizi413[2]="Kalın";     dizi413[3]="Deri";      dizi413[4]="Cilt";          dizi413[5]="Duygusuzlaşmak";
        dizi414[0]="Nasip";     dizi414[1]="Kısmet";    dizi414[2]="Baht";      dizi414[3]="Talih";     dizi414[4]="Kavuşmak";      dizi414[5]="Ulaşmak";
        dizi415[0]="Nazik Olmak";dizi415[1]="Kibar";    dizi415[2]="Zarif";     dizi415[3]="Saygılı";   dizi415[4]="İnce Yapılı";   dizi415[5]="Özen Gösteren";
        dizi416[0]="Nazlanmak"; dizi416[1]="Israr";     dizi416[2]="İsteksiz";  dizi416[3]="Yalvartmak";dizi416[4]="Binbir Zahmet"; dizi416[5]="Hemen Kabul Etmemek";
        dizi417[0]="Nefet";     dizi417[1]="Kin";       dizi417[2]="Tiksinme";  dizi417[3]="Kızgınlık"; dizi417[4]="Olsumsuzluk";   dizi417[5]="Hoşlanmamak";
        dizi418[0]="Net";       dizi418[1]="Safi";      dizi418[2]="Açık";      dizi418[3]="Doğru";     dizi418[4]="İyi Bilinen";   dizi418[5]="Geriye Kalan  ";
        dizi419[0]="Netice";    dizi419[1]="Sonuç";     dizi419[2]="Durum";     dizi419[3]="Son";       dizi419[4]="Haller";        dizi419[5]="Fayda";
        dizi420[0]="Ney";       dizi420[1]="Flüt";      dizi420[2]="Üflemek";   dizi420[3]="Çalgı";     dizi420[4]="Sakinlik";      dizi420[5]="Hû";
        dizi421[0]="Nikah";     dizi421[1]="Evlenmek";  dizi421[2]="Nişam";     dizi421[3]="Düğün";     dizi421[4]="Şahit";         dizi421[5]="Evlilik Cüzdanı";
        dizi422[0]="Nişan";     dizi422[1]="Düğün";     dizi422[2]="Nikah";     dizi422[3]="Söz";       dizi422[4]="Yüzük";         dizi422[5]="Makas";
        dizi423[0]="Nişasta";   dizi423[1]="Un";        dizi423[2]="Mısır";     dizi423[3]="Buğday";    dizi423[4]="Patates";       dizi423[5]="Pirinç";
        dizi424[0]="Niyet";     dizi424[1]="İstek";     dizi424[2]="Düşünce";   dizi424[3]="Tasarlamak";dizi424[4]="Oruç Tutmak";   dizi424[5]="Namaz Kılmak";
        dizi425[0]="Nokta";     dizi425[1]="Virgül";    dizi425[2]="Cümle";     dizi425[3]="Benek";     dizi425[4]="Yer";           dizi425[5]="Önemli Bölüm";
        dizi426[0]="Not";       dizi426[1]="Puan";      dizi426[2]="Kısa Yazı"; dizi426[3]="Derece";    dizi426[4]="Kanı";          dizi426[5]="Okul";
        dizi427[0]="Nota";      dizi427[1]="Müzik";     dizi427[2]="Beste";     dizi427[3]="Simge";     dizi427[4]="Defter";        dizi427[5]="Yazılı Bildiri";
        dizi428[0]="Mide";      dizi428[1]="Organ";     dizi328[2]="Yemek";     dizi428[3]="Sindirim";  dizi428[4]="Ciğer";         dizi428[5]="Böbrek";
        dizi429[0]="Nöbet";     dizi429[1]="Asker";     dizi429[2]="Görev";     dizi429[3]="Gözetlemek";dizi429[4]="Korumak";       dizi429[5]="Sabit Kalmak";
        dizi430[0]="Nötr";      dizi430[1]="Pozitif";   dizi430[2]="Negatif";   dizi430[3]="Elektrik";  dizi430[4]="Toprak";        dizi430[5]="Su";
        dizi431[0]="Numara";    dizi431[1]="Telefon";   dizi431[2]="Sayı";      dizi431[3]="Düzmece";   dizi431[4]="Oyun";          dizi431[5]="Yalan Söylemek";
        dizi432[0]="Nur";       dizi432[1]="Işık";      dizi432[2]="Aydınlık";  dizi432[3]="Parıltı";   dizi432[4]="Parlak";        dizi432[5]="Temiz";
        dizi433[0]="Nutuk";     dizi433[1]="Söylev";    dizi433[2]="İkna Etmek";dizi433[3]="Anlatmak";  dizi433[4]="Uzun Konuşma";  dizi433[5]="Duygu Aşılamak";
        dizi434[0]="Nüfus";     dizi434[1]="Birey";     dizi434[2]="Kişi";      dizi434[3]="Halk";      dizi434[4]="Cüzdan";        dizi434[5]="Müdürlük";
        dizi435[0]="Nükte";     dizi435[1]="Espiri";    dizi435[2]="Şaka";      dizi435[3]="İnce Anlam";dizi435[4]="Zarif Söz";     dizi435[5]="Düşündürücü";
        dizi436[0]="Objektif";  dizi436[1]="Nesnel";    dizi436[2]="Öznel";     dizi436[3]="Duygu";     dizi436[4]="Önyargısız";    dizi436[5]="Fotoğraf Makinesi";
        dizi437[0]="Obur";      dizi437[1]="Aç";        dizi437[2]="İştahlı";   dizi437[3]="yemek";     dizi437[4]="Çok Yiyen";     dizi437[5]="Doymak Bilmeyen";
        dizi438[0]="Ocak";      dizi438[1]="Fırın";     dizi438[2]="Yemek";     dizi438[3]="Pişirmek";  dizi438[4]="Aralık";        dizi438[5]="Şubat";
        dizi439[0]="Odun";      dizi439[1]="Ağaç";      dizi439[2]="Yakacak";   dizi439[3]="Tahta";     dizi439[4]="Dal";           dizi439[5]="Hakaret";
        dizi441[0]="Ofsayt";    dizi440[1]="Korner";    dizi440[2]="Futbolcu";  dizi440[3]="Hakem";     dizi440[4]="Bayrak";        dizi440[5]="Atmak";
        dizi441[0]="Oğlan";     dizi441[1]="Çocuk";     dizi441[2]="Erkek";     dizi441[3]="Delikanlı"; dizi441[4]="Adam";          dizi441[5]="Yetişkin";
        dizi442[0]="Oksijen";   dizi442[1]="Hava";      dizi442[2]="Rüzgar";    dizi442[3]="Nefes";     dizi442[4]="Ciğer";         dizi442[5]="Gaz";
        dizi443[0]="Okul";      dizi443[1]="Öğrenci";   dizi443[2]="Öğretmen";  dizi443[3]="Sınıf";     dizi443[4]="Üniversite";    dizi443[5]="Ders";
        dizi444[0]="Okyanus";   dizi444[1]="Deniz";     dizi444[2]="Göl";       dizi444[3]="Ada";       dizi444[4]="Gemi";          dizi444[5]="Balık";
        dizi445[0]="Olağan";    dizi445[1]="Doğal";     dizi445[2]="Tabii";     dizi445[3]="Normal";    dizi445[4]="Alışagelmiş";   dizi445[5]="Her Zaman Olan";
        dizi446[0]="Olay";      dizi446[1]="Eylem";     dizi446[2]="Durum";     dizi446[3]="Sorun";     dizi446[4]="Dikkat Çeken";  dizi446[5]="Önemli Olgu";
        dizi448[0]="Omurga";    dizi448[1]="Kemik";     dizi448[2]="Sırt";      dizi448[3]="Kambur";    dizi448[4]="Ense";          dizi448[5]="Dik Durmak";
        dizi449[0]="Omuz";      dizi449[1]="Kol";       dizi449[2]="Boyun";     dizi449[3]="Gövde";     dizi449[4]="Göğüs";         dizi449[5]="Kafa";
        dizi450[0]="Onaylamak"; dizi450[1]="Tasdik";    dizi450[2]="İcazet";    dizi450[3]="Doğrulamak";dizi450[4]="İzin Vermek";   dizi450[5]="Uygun Bulma";
        dizi451[0]="Orantı";    dizi451[1]="Eşitlik";   dizi451[2]="Uygunluk";  dizi451[3]="Denge";     dizi451[4]="Matematik";     dizi451[5]="Ters";
        dizi452[0]="Orkestra";  dizi452[1]="Müzik";     dizi452[2]="Çalgılar";  dizi452[3]="Koro";      dizi452[4]="Sahne";         dizi452[5]="Seyirci";
        dizi453[0]="Orman";     dizi453[1]="Ağaç";      dizi453[2]="Fidan";     dizi453[3]="Hayvan";    dizi453[4]="Dağ";           dizi453[5]="Doğa";
        dizi454[0]="Orta";      dizi454[1]="Köşe";      dizi454[2]="İki Parça"; dizi454[3]="Kahve";     dizi454[4]="Ne İyi Ne kötü";dizi454[5]="Eşit Uzaklık";
        dizi455[0]="Oruç";      dizi455[1]="Ramazan";   dizi455[2]="İftar";     dizi455[3]="Sahur";     dizi455[4]="Akşam";         dizi455[5]="Teravih";
        dizi456[0]="Ot";        dizi456[1]="Yeşillik";  dizi456[2]="Sebze";     dizi456[3]="Çimen";     dizi456[4]="İlaç";          dizi456[5]="Çalı Çırpı";
        dizi457[0]="Otizm";     dizi457[1]="2 Nisan";   dizi457[2]="Farklılık"; dizi457[3]="Hastalık";  dizi457[4]="Genetik";       dizi457[5]="Down Sendromu";
        dizi458[0]="Otobiyografi";dizi458[1]="Özyaşam"; dizi458[2]="Yazmak";    dizi458[3]="Anlatmak";  dizi458[4]="Hayat";         dizi458[5]="Kitap";
        dizi459[0]="Otomobil";  dizi459[1]="Araç";      dizi459[2]="Araba";     dizi459[3]="Taşıt";     dizi459[4]="Motor";         dizi459[5]="Motosiklet";
        dizi460[0]="Otopark";   dizi460[1]="Araba";     dizi460[2]="Araç";      dizi460[3]="Bırakmak";  dizi460[4]="Fiş";           dizi460[5]="Süre";
        dizi461[0]="Otostop";   dizi461[1]="Yolculuk";  dizi461[2]="Durdurmak"; dizi461[3]="Parasız";   dizi461[4]="Araba";         dizi461[5]="Gitmek";
        dizi462[0]="Otoyol";    dizi462[1]="Araba";     dizi462[2]="Yolculuk";  dizi462[3]="Geçiş";     dizi462[4]="Şehirlerarası"; dizi462[5]="Dinlenme Tesisi";
        dizi463[0]="Oy";        dizi463[1]="Rey";       dizi463[2]="Seçim";     dizi463[3]="Sandık";    dizi463[4]="Seçenek";       dizi463[5]="Birlikte Belirleme";
        dizi464[0]="Oyun";      dizi464[1]="Eğlence";   dizi464[2]="Çocuk";     dizi464[3]="Şaka Yapma";dizi464[4]="Dalavere";      dizi464[5]="Aldatma";
        dizi465[0]="Ozan";      dizi465[1]="Aşık";      dizi465[2]="Şair";      dizi465[3]="Şiir";      dizi465[4]="Türkü";         dizi465[5]="Oğuz";
        dizi466[0]="Öğrenci";   dizi466[1]="Öğretmen";  dizi465[2]="Okul";      dizi466[3]="Ders";      dizi466[4]="Eğitim";        dizi466[5]="Sınıf";
        dizi467[0]="Ödül";      dizi467[1]="Armağan";   dizi467[2]="Başarı";    dizi467[3]="iyilik";    dizi467[4]="Karşılık";      dizi467[5]="Ceza";
        dizi468[0]="Öğlen";     dizi468[1]="Güneş";     dizi468[2]="Sabah";     dizi468[3]="Akşam";     dizi468[4]="Uyku";          dizi468[5]="Yemek";
        dizi469[0]="Ölmek";     dizi469[1]="Yaşamak";   dizi469[2]="Azrail";    dizi469[3]="Hayat";     dizi469[4]="Başlangıç";     dizi469[5]="Mezar";
        dizi470[0]="Öfke";      dizi470[1]="Kızgınlık"; dizi470[2]="Sinirlenme";dizi470[3]="Gözdağı";   dizi470[4]="Saldırganlık";  dizi470[5]="Hiddet";
        dizi471[0]="Öğüt";      dizi471[1]="Nasihat";   dizi471[2]="Söz";       dizi471[3]="Tavsiye";   dizi471[4]="Vermek";        dizi471[5]="Yol Göstermek";
        dizi472[0]="Öykü";      dizi472[1]="Masal";     dizi472[2]="Anı";       dizi472[3]="Hikaye";    dizi472[4]="Düzyazı";       dizi472[5]="Roman";
        dizi473[0]="Özenmek";   dizi473[1]="Taklit";    dizi473[2]="Çabalamak"; dizi473[3]="İtina";     dizi473[4]="İsteklenmek";   dizi473[5]="Yapmaya Kalkışmak";
        dizi474[0]="Örgü";      dizi474[1]="İplik";     dizi474[2]="Tığ";       dizi474[3]="Şiş";       dizi474[4]="Dikmek";        dizi474[5]="Saç";
        dizi475[0]="Öngörü";    dizi475[1]="Önyargı";   dizi475[2]="Kestirme";  dizi475[3]="Anlayabilme";dizi475[4]="Tahmin Etme";  dizi475[5]="Önceden Anlama";
        dizi476[0]="Özet";      dizi476[1]="Kısa";      dizi476[2]="Öz";        dizi476[3]="Yazı";      dizi476[4]="Konuşma";       dizi476[5]="Eser";
        dizi477[0]="Öküz";      dizi477[1]="İnek";      dizi477[2]="Hayvan";    dizi477[3]="Boğa";      dizi477[4]="Koyun";         dizi477[5]="Hakaret";
        dizi478[0]="Öksürmek";  dizi478[1]="Hapşurmak"; dizi478[2]="Hıçkırmak"; dizi478[3]="Ciğer";     dizi478[4]="Baharat";       dizi478[5]="Ağız";
        dizi479[0]="Ördek";     dizi479[1]="Göl";       dizi479[2]="havuz";     dizi479[3]="Tavuk";     dizi479[4]="Kuş";           dizi479[5]="Sarı";
        dizi480[0]="Örselemek"; dizi480[1]="Sarsmak";   dizi480[2]="Soldurmak"; dizi480[3]="Yıpratmak"; dizi480[4]="Zedelemek";     dizi480[5]="Hırpalamak";
        dizi481[0]="Örs";       dizi481[1]="Maden";     dizi481[2]="Dövmek";    dizi481[3]="Kundura";   dizi481[4]="Ayakkabı";      dizi481[5]="Tamir";
        dizi482[0]="Ödün";      dizi482[1]="Vazgeçme";  dizi482[2]="Hak";       dizi482[3]="Taviz";     dizi482[4]="İvaz";          dizi482[5]="Feragat";
        dizi483[0]="Örnek";     dizi483[1]="Benzer";    dizi483[2]="Biblo";     dizi483[3]="Eş";        dizi483[4]="Maket";         dizi483[5]="Kişiyi Taklit Etme";
        dizi484[0]="Ölçü";      dizi484[1]="Boyut";     dizi484[2]="Ilımlılık"; dizi484[3]="Vezin";     dizi484[4]="Tartı";         dizi484[5]="Terzi";
        dizi485[0]="Örgüt";     dizi485[1]="Birlik";    dizi485[2]="Kuruluş";   dizi485[3]="Dernek";    dizi485[4]="Sendika";       dizi485[5]="Toplanma";
        dizi486[0]="Papatya";   dizi486[1]="Bahar";     dizi486[2]="Çiçek";     dizi486[3]="Polen";     dizi486[4]="Seviyor";       dizi486[5]="Sevmiyor";
        dizi487[0]="Palavra";   dizi487[1]="Uzun";      dizi487[2]="Boş Söz";   dizi487[3]="Gereksiz";  dizi487[4]="Gerçeğe Aykırı";dizi487[5]="Güverte";
        dizi488[0]="Paket";     dizi488[1]="Kargo";     dizi488[2]="Sipariş";   dizi488[3]="Kutu";      dizi488[4]="Fatura";        dizi488[5]="Kontör";
        dizi489[0]="Pabuç";     dizi489[1]="Ayakkabı";  dizi489[2]="Kundura";   dizi489[3]="Giymek";    dizi489[4]="Dam";           dizi489[5]="Bağcık";
        dizi490[0]="Pil";       dizi490[1]="Batarya";   dizi490[2]="Enerji";    dizi490[3]="Kumanda";   dizi490[4]="Telefon";       dizi490[5]="Hücre";
        dizi491[0]="Para";      dizi491[1]="Pamuk";     dizi491[2]="Madeni";    dizi491[3]="Lidyalılar";dizi491[4]="Maaş";          dizi491[5]="Harçlık";
        dizi492[0]="Pasaj";     dizi492[1]="Çarşı";     dizi492[2]="Dükkan";    dizi492[3]="Kapalı";    dizi492[4]="Üstü Kapalı";   dizi492[5]="Eserin Bir Bölümü";
        dizi493[0]="Pide";      dizi493[1]="Ramazan";   dizi493[2]="Ekmek";     dizi493[3]="İftar";     dizi493[4]="Fırın";         dizi493[5]="Kuyruk";
        dizi494[0]="Pense";     dizi494[1]="Tornavida"; dizi494[2]="Vida";      dizi494[3]="Germek";    dizi494[4]="Bükmek";        dizi494[5]="Maşa";
        dizi495[0]="Posta";     dizi495[1]="Mail";      dizi495[2]="Mektup";    dizi495[3]="Paket";     dizi495[4]="PTT";           dizi495[5]="Kargo";
        dizi496[0]="Pist";      dizi496[1]="Uçak";      dizi496[2]="Havalimanı";dizi496[3]="Gösteri";   dizi496[4]="Dans";          dizi496[5]="Yarış";
        dizi497[0]="Partner";   dizi497[1]="Eş";        dizi497[2]="Sevgili";   dizi497[3]="Ortak";     dizi497[4]="Dans";          dizi497[5]="Oyun Arkadaşı";
        dizi498[0]="Parazit";   dizi498[1]="Virüs";     dizi498[2]="Mikrop";    dizi498[3]="Bozucu Ses";dizi498[4]="Uyumu Bozmak";  dizi498[5]="Araya Girmek";
        dizi499[0]="Rüya";      dizi499[1]="Düş";       dizi499[2]="Hayal";     dizi499[3]="Uyku";      dizi499[4]="Bilinçaltı";    dizi499[5]="Gece";
        dizi500[0]="Rakip";     dizi500[1]="Yarış";     dizi500[2]="Müsabaka";  dizi500[3]="Geçmek";    dizi500[4]="Sınav";         dizi500[5]="Üstünlük";










        String diziler[][]={
                {dizi0[0],dizi0[1],dizi0[2],dizi0[3],dizi0[4],dizi0[5]},
                {dizi1[0],dizi1[1],dizi1[2],dizi1[3],dizi1[4],dizi1[5]},
                {dizi2[0],dizi2[1],dizi2[2],dizi2[3],dizi2[4],dizi2[5]},
                {dizi3[0],dizi3[1],dizi3[2],dizi3[3],dizi3[4],dizi3[5]},
                {dizi4[0],dizi4[1],dizi4[2],dizi4[3],dizi4[4],dizi4[5]},
                {dizi5[0],dizi5[1],dizi5[2],dizi5[3],dizi5[4],dizi5[5]},
                {dizi6[0],dizi6[1],dizi6[2],dizi6[3],dizi6[4],dizi6[5]},
                {dizi7[0],dizi7[1],dizi7[2],dizi7[3],dizi7[4],dizi7[5]},
                {dizi8[0],dizi8[1],dizi8[2],dizi8[3],dizi8[4],dizi8[5]},
                {dizi9[0],dizi9[1],dizi9[2],dizi9[3],dizi9[4],dizi9[5]},
                {dizi10[0],dizi10[1],dizi10[2],dizi10[3],dizi10[4],dizi10[5]},
                {dizi11[0],dizi11[1],dizi11[2],dizi11[3],dizi11[4],dizi11[5]},
                {dizi12[0],dizi12[1],dizi12[2],dizi12[3],dizi12[4],dizi12[5]},
                {dizi13[0],dizi13[1],dizi13[2],dizi13[3],dizi13[4],dizi13[5]},
                {dizi14[0],dizi14[1],dizi14[2],dizi14[3],dizi14[4],dizi14[5]},
                {dizi15[0],dizi15[1],dizi15[2],dizi15[3],dizi15[4],dizi15[5]},
                {dizi16[0],dizi16[1],dizi16[2],dizi16[3],dizi16[4],dizi16[5]},
                {dizi17[0],dizi17[1],dizi17[2],dizi17[3],dizi17[4],dizi17[5]},
                {dizi18[0],dizi18[1],dizi18[2],dizi18[3],dizi18[4],dizi18[5]},
                {dizi19[0],dizi19[1],dizi19[2],dizi19[3],dizi19[4],dizi19[5]},
                {dizi20[0],dizi20[1],dizi20[2],dizi20[3],dizi20[4],dizi20[5]},
                {dizi21[0],dizi21[1],dizi21[2],dizi21[3],dizi21[4],dizi21[5]},
                {dizi22[0],dizi22[1],dizi22[2],dizi22[3],dizi22[4],dizi22[5]},
                {dizi23[0],dizi23[1],dizi23[2],dizi23[3],dizi23[4],dizi23[5]},
                {dizi24[0],dizi24[1],dizi24[2],dizi24[3],dizi24[4],dizi24[5]},
                {dizi25[0],dizi25[1],dizi25[2],dizi25[3],dizi25[4],dizi25[5]},
                {dizi26[0],dizi26[1],dizi26[2],dizi26[3],dizi26[4],dizi26[5]},
                {dizi27[0],dizi27[1],dizi27[2],dizi27[3],dizi27[4],dizi27[5]},
                {dizi28[0],dizi28[1],dizi28[2],dizi28[3],dizi28[4],dizi28[5]},
                {dizi29[0],dizi29[1],dizi29[2],dizi29[3],dizi29[4],dizi29[5]},
                {dizi30[0],dizi30[1],dizi30[2],dizi30[3],dizi30[4],dizi30[5]},
                {dizi31[0],dizi31[1],dizi31[2],dizi31[3],dizi31[4],dizi31[5]},
                {dizi32[0],dizi32[1],dizi32[2],dizi32[3],dizi32[4],dizi32[5]},
                {dizi33[0],dizi33[1],dizi33[2],dizi33[3],dizi33[4],dizi33[5]},
                {dizi34[0],dizi34[1],dizi34[2],dizi34[3],dizi34[4],dizi34[5]},
                {dizi35[0],dizi35[1],dizi35[2],dizi35[3],dizi35[4],dizi35[5]},
                {dizi36[0],dizi36[1],dizi36[2],dizi36[3],dizi36[4],dizi36[5]},
                {dizi37[0],dizi37[1],dizi37[2],dizi37[3],dizi37[4],dizi37[5]},
                {dizi38[0],dizi38[1],dizi38[2],dizi38[3],dizi38[4],dizi38[5]},
                {dizi39[0],dizi39[1],dizi39[2],dizi39[3],dizi39[4],dizi39[5]},
                {dizi40[0],dizi40[1],dizi40[2],dizi40[3],dizi40[4],dizi40[5]},
                {dizi41[0],dizi41[1],dizi41[2],dizi41[3],dizi41[4],dizi41[5]},
                {dizi42[0],dizi42[1],dizi42[2],dizi42[3],dizi42[4],dizi42[5]},
                {dizi43[0],dizi43[1],dizi43[2],dizi43[3],dizi43[4],dizi43[5]},
                {dizi44[0],dizi44[1],dizi44[2],dizi44[3],dizi44[4],dizi44[5]},
                {dizi45[0],dizi45[1],dizi45[2],dizi45[3],dizi45[4],dizi45[5]},
                {dizi46[0],dizi46[1],dizi46[2],dizi46[3],dizi46[4],dizi46[5]},
                {dizi47[0],dizi47[1],dizi47[2],dizi47[3],dizi47[4],dizi47[5]},
                {dizi48[0],dizi48[1],dizi48[2],dizi48[3],dizi48[4],dizi48[5]},
                {dizi49[0],dizi49[1],dizi49[2],dizi49[3],dizi49[4],dizi49[5]},
                {dizi50[0],dizi50[1],dizi50[2],dizi50[3],dizi50[4],dizi50[5]},
                {dizi51[0],dizi51[1],dizi51[2],dizi51[3],dizi51[4],dizi51[5]},
                {dizi52[0],dizi52[1],dizi52[2],dizi52[3],dizi52[4],dizi52[5]},
                {dizi53[0],dizi53[1],dizi53[2],dizi53[3],dizi53[4],dizi53[5]},
                {dizi54[0],dizi54[1],dizi54[2],dizi54[3],dizi54[4],dizi54[5]},
                {dizi55[0],dizi55[1],dizi55[2],dizi55[3],dizi55[4],dizi55[5]},
                {dizi56[0],dizi56[1],dizi56[2],dizi56[3],dizi56[4],dizi56[5]},
                {dizi57[0],dizi57[1],dizi57[2],dizi57[3],dizi57[4],dizi57[5]},
                {dizi58[0],dizi58[1],dizi58[2],dizi58[3],dizi58[4],dizi58[5]},
                {dizi59[0],dizi59[1],dizi59[2],dizi59[3],dizi59[4],dizi59[5]},
                {dizi60[0],dizi60[1],dizi60[2],dizi60[3],dizi60[4],dizi60[5]},
                {dizi61[0],dizi61[1],dizi61[2],dizi61[3],dizi61[4],dizi61[5]},
                {dizi62[0],dizi62[1],dizi62[2],dizi62[3],dizi62[4],dizi62[5]},
                {dizi63[0],dizi63[1],dizi63[2],dizi63[3],dizi63[4],dizi63[5]},
                {dizi64[0],dizi64[1],dizi64[2],dizi64[3],dizi64[4],dizi64[5]},
                {dizi65[0],dizi65[1],dizi65[2],dizi65[3],dizi65[4],dizi65[5]},
                {dizi66[0],dizi66[1],dizi66[2],dizi66[3],dizi66[4],dizi66[5]},
                {dizi67[0],dizi67[1],dizi67[2],dizi67[3],dizi67[4],dizi67[5]},
                {dizi68[0],dizi68[1],dizi68[2],dizi68[3],dizi68[4],dizi68[5]},
                {dizi69[0],dizi69[1],dizi69[2],dizi69[3],dizi69[4],dizi69[5]},
                {dizi70[0],dizi70[1],dizi70[2],dizi70[3],dizi70[4],dizi70[5]},
                {dizi71[0],dizi71[1],dizi71[2],dizi71[3],dizi71[4],dizi71[5]},
                {dizi72[0],dizi72[1],dizi72[2],dizi72[3],dizi72[4],dizi72[5]},
                {dizi73[0],dizi73[1],dizi73[2],dizi73[3],dizi73[4],dizi73[5]},
                {dizi74[0],dizi74[1],dizi74[2],dizi74[3],dizi74[4],dizi74[5]},
                {dizi75[0],dizi75[1],dizi75[2],dizi75[3],dizi75[4],dizi75[5]},
                {dizi76[0],dizi76[1],dizi76[2],dizi76[3],dizi76[4],dizi76[5]},
                {dizi77[0],dizi77[1],dizi77[2],dizi77[3],dizi77[4],dizi77[5]},
                {dizi78[0],dizi78[1],dizi78[2],dizi78[3],dizi78[4],dizi78[5]},
                {dizi79[0],dizi79[1],dizi79[2],dizi79[3],dizi79[4],dizi79[5]},
                {dizi80[0],dizi80[1],dizi80[2],dizi80[3],dizi80[4],dizi80[5]},
                {dizi81[0],dizi81[1],dizi81[2],dizi81[3],dizi81[4],dizi81[5]},
                {dizi82[0],dizi82[1],dizi82[2],dizi82[3],dizi82[4],dizi82[5]},
                {dizi83[0],dizi83[1],dizi83[2],dizi83[3],dizi83[4],dizi83[5]},
                {dizi84[0],dizi84[1],dizi84[2],dizi84[3],dizi84[4],dizi84[5]},
                {dizi85[0],dizi85[1],dizi85[2],dizi85[3],dizi85[4],dizi85[5]},
                {dizi86[0],dizi86[1],dizi86[2],dizi86[3],dizi86[4],dizi86[5]},
                {dizi87[0],dizi87[1],dizi87[2],dizi87[3],dizi87[4],dizi87[5]},
                {dizi88[0],dizi88[1],dizi88[2],dizi88[3],dizi88[4],dizi88[5]},
                {dizi89[0],dizi89[1],dizi89[2],dizi89[3],dizi89[4],dizi89[5]},
                {dizi90[0],dizi90[1],dizi90[2],dizi90[3],dizi90[4],dizi90[5]},
                {dizi91[0],dizi91[1],dizi91[2],dizi91[3],dizi91[4],dizi91[5]},
                {dizi92[0],dizi92[1],dizi92[2],dizi92[3],dizi92[4],dizi92[5]},
                {dizi93[0],dizi93[1],dizi93[2],dizi93[3],dizi93[4],dizi93[5]},
                {dizi94[0],dizi94[1],dizi94[2],dizi94[3],dizi94[4],dizi94[5]},
                {dizi95[0],dizi95[1],dizi95[2],dizi95[3],dizi95[4],dizi95[5]},
                {dizi96[0],dizi96[1],dizi96[2],dizi96[3],dizi96[4],dizi96[5]},
                {dizi97[0],dizi97[1],dizi97[2],dizi97[3],dizi97[4],dizi97[5]},
                {dizi98[0],dizi98[1],dizi98[2],dizi98[3],dizi98[4],dizi98[5]},
                {dizi99[0],dizi99[1],dizi99[2],dizi99[3],dizi99[4],dizi99[5]},
                {dizi100[0],dizi100[1],dizi100[2],dizi100[3],dizi100[4],dizi100[5]},
                {dizi101[0],dizi101[1],dizi101[2],dizi101[3],dizi101[4],dizi101[5]},
                {dizi102[0],dizi102[1],dizi102[2],dizi102[3],dizi102[4],dizi102[5]},
                {dizi103[0],dizi103[1],dizi103[2],dizi103[3],dizi103[4],dizi103[5]},
                {dizi104[0],dizi104[1],dizi104[2],dizi104[3],dizi104[4],dizi104[5]},
                {dizi105[0],dizi105[1],dizi105[2],dizi105[3],dizi105[4],dizi105[5]},
                {dizi106[0],dizi106[1],dizi106[2],dizi106[3],dizi106[4],dizi106[5]},
                {dizi107[0],dizi107[1],dizi107[2],dizi107[3],dizi107[4],dizi107[5]},
                {dizi108[0],dizi108[1],dizi108[2],dizi108[3],dizi108[4],dizi108[5]},
                {dizi109[0],dizi109[1],dizi109[2],dizi109[3],dizi109[4],dizi109[5]},
                {dizi110[0],dizi110[1],dizi110[2],dizi110[3],dizi110[4],dizi110[5]},
                {dizi111[0],dizi111[1],dizi111[2],dizi111[3],dizi111[4],dizi111[5]},
                {dizi112[0],dizi112[1],dizi112[2],dizi112[3],dizi112[4],dizi112[5]},
                {dizi113[0],dizi113[1],dizi113[2],dizi113[3],dizi113[4],dizi113[5]},
                {dizi114[0],dizi114[1],dizi114[2],dizi114[3],dizi114[4],dizi114[5]},
                {dizi115[0],dizi115[1],dizi115[2],dizi115[3],dizi115[4],dizi115[5]},
                {dizi116[0],dizi116[1],dizi116[2],dizi116[3],dizi116[4],dizi116[5]},
                {dizi117[0],dizi117[1],dizi117[2],dizi117[3],dizi117[4],dizi117[5]},
                {dizi118[0],dizi118[1],dizi118[2],dizi118[3],dizi118[4],dizi118[5]},
                {dizi119[0],dizi119[1],dizi119[2],dizi119[3],dizi119[4],dizi119[5]},
                {dizi120[0],dizi120[1],dizi120[2],dizi120[3],dizi120[4],dizi120[5]},
                {dizi121[0],dizi121[1],dizi121[2],dizi121[3],dizi121[4],dizi121[5]},
                {dizi122[0],dizi122[1],dizi122[2],dizi122[3],dizi122[4],dizi122[5]},
                {dizi123[0],dizi123[1],dizi123[2],dizi123[3],dizi123[4],dizi123[5]},
                {dizi124[0],dizi124[1],dizi124[2],dizi124[3],dizi124[4],dizi124[5]},
                {dizi125[0],dizi125[1],dizi125[2],dizi125[3],dizi125[4],dizi125[5]},
                {dizi126[0],dizi126[1],dizi126[2],dizi126[3],dizi126[4],dizi126[5]},
                {dizi127[0],dizi127[1],dizi127[2],dizi127[3],dizi127[4],dizi127[5]},
                {dizi128[0],dizi128[1],dizi128[2],dizi128[3],dizi128[4],dizi128[5]},
                {dizi129[0],dizi129[1],dizi129[2],dizi129[3],dizi129[4],dizi129[5]},
                {dizi130[0],dizi130[1],dizi130[2],dizi130[3],dizi130[4],dizi130[5]},
                {dizi131[0],dizi131[1],dizi131[2],dizi131[3],dizi131[4],dizi131[5]},
                {dizi132[0],dizi132[1],dizi132[2],dizi132[3],dizi132[4],dizi132[5]},
                {dizi133[0],dizi133[1],dizi133[2],dizi133[3],dizi133[4],dizi133[5]},
                {dizi134[0],dizi134[1],dizi134[2],dizi134[3],dizi134[4],dizi134[5]},
                {dizi135[0],dizi135[1],dizi135[2],dizi135[3],dizi135[4],dizi135[5]},
                {dizi136[0],dizi136[1],dizi136[2],dizi136[3],dizi136[4],dizi136[5]},
                {dizi137[0],dizi137[1],dizi137[2],dizi137[3],dizi137[4],dizi137[5]},
                {dizi138[0],dizi138[1],dizi138[2],dizi138[3],dizi138[4],dizi138[5]},
                {dizi139[0],dizi139[1],dizi139[2],dizi139[3],dizi139[4],dizi139[5]},
                {dizi140[0],dizi140[1],dizi140[2],dizi140[3],dizi140[4],dizi140[5]},
                {dizi141[0],dizi141[1],dizi141[2],dizi141[3],dizi141[4],dizi141[5]},
                {dizi142[0],dizi142[1],dizi142[2],dizi142[3],dizi142[4],dizi142[5]},
                {dizi143[0],dizi143[1],dizi143[2],dizi143[3],dizi143[4],dizi143[5]},
                {dizi144[0],dizi144[1],dizi144[2],dizi144[3],dizi144[4],dizi144[5]},
                {dizi145[0],dizi145[1],dizi145[2],dizi145[3],dizi145[4],dizi145[5]},
                {dizi146[0],dizi146[1],dizi146[2],dizi146[3],dizi146[4],dizi146[5]},
                {dizi147[0],dizi147[1],dizi147[2],dizi147[3],dizi147[4],dizi147[5]},
                {dizi148[0],dizi148[1],dizi148[2],dizi148[3],dizi148[4],dizi148[5]},
                {dizi149[0],dizi149[1],dizi149[2],dizi149[3],dizi149[4],dizi149[5]},
                {dizi150[0],dizi150[1],dizi150[2],dizi150[3],dizi150[4],dizi150[5]},
                {dizi151[0],dizi151[1],dizi151[2],dizi151[3],dizi151[4],dizi151[5]},
                {dizi152[0],dizi152[1],dizi152[2],dizi152[3],dizi152[4],dizi152[5]},
                {dizi153[0],dizi153[1],dizi153[2],dizi153[3],dizi153[4],dizi153[5]},
                {dizi154[0],dizi154[1],dizi154[2],dizi154[3],dizi154[4],dizi154[5]},
                {dizi155[0],dizi155[1],dizi155[2],dizi155[3],dizi155[4],dizi155[5]},
                {dizi156[0],dizi156[1],dizi156[2],dizi156[3],dizi156[4],dizi156[5]},
                {dizi157[0],dizi157[1],dizi157[2],dizi157[3],dizi157[4],dizi157[5]},
                {dizi158[0],dizi158[1],dizi158[2],dizi158[3],dizi158[4],dizi158[5]},
                {dizi159[0],dizi159[1],dizi159[2],dizi159[3],dizi159[4],dizi159[5]},
                {dizi160[0],dizi160[1],dizi160[2],dizi160[3],dizi160[4],dizi160[5]},
                {dizi161[0],dizi161[1],dizi161[2],dizi161[3],dizi161[4],dizi161[5]},
                {dizi162[0],dizi162[1],dizi162[2],dizi162[3],dizi162[4],dizi162[5]},
                {dizi163[0],dizi163[1],dizi163[2],dizi163[3],dizi163[4],dizi163[5]},
                {dizi164[0],dizi164[1],dizi164[2],dizi164[3],dizi164[4],dizi164[5]},
                {dizi165[0],dizi165[1],dizi165[2],dizi165[3],dizi165[4],dizi165[5]},
                {dizi166[0],dizi166[1],dizi166[2],dizi166[3],dizi166[4],dizi166[5]},
                {dizi167[0],dizi167[1],dizi167[2],dizi167[3],dizi167[4],dizi167[5]},
                {dizi168[0],dizi168[1],dizi168[2],dizi168[3],dizi168[4],dizi168[5]},
                {dizi169[0],dizi169[1],dizi169[2],dizi169[3],dizi169[4],dizi169[5]},
                {dizi170[0],dizi170[1],dizi170[2],dizi170[3],dizi170[4],dizi170[5]},
                {dizi171[0],dizi171[1],dizi171[2],dizi171[3],dizi171[4],dizi171[5]},
                {dizi172[0],dizi172[1],dizi172[2],dizi172[3],dizi172[4],dizi172[5]},
                {dizi173[0],dizi173[1],dizi173[2],dizi173[3],dizi173[4],dizi173[5]},
                {dizi174[0],dizi174[1],dizi174[2],dizi174[3],dizi174[4],dizi174[5]},
                {dizi175[0],dizi175[1],dizi175[2],dizi175[3],dizi175[4],dizi175[5]},
                {dizi176[0],dizi176[1],dizi176[2],dizi176[3],dizi176[4],dizi176[5]},
                {dizi177[0],dizi177[1],dizi177[2],dizi177[3],dizi177[4],dizi177[5]},
                {dizi178[0],dizi178[1],dizi178[2],dizi178[3],dizi178[4],dizi178[5]},
                {dizi179[0],dizi179[1],dizi179[2],dizi179[3],dizi179[4],dizi179[5]},
                {dizi180[0],dizi180[1],dizi180[2],dizi180[3],dizi180[4],dizi180[5]},
                {dizi181[0],dizi181[1],dizi181[2],dizi181[3],dizi181[4],dizi181[5]},
                {dizi182[0],dizi182[1],dizi182[2],dizi182[3],dizi182[4],dizi182[5]},
                {dizi183[0],dizi183[1],dizi183[2],dizi183[3],dizi183[4],dizi183[5]},
                {dizi184[0],dizi184[1],dizi184[2],dizi184[3],dizi184[4],dizi184[5]},
                {dizi185[0],dizi185[1],dizi185[2],dizi185[3],dizi185[4],dizi185[5]},
                {dizi186[0],dizi186[1],dizi186[2],dizi186[3],dizi186[4],dizi186[5]},
                {dizi187[0],dizi187[1],dizi187[2],dizi187[3],dizi187[4],dizi187[5]},
                {dizi188[0],dizi188[1],dizi188[2],dizi188[3],dizi188[4],dizi188[5]},
                {dizi189[0],dizi189[1],dizi189[2],dizi189[3],dizi189[4],dizi189[5]},
                {dizi190[0],dizi190[1],dizi190[2],dizi190[3],dizi190[4],dizi190[5]},
                {dizi191[0],dizi191[1],dizi191[2],dizi191[3],dizi191[4],dizi191[5]},
                {dizi192[0],dizi192[1],dizi192[2],dizi192[3],dizi192[4],dizi192[5]},
                {dizi193[0],dizi193[1],dizi193[2],dizi193[3],dizi193[4],dizi193[5]},
                {dizi194[0],dizi194[1],dizi194[2],dizi194[3],dizi194[4],dizi194[5]},
                {dizi195[0],dizi195[1],dizi195[2],dizi195[3],dizi195[4],dizi195[5]},
                {dizi196[0],dizi196[1],dizi196[2],dizi196[3],dizi196[4],dizi196[5]},
                {dizi197[0],dizi197[1],dizi197[2],dizi197[3],dizi197[4],dizi197[5]},
                {dizi198[0],dizi198[1],dizi198[2],dizi198[3],dizi198[4],dizi198[5]},
                {dizi199[0],dizi199[1],dizi199[2],dizi199[3],dizi199[4],dizi199[5]},
                {dizi200[0],dizi200[1],dizi200[2],dizi200[3],dizi200[4],dizi200[5]},
                {dizi201[0],dizi201[1],dizi201[2],dizi201[3],dizi201[4],dizi201[5]},
                {dizi202[0],dizi202[1],dizi202[2],dizi202[3],dizi202[4],dizi202[5]},
                {dizi203[0],dizi203[1],dizi203[2],dizi203[3],dizi203[4],dizi203[5]},
                {dizi204[0],dizi204[1],dizi204[2],dizi204[3],dizi204[4],dizi204[5]},
                {dizi205[0],dizi205[1],dizi205[2],dizi205[3],dizi205[4],dizi205[5]},
                {dizi206[0],dizi206[1],dizi206[2],dizi206[3],dizi206[4],dizi206[5]},
                {dizi207[0],dizi207[1],dizi207[2],dizi207[3],dizi207[4],dizi207[5]},
                {dizi208[0],dizi208[1],dizi208[2],dizi208[3],dizi208[4],dizi208[5]},
                {dizi209[0],dizi209[1],dizi209[2],dizi209[3],dizi209[4],dizi209[5]},
                {dizi210[0],dizi210[1],dizi210[2],dizi210[3],dizi210[4],dizi210[5]},
                {dizi211[0],dizi211[1],dizi211[2],dizi211[3],dizi211[4],dizi211[5]},
                {dizi212[0],dizi212[1],dizi212[2],dizi212[3],dizi212[4],dizi212[5]},
                {dizi213[0],dizi213[1],dizi213[2],dizi213[3],dizi213[4],dizi213[5]},
                {dizi214[0],dizi214[1],dizi214[2],dizi214[3],dizi214[4],dizi214[5]},
                {dizi215[0],dizi215[1],dizi215[2],dizi215[3],dizi215[4],dizi215[5]},
                {dizi216[0],dizi216[1],dizi216[2],dizi216[3],dizi216[4],dizi216[5]},
                {dizi217[0],dizi217[1],dizi217[2],dizi217[3],dizi217[4],dizi217[5]},
                {dizi218[0],dizi218[1],dizi218[2],dizi218[3],dizi218[4],dizi218[5]},
                {dizi219[0],dizi219[1],dizi219[2],dizi219[3],dizi219[4],dizi219[5]},
                {dizi220[0],dizi220[1],dizi220[2],dizi220[3],dizi220[4],dizi220[5]},
                {dizi221[0],dizi221[1],dizi221[2],dizi221[3],dizi221[4],dizi221[5]},
                {dizi222[0],dizi222[1],dizi222[2],dizi222[3],dizi222[4],dizi222[5]},
                {dizi223[0],dizi223[1],dizi223[2],dizi223[3],dizi223[4],dizi223[5]},
                {dizi224[0],dizi224[1],dizi224[2],dizi224[3],dizi224[4],dizi224[5]},
                {dizi225[0],dizi225[1],dizi225[2],dizi225[3],dizi225[4],dizi225[5]},
                {dizi226[0],dizi226[1],dizi226[2],dizi226[3],dizi226[4],dizi226[5]},
                {dizi227[0],dizi227[1],dizi227[2],dizi227[3],dizi227[4],dizi227[5]},
                {dizi228[0],dizi228[1],dizi228[2],dizi228[3],dizi228[4],dizi228[5]},
                {dizi229[0],dizi229[1],dizi229[2],dizi229[3],dizi229[4],dizi229[5]},
                {dizi230[0],dizi230[1],dizi230[2],dizi230[3],dizi230[4],dizi230[5]},
                {dizi231[0],dizi231[1],dizi231[2],dizi231[3],dizi231[4],dizi231[5]},
                {dizi232[0],dizi232[1],dizi232[2],dizi232[3],dizi232[4],dizi232[5]},
                {dizi233[0],dizi233[1],dizi233[2],dizi233[3],dizi233[4],dizi233[5]},
                {dizi234[0],dizi234[1],dizi234[2],dizi234[3],dizi234[4],dizi234[5]},
                {dizi235[0],dizi235[1],dizi235[2],dizi235[3],dizi235[4],dizi235[5]},
                {dizi236[0],dizi236[1],dizi236[2],dizi236[3],dizi236[4],dizi236[5]},
                {dizi237[0],dizi237[1],dizi237[2],dizi237[3],dizi237[4],dizi237[5]},
                {dizi238[0],dizi238[1],dizi238[2],dizi238[3],dizi238[4],dizi238[5]},
                {dizi239[0],dizi239[1],dizi239[2],dizi239[3],dizi239[4],dizi239[5]},
                {dizi240[0],dizi240[1],dizi240[2],dizi240[3],dizi240[4],dizi240[5]},
                {dizi241[0],dizi241[1],dizi241[2],dizi241[3],dizi241[4],dizi241[5]},
                {dizi242[0],dizi242[1],dizi242[2],dizi242[3],dizi242[4],dizi242[5]},
                {dizi243[0],dizi243[1],dizi243[2],dizi243[3],dizi243[4],dizi243[5]},
                {dizi244[0],dizi244[1],dizi244[2],dizi244[3],dizi244[4],dizi244[5]},
                {dizi245[0],dizi245[1],dizi245[2],dizi245[3],dizi245[4],dizi245[5]},
                {dizi246[0],dizi246[1],dizi246[2],dizi246[3],dizi246[4],dizi246[5]},
                {dizi247[0],dizi247[1],dizi247[2],dizi247[3],dizi247[4],dizi247[5]},
                {dizi248[0],dizi248[1],dizi248[2],dizi248[3],dizi248[4],dizi248[5]},
                {dizi249[0],dizi249[1],dizi249[2],dizi249[3],dizi249[4],dizi249[5]},
                {dizi250[0],dizi250[1],dizi250[2],dizi250[3],dizi250[4],dizi250[5]},
                {dizi251[0],dizi251[1],dizi251[2],dizi251[3],dizi251[4],dizi251[5]},
                {dizi252[0],dizi252[1],dizi252[2],dizi252[3],dizi252[4],dizi252[5]},
                {dizi253[0],dizi253[1],dizi253[2],dizi253[3],dizi253[4],dizi253[5]},
                {dizi254[0],dizi254[1],dizi254[2],dizi254[3],dizi254[4],dizi254[5]},
                {dizi255[0],dizi255[1],dizi255[2],dizi255[3],dizi255[4],dizi255[5]},
                {dizi256[0],dizi256[1],dizi256[2],dizi256[3],dizi256[4],dizi256[5]},
                {dizi257[0],dizi257[1],dizi257[2],dizi257[3],dizi257[4],dizi257[5]},
                {dizi258[0],dizi258[1],dizi258[2],dizi258[3],dizi258[4],dizi258[5]},
                {dizi259[0],dizi259[1],dizi259[2],dizi259[3],dizi259[4],dizi259[5]},
                {dizi260[0],dizi260[1],dizi260[2],dizi260[3],dizi260[4],dizi260[5]},
                {dizi261[0],dizi261[1],dizi261[2],dizi261[3],dizi261[4],dizi261[5]},
                {dizi262[0],dizi262[1],dizi262[2],dizi262[3],dizi262[4],dizi262[5]},
                {dizi263[0],dizi263[1],dizi263[2],dizi263[3],dizi263[4],dizi263[5]},
                {dizi264[0],dizi264[1],dizi264[2],dizi264[3],dizi264[4],dizi264[5]},
                {dizi265[0],dizi265[1],dizi265[2],dizi265[3],dizi265[4],dizi265[5]},
                {dizi266[0],dizi266[1],dizi266[2],dizi266[3],dizi266[4],dizi266[5]},
                {dizi267[0],dizi267[1],dizi267[2],dizi267[3],dizi267[4],dizi267[5]},
                {dizi268[0],dizi268[1],dizi268[2],dizi268[3],dizi268[4],dizi268[5]},
                {dizi269[0],dizi269[1],dizi269[2],dizi269[3],dizi269[4],dizi269[5]},
                {dizi270[0],dizi270[1],dizi270[2],dizi270[3],dizi270[4],dizi270[5]},
                {dizi271[0],dizi271[1],dizi271[2],dizi271[3],dizi271[4],dizi271[5]},
                {dizi272[0],dizi272[1],dizi272[2],dizi272[3],dizi272[4],dizi272[5]},
                {dizi273[0],dizi273[1],dizi273[2],dizi273[3],dizi273[4],dizi273[5]},
                {dizi274[0],dizi274[1],dizi274[2],dizi274[3],dizi274[4],dizi274[5]},
                {dizi275[0],dizi275[1],dizi275[2],dizi275[3],dizi275[4],dizi275[5]},
                {dizi276[0],dizi276[1],dizi276[2],dizi276[3],dizi276[4],dizi276[5]},
                {dizi277[0],dizi277[1],dizi277[2],dizi277[3],dizi277[4],dizi277[5]},
                {dizi278[0],dizi278[1],dizi278[2],dizi278[3],dizi278[4],dizi278[5]},
                {dizi279[0],dizi279[1],dizi279[2],dizi279[3],dizi279[4],dizi279[5]},
                {dizi280[0],dizi280[1],dizi280[2],dizi280[3],dizi280[4],dizi280[5]},
                {dizi281[0],dizi281[1],dizi281[2],dizi281[3],dizi281[4],dizi281[5]},
                {dizi282[0],dizi282[1],dizi282[2],dizi282[3],dizi282[4],dizi282[5]},
                {dizi283[0],dizi283[1],dizi283[2],dizi283[3],dizi283[4],dizi283[5]},
                {dizi284[0],dizi284[1],dizi284[2],dizi284[3],dizi284[4],dizi284[5]},
                {dizi285[0],dizi285[1],dizi285[2],dizi285[3],dizi285[4],dizi285[5]},
                {dizi286[0],dizi286[1],dizi286[2],dizi286[3],dizi286[4],dizi286[5]},
                {dizi287[0],dizi287[1],dizi287[2],dizi287[3],dizi287[4],dizi287[5]},
                {dizi288[0],dizi288[1],dizi288[2],dizi288[3],dizi288[4],dizi288[5]},
                {dizi289[0],dizi289[1],dizi289[2],dizi289[3],dizi289[4],dizi289[5]},
                {dizi290[0],dizi290[1],dizi290[2],dizi290[3],dizi290[4],dizi290[5]},
                {dizi291[0],dizi291[1],dizi291[2],dizi291[3],dizi291[4],dizi291[5]},
                {dizi292[0],dizi292[1],dizi292[2],dizi292[3],dizi292[4],dizi292[5]},
                {dizi293[0],dizi293[1],dizi293[2],dizi293[3],dizi293[4],dizi293[5]},
                {dizi294[0],dizi294[1],dizi294[2],dizi294[3],dizi294[4],dizi294[5]},
                {dizi295[0],dizi295[1],dizi295[2],dizi295[3],dizi295[4],dizi295[5]},
                {dizi296[0],dizi296[1],dizi296[2],dizi296[3],dizi296[4],dizi296[5]},
                {dizi297[0],dizi297[1],dizi297[2],dizi297[3],dizi297[4],dizi297[5]},
                {dizi298[0],dizi298[1],dizi298[2],dizi298[3],dizi298[4],dizi298[5]},
                {dizi299[0],dizi299[1],dizi299[2],dizi299[3],dizi299[4],dizi299[5]},
                {dizi300[0],dizi300[1],dizi300[2],dizi300[3],dizi300[4],dizi300[5]},
                {dizi301[0],dizi301[1],dizi301[2],dizi301[3],dizi301[4],dizi301[5]},
                {dizi302[0],dizi302[1],dizi302[2],dizi302[3],dizi302[4],dizi302[5]},
                {dizi303[0],dizi303[1],dizi303[2],dizi303[3],dizi303[4],dizi303[5]},
                {dizi304[0],dizi304[1],dizi304[2],dizi304[3],dizi304[4],dizi304[5]},
                {dizi305[0],dizi305[1],dizi305[2],dizi305[3],dizi305[4],dizi305[5]},
                {dizi306[0],dizi306[1],dizi306[2],dizi306[3],dizi306[4],dizi306[5]},
                {dizi307[0],dizi307[1],dizi307[2],dizi307[3],dizi307[4],dizi307[5]},
                {dizi308[0],dizi308[1],dizi308[2],dizi308[3],dizi308[4],dizi308[5]},
                {dizi309[0],dizi309[1],dizi309[2],dizi309[3],dizi309[4],dizi309[5]},
                {dizi310[0],dizi310[1],dizi310[2],dizi310[3],dizi310[4],dizi310[5]},
                {dizi311[0],dizi311[1],dizi311[2],dizi311[3],dizi311[4],dizi311[5]},
                {dizi312[0],dizi312[1],dizi312[2],dizi312[3],dizi312[4],dizi312[5]},
                {dizi313[0],dizi313[1],dizi313[2],dizi313[3],dizi313[4],dizi313[5]},
                {dizi314[0],dizi314[1],dizi314[2],dizi314[3],dizi314[4],dizi314[5]},
                {dizi315[0],dizi315[1],dizi315[2],dizi315[3],dizi315[4],dizi315[5]},
                {dizi316[0],dizi316[1],dizi316[2],dizi316[3],dizi316[4],dizi316[5]},
                {dizi317[0],dizi317[1],dizi317[2],dizi317[3],dizi317[4],dizi317[5]},
                {dizi318[0],dizi318[1],dizi318[2],dizi318[3],dizi318[4],dizi318[5]},
                {dizi319[0],dizi319[1],dizi319[2],dizi319[3],dizi319[4],dizi319[5]},
                {dizi320[0],dizi320[1],dizi320[2],dizi320[3],dizi320[4],dizi320[5]},
                {dizi321[0],dizi321[1],dizi321[2],dizi321[3],dizi321[4],dizi321[5]},
                {dizi322[0],dizi322[1],dizi322[2],dizi322[3],dizi322[4],dizi322[5]},
                {dizi323[0],dizi323[1],dizi323[2],dizi323[3],dizi323[4],dizi323[5]},
                {dizi324[0],dizi324[1],dizi324[2],dizi324[3],dizi324[4],dizi324[5]},
                {dizi325[0],dizi325[1],dizi325[2],dizi325[3],dizi325[4],dizi325[5]},
                {dizi326[0],dizi326[1],dizi326[2],dizi326[3],dizi326[4],dizi326[5]},
                {dizi327[0],dizi327[1],dizi327[2],dizi327[3],dizi327[4],dizi327[5]},
                {dizi328[0],dizi328[1],dizi328[2],dizi328[3],dizi328[4],dizi328[5]},
                {dizi329[0],dizi329[1],dizi329[2],dizi329[3],dizi329[4],dizi329[5]},
                {dizi330[0],dizi330[1],dizi330[2],dizi330[3],dizi330[4],dizi330[5]},
                {dizi331[0],dizi331[1],dizi331[2],dizi331[3],dizi331[4],dizi331[5]},
                {dizi332[0],dizi332[1],dizi332[2],dizi332[3],dizi332[4],dizi332[5]},
                {dizi333[0],dizi333[1],dizi333[2],dizi333[3],dizi333[4],dizi333[5]},
                {dizi334[0],dizi334[1],dizi334[2],dizi334[3],dizi334[4],dizi334[5]},
                {dizi335[0],dizi335[1],dizi335[2],dizi335[3],dizi335[4],dizi335[5]},
                {dizi336[0],dizi336[1],dizi336[2],dizi336[3],dizi336[4],dizi336[5]},
                {dizi337[0],dizi337[1],dizi337[2],dizi337[3],dizi337[4],dizi337[5]},
                {dizi338[0],dizi338[1],dizi338[2],dizi338[3],dizi338[4],dizi338[5]},
                {dizi339[0],dizi339[1],dizi339[2],dizi339[3],dizi339[4],dizi339[5]},
                {dizi340[0],dizi240[1],dizi340[2],dizi340[3],dizi340[4],dizi340[5]},
                {dizi341[0],dizi341[1],dizi341[2],dizi341[3],dizi341[4],dizi341[5]},
                {dizi342[0],dizi342[1],dizi342[2],dizi342[3],dizi342[4],dizi342[5]},
                {dizi343[0],dizi343[1],dizi343[2],dizi343[3],dizi343[4],dizi343[5]},
                {dizi344[0],dizi344[1],dizi344[2],dizi344[3],dizi344[4],dizi344[5]},
                {dizi345[0],dizi345[1],dizi345[2],dizi345[3],dizi345[4],dizi345[5]},
                {dizi346[0],dizi346[1],dizi346[2],dizi346[3],dizi346[4],dizi346[5]},
                {dizi347[0],dizi347[1],dizi347[2],dizi347[3],dizi347[4],dizi347[5]},
                {dizi348[0],dizi348[1],dizi348[2],dizi348[3],dizi348[4],dizi348[5]},
                {dizi349[0],dizi349[1],dizi349[2],dizi349[3],dizi349[4],dizi349[5]},
                {dizi350[0],dizi350[1],dizi350[2],dizi350[3],dizi350[4],dizi350[5]},
                {dizi351[0],dizi351[1],dizi351[2],dizi351[3],dizi351[4],dizi351[5]},
                {dizi352[0],dizi352[1],dizi352[2],dizi352[3],dizi352[4],dizi352[5]},
                {dizi353[0],dizi353[1],dizi353[2],dizi353[3],dizi353[4],dizi353[5]},
                {dizi354[0],dizi354[1],dizi354[2],dizi354[3],dizi354[4],dizi354[5]},
                {dizi355[0],dizi355[1],dizi355[2],dizi355[3],dizi355[4],dizi355[5]},
                {dizi356[0],dizi356[1],dizi356[2],dizi356[3],dizi356[4],dizi356[5]},
                {dizi357[0],dizi357[1],dizi357[2],dizi357[3],dizi357[4],dizi357[5]},
                {dizi358[0],dizi358[1],dizi358[2],dizi358[3],dizi358[4],dizi358[5]},
                {dizi359[0],dizi359[1],dizi359[2],dizi359[3],dizi359[4],dizi359[5]},
                {dizi360[0],dizi360[1],dizi360[2],dizi360[3],dizi360[4],dizi360[5]},
                {dizi361[0],dizi361[1],dizi361[2],dizi361[3],dizi361[4],dizi361[5]},
                {dizi362[0],dizi362[1],dizi362[2],dizi362[3],dizi362[4],dizi362[5]},
                {dizi363[0],dizi363[1],dizi363[2],dizi363[3],dizi363[4],dizi363[5]},
                {dizi364[0],dizi364[1],dizi364[2],dizi364[3],dizi364[4],dizi364[5]},
                {dizi365[0],dizi365[1],dizi365[2],dizi365[3],dizi365[4],dizi365[5]},
                {dizi366[0],dizi366[1],dizi366[2],dizi366[3],dizi366[4],dizi366[5]},
                {dizi367[0],dizi367[1],dizi367[2],dizi367[3],dizi367[4],dizi367[5]},
                {dizi368[0],dizi368[1],dizi368[2],dizi368[3],dizi368[4],dizi368[5]},
                {dizi369[0],dizi369[1],dizi369[2],dizi369[3],dizi369[4],dizi369[5]},
                {dizi370[0],dizi370[1],dizi370[2],dizi370[3],dizi370[4],dizi370[5]},
                {dizi371[0],dizi371[1],dizi371[2],dizi371[3],dizi371[4],dizi371[5]},
                {dizi372[0],dizi372[1],dizi372[2],dizi372[3],dizi372[4],dizi372[5]},
                {dizi373[0],dizi373[1],dizi373[2],dizi373[3],dizi373[4],dizi373[5]},
                {dizi374[0],dizi374[1],dizi374[2],dizi374[3],dizi374[4],dizi374[5]},
                {dizi375[0],dizi375[1],dizi375[2],dizi375[3],dizi375[4],dizi375[5]},
                {dizi376[0],dizi376[1],dizi376[2],dizi376[3],dizi376[4],dizi376[5]},
                {dizi377[0],dizi377[1],dizi377[2],dizi377[3],dizi377[4],dizi377[5]},
                {dizi378[0],dizi378[1],dizi378[2],dizi378[3],dizi378[4],dizi378[5]},
                {dizi379[0],dizi379[1],dizi379[2],dizi379[3],dizi379[4],dizi379[5]},
                {dizi380[0],dizi380[1],dizi380[2],dizi380[3],dizi380[4],dizi380[5]},
                {dizi381[0],dizi381[1],dizi381[2],dizi381[3],dizi381[4],dizi381[5]},
                {dizi382[0],dizi382[1],dizi382[2],dizi382[3],dizi382[4],dizi382[5]},
                {dizi383[0],dizi383[1],dizi383[2],dizi383[3],dizi383[4],dizi383[5]},
                {dizi384[0],dizi384[1],dizi384[2],dizi384[3],dizi384[4],dizi384[5]},
                {dizi385[0],dizi385[1],dizi385[2],dizi385[3],dizi385[4],dizi385[5]},
                {dizi386[0],dizi386[1],dizi386[2],dizi386[3],dizi386[4],dizi386[5]},
                {dizi387[0],dizi387[1],dizi387[2],dizi387[3],dizi387[4],dizi387[5]},
                {dizi388[0],dizi388[1],dizi388[2],dizi388[3],dizi388[4],dizi388[5]},
                {dizi389[0],dizi389[1],dizi389[2],dizi389[3],dizi389[4],dizi389[5]},
                {dizi390[0],dizi390[1],dizi390[2],dizi390[3],dizi390[4],dizi390[5]},
                {dizi391[0],dizi391[1],dizi391[2],dizi391[3],dizi391[4],dizi391[5]},
                {dizi392[0],dizi392[1],dizi392[2],dizi392[3],dizi392[4],dizi392[5]},
                {dizi393[0],dizi393[1],dizi393[2],dizi393[3],dizi393[4],dizi393[5]},
                {dizi394[0],dizi394[1],dizi394[2],dizi394[3],dizi394[4],dizi394[5]},
                {dizi395[0],dizi395[1],dizi395[2],dizi395[3],dizi395[4],dizi395[5]},
                {dizi396[0],dizi396[1],dizi396[2],dizi396[3],dizi396[4],dizi396[5]},
                {dizi397[0],dizi397[1],dizi397[2],dizi397[3],dizi397[4],dizi397[5]},
                {dizi398[0],dizi398[1],dizi398[2],dizi398[3],dizi398[4],dizi398[5]},
                {dizi399[0],dizi399[1],dizi399[2],dizi399[3],dizi399[4],dizi399[5]},
                {dizi400[0],dizi400[1],dizi400[2],dizi400[3],dizi400[4],dizi400[5]},
                {dizi401[0],dizi401[1],dizi401[2],dizi401[3],dizi401[4],dizi401[5]},
                {dizi402[0],dizi402[1],dizi402[2],dizi402[3],dizi402[4],dizi402[5]},
                {dizi403[0],dizi403[1],dizi403[2],dizi403[3],dizi403[4],dizi403[5]},
                {dizi404[0],dizi404[1],dizi404[2],dizi404[3],dizi404[4],dizi404[5]},
                {dizi405[0],dizi405[1],dizi405[2],dizi405[3],dizi405[4],dizi405[5]},
                {dizi406[0],dizi406[1],dizi406[2],dizi406[3],dizi406[4],dizi406[5]},
                {dizi407[0],dizi407[1],dizi407[2],dizi407[3],dizi407[4],dizi407[5]},
                {dizi408[0],dizi408[1],dizi408[2],dizi408[3],dizi408[4],dizi408[5]},
                {dizi409[0],dizi409[1],dizi409[2],dizi409[3],dizi409[4],dizi409[5]},
                {dizi410[0],dizi410[1],dizi410[2],dizi410[3],dizi410[4],dizi410[5]},
                {dizi411[0],dizi411[1],dizi411[2],dizi411[3],dizi411[4],dizi411[5]},
                {dizi412[0],dizi412[1],dizi412[2],dizi412[3],dizi412[4],dizi412[5]},
                {dizi413[0],dizi413[1],dizi413[2],dizi413[3],dizi413[4],dizi413[5]},
                {dizi414[0],dizi414[1],dizi414[2],dizi414[3],dizi414[4],dizi414[5]},
                {dizi415[0],dizi415[1],dizi415[2],dizi415[3],dizi415[4],dizi415[5]},
                {dizi416[0],dizi416[1],dizi416[2],dizi416[3],dizi416[4],dizi416[5]},
                {dizi417[0],dizi417[1],dizi417[2],dizi417[3],dizi417[4],dizi417[5]},
                {dizi418[0],dizi418[1],dizi418[2],dizi418[3],dizi418[4],dizi418[5]},
                {dizi419[0],dizi419[1],dizi419[2],dizi419[3],dizi419[4],dizi419[5]},
                {dizi420[0],dizi420[1],dizi420[2],dizi420[3],dizi420[4],dizi420[5]},
                {dizi421[0],dizi421[1],dizi421[2],dizi421[3],dizi421[4],dizi421[5]},
                {dizi422[0],dizi422[1],dizi422[2],dizi422[3],dizi422[4],dizi422[5]},
                {dizi423[0],dizi423[1],dizi423[2],dizi423[3],dizi423[4],dizi423[5]},
                {dizi424[0],dizi424[1],dizi424[2],dizi424[3],dizi424[4],dizi424[5]},
                {dizi425[0],dizi425[1],dizi425[2],dizi425[3],dizi425[4],dizi425[5]},
                {dizi426[0],dizi426[1],dizi426[2],dizi426[3],dizi426[4],dizi426[5]},
                {dizi427[0],dizi427[1],dizi427[2],dizi427[3],dizi427[4],dizi427[5]},
                {dizi428[0],dizi428[1],dizi428[2],dizi428[3],dizi428[4],dizi428[5]},
                {dizi429[0],dizi429[1],dizi429[2],dizi429[3],dizi429[4],dizi429[5]},
                {dizi430[0],dizi430[1],dizi430[2],dizi430[3],dizi430[4],dizi430[5]},
                {dizi431[0],dizi431[1],dizi431[2],dizi431[3],dizi431[4],dizi431[5]},
                {dizi432[0],dizi432[1],dizi432[2],dizi432[3],dizi432[4],dizi432[5]},
                {dizi433[0],dizi433[1],dizi433[2],dizi433[3],dizi433[4],dizi433[5]},
                {dizi434[0],dizi434[1],dizi434[2],dizi434[3],dizi434[4],dizi434[5]},
                {dizi435[0],dizi435[1],dizi435[2],dizi435[3],dizi435[4],dizi435[5]},
                {dizi436[0],dizi436[1],dizi436[2],dizi436[3],dizi436[4],dizi436[5]},
                {dizi437[0],dizi437[1],dizi437[2],dizi437[3],dizi437[4],dizi437[5]},
                {dizi438[0],dizi438[1],dizi438[2],dizi438[3],dizi438[4],dizi438[5]},
                {dizi439[0],dizi439[1],dizi439[2],dizi439[3],dizi439[4],dizi439[5]},
                {dizi440[0],dizi440[1],dizi440[2],dizi440[3],dizi440[4],dizi440[5]},
                {dizi441[0],dizi441[1],dizi441[2],dizi441[3],dizi441[4],dizi441[5]},
                {dizi442[0],dizi442[1],dizi442[2],dizi442[3],dizi442[4],dizi442[5]},
                {dizi443[0],dizi443[1],dizi443[2],dizi443[3],dizi443[4],dizi443[5]},
                {dizi444[0],dizi444[1],dizi444[2],dizi444[3],dizi444[4],dizi444[5]},
                {dizi445[0],dizi445[1],dizi445[2],dizi445[3],dizi445[4],dizi445[5]},
                {dizi446[0],dizi446[1],dizi446[2],dizi446[3],dizi446[4],dizi446[5]},
                {dizi447[0],dizi447[1],dizi447[2],dizi447[3],dizi447[4],dizi447[5]},
                {dizi448[0],dizi448[1],dizi448[2],dizi448[3],dizi448[4],dizi448[5]},
                {dizi449[0],dizi449[1],dizi449[2],dizi449[3],dizi449[4],dizi449[5]},
                {dizi450[0],dizi450[1],dizi450[2],dizi450[3],dizi450[4],dizi450[5]},
                {dizi451[0],dizi451[1],dizi451[2],dizi451[3],dizi451[4],dizi451[5]},
                {dizi452[0],dizi452[1],dizi452[2],dizi452[3],dizi452[4],dizi452[5]},
                {dizi453[0],dizi453[1],dizi453[2],dizi453[3],dizi453[4],dizi453[5]},
                {dizi454[0],dizi454[1],dizi454[2],dizi454[3],dizi454[4],dizi454[5]},
                {dizi455[0],dizi455[1],dizi455[2],dizi455[3],dizi455[4],dizi455[5]},
                {dizi456[0],dizi456[1],dizi456[2],dizi456[3],dizi456[4],dizi456[5]},
                {dizi457[0],dizi457[1],dizi457[2],dizi457[3],dizi457[4],dizi457[5]},
                {dizi458[0],dizi458[1],dizi458[2],dizi458[3],dizi458[4],dizi458[5]},
                {dizi459[0],dizi459[1],dizi459[2],dizi459[3],dizi459[4],dizi459[5]},
                {dizi460[0],dizi460[1],dizi460[2],dizi460[3],dizi460[4],dizi460[5]},
                {dizi461[0],dizi461[1],dizi461[2],dizi461[3],dizi461[4],dizi461[5]},
                {dizi462[0],dizi462[1],dizi462[2],dizi462[3],dizi462[4],dizi462[5]},
                {dizi463[0],dizi463[1],dizi463[2],dizi463[3],dizi463[4],dizi463[5]},
                {dizi464[0],dizi464[1],dizi464[2],dizi464[3],dizi464[4],dizi464[5]},
                {dizi465[0],dizi465[1],dizi465[2],dizi465[3],dizi465[4],dizi465[5]},
                {dizi466[0],dizi466[1],dizi466[2],dizi466[3],dizi466[4],dizi466[5]},
                {dizi467[0],dizi467[1],dizi467[2],dizi467[3],dizi467[4],dizi467[5]},
                {dizi468[0],dizi468[1],dizi468[2],dizi468[3],dizi468[4],dizi468[5]},
                {dizi469[0],dizi469[1],dizi469[2],dizi469[3],dizi469[4],dizi469[5]},
                {dizi470[0],dizi470[1],dizi470[2],dizi470[3],dizi470[4],dizi470[5]},
                {dizi471[0],dizi471[1],dizi471[2],dizi471[3],dizi471[4],dizi471[5]},
                {dizi472[0],dizi472[1],dizi472[2],dizi472[3],dizi472[4],dizi472[5]},
                {dizi473[0],dizi473[1],dizi473[2],dizi473[3],dizi473[4],dizi473[5]},
                {dizi474[0],dizi474[1],dizi474[2],dizi474[3],dizi474[4],dizi474[5]},
                {dizi475[0],dizi475[1],dizi475[2],dizi475[3],dizi475[4],dizi475[5]},
                {dizi476[0],dizi476[1],dizi476[2],dizi476[3],dizi476[4],dizi476[5]},
                {dizi477[0],dizi477[1],dizi477[2],dizi477[3],dizi477[4],dizi477[5]},
                {dizi478[0],dizi478[1],dizi478[2],dizi478[3],dizi478[4],dizi478[5]},
                {dizi479[0],dizi479[1],dizi479[2],dizi479[3],dizi479[4],dizi479[5]},
                {dizi480[0],dizi480[1],dizi480[2],dizi480[3],dizi480[4],dizi480[5]},
                {dizi481[0],dizi481[1],dizi481[2],dizi481[3],dizi481[4],dizi481[5]},
                {dizi482[0],dizi482[1],dizi482[2],dizi482[3],dizi482[4],dizi482[5]},
                {dizi483[0],dizi483[1],dizi483[2],dizi483[3],dizi483[4],dizi483[5]},
                {dizi484[0],dizi484[1],dizi484[2],dizi484[3],dizi484[4],dizi484[5]},
                {dizi485[0],dizi485[1],dizi485[2],dizi485[3],dizi485[4],dizi485[5]},
                {dizi486[0],dizi486[1],dizi486[2],dizi486[3],dizi486[4],dizi486[5]},
                {dizi487[0],dizi487[1],dizi487[2],dizi487[3],dizi487[4],dizi487[5]},
                {dizi488[0],dizi488[1],dizi488[2],dizi488[3],dizi488[4],dizi488[5]},
                {dizi489[0],dizi489[1],dizi489[2],dizi489[3],dizi489[4],dizi489[5]},
                {dizi490[0],dizi490[1],dizi490[2],dizi490[3],dizi490[4],dizi490[5]},
                {dizi491[0],dizi491[1],dizi491[2],dizi491[3],dizi491[4],dizi491[5]},
                {dizi492[0],dizi492[1],dizi492[2],dizi492[3],dizi492[4],dizi492[5]},
                {dizi493[0],dizi493[1],dizi493[2],dizi493[3],dizi493[4],dizi493[5]},
                {dizi494[0],dizi494[1],dizi494[2],dizi494[3],dizi494[4],dizi494[5]},
                {dizi495[0],dizi495[1],dizi495[2],dizi495[3],dizi495[4],dizi495[5]},
                {dizi496[0],dizi496[1],dizi496[2],dizi496[3],dizi496[4],dizi496[5]},
                {dizi497[0],dizi497[1],dizi497[2],dizi497[3],dizi497[4],dizi497[5]},
                {dizi498[0],dizi498[1],dizi498[2],dizi498[3],dizi498[4],dizi498[5]},
                {dizi499[0],dizi499[1],dizi499[2],dizi499[3],dizi499[4],dizi499[5]},
                {dizi500[0],dizi500[1],dizi500[2],dizi500[3],dizi500[4],dizi500[5]},
        };




        anaKelime.setText(diziler[Sorusec[dizi]][0]);
        yasak1.setText(diziler[Sorusec[dizi]][1]);
        yasak2.setText(diziler[Sorusec[dizi]][2]);
        yasak3.setText(diziler[Sorusec[dizi]][3]);
        yasak4.setText(diziler[Sorusec[dizi]][4]);
        yasak5.setText(diziler[Sorusec[dizi]][5]);

        kelimeSayisi++;
        dizi++;

        if(kelimeSayisi%500==0){ //Kelime Sayısından Bir Eksik
            cagir();
            dizi=0;
        }


    }

    private void cagir(){

        for (int i = 1; i < 501; i++){  //Kelime Sayısı Kadar
            sayi = (int) (Math.random() * 501);
            for (int j = 0; j <= i; j++){
                if (Sorusec[j] == sayi) {
                    sayi = (int) (Math.random() * 501);
                    j=0;
                }
            }
            Sorusec[i] = sayi;
        }

    }

}