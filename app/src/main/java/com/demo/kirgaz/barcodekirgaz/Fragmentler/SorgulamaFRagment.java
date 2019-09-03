package com.demo.kirgaz.barcodekirgaz.Fragmentler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.kirgaz.barcodekirgaz.Adapterler.MalzemeListAdapter;
import com.demo.kirgaz.barcodekirgaz.BarKodkirgaz;
import com.demo.kirgaz.barcodekirgaz.R;
import com.demo.kirgaz.barcodekirgaz.Siniflar.DBBaglanti;
import com.demo.kirgaz.barcodekirgaz.Siniflar.Malzeme;
import com.demo.kirgaz.barcodekirgaz.Siniflar.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SorgulamaFRagment extends Fragment implements View.OnClickListener{
    private DBBaglanti dbBaglanti;
    private View view;
    private RecyclerView recyclerView;
    private Button btnGeri, btnRaporOlustur;
    private Spinner kriter, secenekler;
    private String kriterVeri[]={"Malzeme Giriş/Çikiş", "Malzeme Groupu", "Malzeme Adı", "Malzeme Stock Kodu", "Malzeme Giriş/Çikiş yeri", "Malzeme Teslim Alan", "Malzeme Teslim Eden", "Malzeme Giriş/Çikiş Tarihi"};
    private String seceneklerVeri[];
    private ArrayAdapter<String> kriterArrAdapter, seceneklerArrAdapter;
    private String selectedItem;
    private final String islemTuruVeri[]={"GİRİŞ","ÇIKIŞ"};
    private List<Malzeme> malzemeLis;
    private MalzemeListAdapter mAdapter;
    private String dbColumn;
    private TextView gececiTarih;
    final private int PERMISSION_REQUEST_CODE = 123;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sorgulama_fragment, container, false);
        recyclerView = view.findViewById(R.id.rvSorguListe);
        btnGeri=view.findViewById(R.id.btnSorgGer);
        btnRaporOlustur=view.findViewById(R.id.btnSorgCSVDosyaExport);
        btnGeri.setOnClickListener(this);
        btnRaporOlustur.setOnClickListener(this);
        kriter=view.findViewById(R.id.KriterSP);
        secenekler=view.findViewById(R.id.secenekSP);
        dbBaglanti=new DBBaglanti(getContext());
        gececiTarih=view.findViewById(R.id.tvSelectedTarih);



        kriterArrAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, kriterVeri);
        kriterArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kriter.setAdapter(kriterArrAdapter);
        kriter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem=parent.getItemAtPosition(position).toString();
                System.out.println("########################"+selectedItem);
                switch (selectedItem){
                    case "Malzeme Giriş/Çikiş":
                        //seceneklerVeri[0]="GİRİŞ" ;
                        //seceneklerVeri[1]="ÇIKIŞ";
                        dbColumn="islem_Turu";
                        seceneklerSpinnerOlustur(islemTuruVeri);


                        break;
                    case "Malzeme Groupu":
                        dbColumn="MalzemeGroupu";
                        seceneklerVeri=dbBaglanti.getVeri("Malzeme_Kategori","MalzemeGroupu","");
                        seceneklerSpinnerOlustur(seceneklerVeri);

                        break;
                    case "Malzeme Adı":
                        dbColumn="MalzemeAdi";
                        seceneklerVeri=dbBaglanti.getVeri("cikan_malzemeler","MalzemeAdi","");
                        seceneklerSpinnerOlustur(seceneklerVeri);

                        break;
                    case "Malzeme Stock Kodu":
                        dbColumn="Stok_Kodu";
                        seceneklerVeri=dbBaglanti.getVeri("Malzeme_Kategori","Stok_Kodu","");
                        seceneklerSpinnerOlustur(seceneklerVeri);

                        break;
                    case "Malzeme Giriş/Çikiş yeri":
                        dbColumn="cikis_yer";
                        seceneklerVeri=dbBaglanti.getVeri("cikan_malzemeler","cikis_yer","");
                        seceneklerSpinnerOlustur(seceneklerVeri);


                        break;
                    case "Malzeme Teslim Alan":
                        dbColumn="Teslim_Alan";
                        seceneklerVeri=dbBaglanti.getVeri("cikan_malzemeler","Teslim_Alan","");
                        seceneklerSpinnerOlustur(seceneklerVeri);


                        break;
                    case "Malzeme Teslim Eden":
                        dbColumn="Teslim_Eden";
                        seceneklerVeri=dbBaglanti.getVeri("cikan_malzemeler","Teslim_Eden","");
                        seceneklerSpinnerOlustur(seceneklerVeri);

                        break;
                    case "Malzeme Giriş/Çikiş Tarihi":
                        getTarih();



                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnSorgCSVDosyaExport:
                requestPermission();
                dosyaAdiDialog();

                break;
            case R.id.btnSorgGer:
                dosyaAdiDialog();

                break;
        }

    }

    private void seceneklerSpinnerOlustur(String[] seceneklerVeri){
        seceneklerArrAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,seceneklerVeri);
        seceneklerArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secenekler.setAdapter(seceneklerArrAdapter);
        secenekler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSecenek=parent.getItemAtPosition(position).toString();
                malzemeLis=new ArrayList<>();
                //System.out.println("#########################"+selectedSecenek);
                malzemeLis=dbBaglanti.getMalzemeler("cikan_malzemeler",dbColumn+"= '"+selectedSecenek+"'");
                recyclerViewOlustur(malzemeLis);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void recyclerViewOlustur(List<Malzeme> malzemeLis){
        mAdapter = new MalzemeListAdapter(getActivity(), malzemeLis);
        recyclerView.setHasFixedSize(true);
        Toast.makeText(getContext(), malzemeLis.size() + " Kalem Listlenecektir...", Toast.LENGTH_LONG).show();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();
    }

    private void getTarih(){

        int mYear, mMonth, mDay;
        final String[] tarih = new String[1];
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //System.out.println("#################"+dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        if(monthOfYear<10){
                            String t=year+ "/0" + (monthOfYear + 1) + "/" + dayOfMonth;
                            //gececiTarih.setText( year+ "/0" + (monthOfYear + 1) + "/" + dayOfMonth);
                            dbColumn="Cikis_Tarih";

                            malzemeLis=dbBaglanti.getMalzemeler("cikan_malzemeler",dbColumn+" between '" +t+" 00:00:00' and '"+t+" 24:00:00'");
                            recyclerViewOlustur(malzemeLis);

                        }else if(dayOfMonth<10){
                            String t=year+ "/" + (monthOfYear + 1) + "/0" + dayOfMonth;
                            //gececiTarih.setText( year+ "/0" + (monthOfYear + 1) + "/" + dayOfMonth);
                            dbColumn="Cikis_Tarih";

                            malzemeLis=dbBaglanti.getMalzemeler("cikan_malzemeler",dbColumn+" between '" +t+" 00:00:00' and '"+t+" 24:00:00'");
                            recyclerViewOlustur(malzemeLis);

                        }
                        else if(dayOfMonth<10&&monthOfYear<10){
                            String t=year+ "/0" + (monthOfYear + 1) + "/0" + dayOfMonth;
                            //gececiTarih.setText( year+ "/0" + (monthOfYear + 1) + "/" + dayOfMonth);
                            dbColumn="Cikis_Tarih";

                            malzemeLis=dbBaglanti.getMalzemeler("cikan_malzemeler",dbColumn+" between '" +t+" 00:00:00' and '"+t+" 24:00:00'");
                            recyclerViewOlustur(malzemeLis);

                        }
                        else{
                            String t=year+ "/0" + (monthOfYear + 1) + "/" + dayOfMonth;

                            gececiTarih.setText(year+ "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            dbColumn="Cikis_Tarih";
                            malzemeLis=dbBaglanti.getMalzemeler("cikan_malzemeler",dbColumn+" between '" +t+" 00:00:00' and '"+t+" 24:00:00'");
                            recyclerViewOlustur(malzemeLis);
                            }

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();


    }

    public void donusDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Geriye dönüş yapmak ister misiniz?");
        alertDialogBuilder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        alertDialogBuilder.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getContext(), BarKodkirgaz.class);
                        startActivity(intent);


                    }
                });

        alertDialogBuilder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public void dosyaAdiDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Dosya Adi");
        alert.setMessage(" Oluşturulacak dosyanın adını girimisiniz");

        // Set an EditText view to get user input
        final EditText dosya = new EditText(getContext());
        dosya.setHint("CSV Dosya Adı");
        alert.setView(dosya);

        alert.setPositiveButton("Devam et", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String dosyaAdi=dosya.getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Utils util=new Utils();
                dosyaAdi=dosyaAdi+date;
                util.Malzeme2CsvDonusturcu(getActivity(),malzemeLis, dosyaAdi);



            }
        });

        alert.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }



}
