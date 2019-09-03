package com.demo.kirgaz.barcodekirgaz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.demo.kirgaz.barcodekirgaz.Fragmentler.BarkodFragment;
import com.demo.kirgaz.barcodekirgaz.Siniflar.DBBaglanti;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

public class Giris extends AppCompatActivity {

    private Button gerisBtn;
    private Intent intent;
    private Spinner userNameSP;
    private EditText Password;

    private String secilenUser, pass;
    private RadioGroup myrdGroup;
    private DBBaglanti dbBaglanti;
    private Context context;
    private String ekranPassword;

    private int girisKodu;

    private String [] userNames;
    private ArrayAdapter<String> usersArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        gerisBtn = findViewById(R.id.btnLogin);
        userNameSP = findViewById(R.id.spKullanici);
        Password = findViewById(R.id.etPass);
        myrdGroup = findViewById(R.id.rdGoup);
        context = Giris.this;
        dbBaglanti=new DBBaglanti(context);
        girisKodu = 71;
        String sart=" sehir= 'Kirikkale'";
        userNames=dbBaglanti.getVeri("giris_bilgi","userName",sart);
        userSpinnerOlustur(userNames);

        myrdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rdbtnKirikkale) {


                    Password.setText("");
                    girisKodu = 71;
                    String sart=" sehir= 'Kirikkale'";
                    userNames=dbBaglanti.getVeri("giris_bilgi","userName",sart);
                    userSpinnerOlustur(userNames);

                } else if (checkedId == R.id.rdbtnKirsehir) {

                    Password.setText("");

                    girisKodu = 40;
                    String sart=" sehir= 'Kirsehir'";
                    userNames=dbBaglanti.getVeri("giris_bilgi","userName",sart);
                    userSpinnerOlustur(userNames);


                }
            }
        });


    }
    public void userSpinnerOlustur(String[] userNames){
        usersArrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, userNames);
        usersArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userNameSP.setAdapter(usersArrayAdapter);
        userNameSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilenUser = parent.getItemAtPosition(position).toString();
                gerisBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ekranPassword=Password.getText().toString();
                        if (girisKodu == 71) {
                            String sart=" sehir= 'Kirikkale' and userName='"+secilenUser+"'";
                            pass=dbBaglanti.getVeri("giris_bilgi","password",sart)[0];
                            System.out.println("P@@@@@@@@@@@@@s"+pass);
                            if(pass.equals(ekranPassword)){
                                intent=new Intent(Giris.this, BarKodkirgaz.class);
                                intent.putExtra("user", secilenUser);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(context,"Girdiniz Parola Yanlış, Tekrar deneyebilir misiniz...",Toast.LENGTH_LONG).show();
                            }
                        } else if (girisKodu == 40) {
                            String sart=" sehir= 'Kirsehir' and userName='"+secilenUser+"'";
                            pass=dbBaglanti.getVeri("giris_bilgi","password",sart)[0];
                            if(pass.equals(ekranPassword)){
                                intent=new Intent(Giris.this, BarKodkirgaz.class);
                                intent.putExtra("user", secilenUser);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(context,"Girdiniz Parola Yanlış, Tekrar deneyebilir misiniz...",Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                });




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }


}


