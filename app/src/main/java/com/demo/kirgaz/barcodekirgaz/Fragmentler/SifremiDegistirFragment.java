package com.demo.kirgaz.barcodekirgaz.Fragmentler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.kirgaz.barcodekirgaz.Giris;
import com.demo.kirgaz.barcodekirgaz.R;
import com.demo.kirgaz.barcodekirgaz.Siniflar.DBBaglanti;

public class SifremiDegistirFragment extends Fragment {
    private View view;
    private String user;
    private EditText parola, parolaTekrari;
    private DBBaglanti dbBaglanti;
    private String pass,parolaTekrar;
    private Button btnparolaDegistir;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sifre_degistir_fragment, container, false);
        user= getArguments().getString("secilenUser");
        parola=view.findViewById(R.id.etProla);
        parolaTekrari=view.findViewById(R.id.etParolaTekrari);
        btnparolaDegistir=view.findViewById(R.id.btnParolaDegistir);

        dbBaglanti=new DBBaglanti(getContext());
        btnparolaDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=parola.getText().toString();
                parolaTekrar=parolaTekrari.getText().toString();
                if(pass.equals("")||parolaTekrar.equals("")){
                    Toast.makeText(getContext(),"Parola Daha yazmadiniz yeni paolanizi yazar misiniz",Toast.LENGTH_LONG).show();
                }

                if(pass.equals(parolaTekrar)){
                    dbBaglanti.updateVeri("giris_bilgi", "password", parola.getText().toString(),"userName='"+user+"'");
                    donusDialog();
                }else{
                    Toast.makeText(getContext(),"Yazdığınız iki parola eşit değil parolanızı kontrol ederek yeniden girermisiniz",Toast.LENGTH_LONG).show();
                }
            }
        });









        return view;
    }
    public void donusDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(" Parolanız başarıyla değiştirilmiştir, Giris ekranına dönmek ister misiniz?");
        alertDialogBuilder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        alertDialogBuilder.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(getContext(), Giris.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
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

}
