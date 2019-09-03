package com.demo.kirgaz.barcodekirgaz.Fragmentler;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.kirgaz.barcodekirgaz.Adapterler.MalzemeListAdapter;
import com.demo.kirgaz.barcodekirgaz.BarKodkirgaz;
import com.demo.kirgaz.barcodekirgaz.R;
import com.demo.kirgaz.barcodekirgaz.Siniflar.DBBaglanti;
import com.demo.kirgaz.barcodekirgaz.Siniflar.Malzeme;
import com.demo.kirgaz.barcodekirgaz.Siniflar.Utils;
import com.google.android.gms.common.api.CommonStatusCodes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListeOlusturFragment extends Fragment implements View.OnClickListener {
    private View view;
    private List<Malzeme> malzemeler;
    private MalzemeListAdapter mAdapter;
    private RecyclerView recyclerView;
    private Button GeriDonus, CSVDonustur;
    final private int PERMISSION_REQUEST_CODE = 123;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.liste_olustur_fragment, container, false);
        recyclerView = view.findViewById(R.id.rvlisteOlustur);
        malzemeler = new ArrayList<>();
        DBBaglanti dbBaglanti = new DBBaglanti(getActivity());
        malzemeler = dbBaglanti.getMalzemeler();
        for(int i=0;i<malzemeler.size();i++){
            System.out.println(malzemeler.get(i).toString());
        }
        mAdapter = new MalzemeListAdapter(getActivity(), malzemeler);
        recyclerView.setHasFixedSize(true);
        GeriDonus = view.findViewById(R.id.btnGer);
        CSVDonustur = view.findViewById(R.id.btnCSVDosyaExport);
        GeriDonus.setOnClickListener(this);
        CSVDonustur.setOnClickListener(this);
        Toast.makeText(getContext(), malzemeler.size() + " Kalem Listlenecektir...", Toast.LENGTH_LONG).show();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnGer:
                donusDialog();
                break;
            case R.id.btnCSVDosyaExport: {

                if (checkPermission())
                {
                    dosyaAdiDialog();


                }else {
                    requestPermission();
                    dosyaAdiDialog();
                }


                break;
            }


        }
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
                util.Malzeme2CsvDonusturcu(getActivity(),malzemeler, dosyaAdi);



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
