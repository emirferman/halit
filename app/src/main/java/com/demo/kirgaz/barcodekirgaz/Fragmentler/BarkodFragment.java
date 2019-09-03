package com.demo.kirgaz.barcodekirgaz.Fragmentler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.demo.kirgaz.barcodekirgaz.Adapterler.MalzemeListAdapter;
import com.demo.kirgaz.barcodekirgaz.R;
import com.demo.kirgaz.barcodekirgaz.Siniflar.DBBaglanti;
import com.demo.kirgaz.barcodekirgaz.Siniflar.Malzeme;
import com.demo.kirgaz.barcodekirgaz.Siniflar.Utils;
import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BarkodFragment extends Fragment implements View.OnClickListener {
    TextView barkotSonuc;
    private final String islemTuruVeri[]={"GİRİŞ","ÇIKIŞ"};
    private Button BarkotOku, btnListele, btnVeriTabaniyeEkle;
    private View view;
    private String [] Malzeme_Group_Veri, Malzeme_Adi_Veri,Çıkış_Giriş_Yeri,Teslim_Alan,Teslim_Eden ;
    private ArrayAdapter<String> malzemeGroupArrayAdapter,malzemeArrayAdapter, cikisYerArrayAdapter, teslimAlanArrayAdapter,teslimEdenArrayAdapter;
    private Spinner malzemeGroupSpinner,malzemeSpinner, CikisYerSpinner, teslimSpinner, teslimEdenSpinner;
    private ArrayList<String> okunanBrkotlari;
    private int birimSayaisi;
    private List<Malzeme> liste;
    private RecyclerView recyclerView;
    private MalzemeListAdapter mAdapter;
    private Malzeme malzeme;
    private String secilenIslemTuru, secilenMalzemeGroupu,secilenMalzemeAdi, secilenTeslimAlan, secilenGirisCikisYeri,partiNo, tarih, teslimEden,secilenTeslimEden;
    private DBBaglanti dbBaglanti;
    private RadioGroup myRadioGroup;
    private RadioButton giris,cikis;
    private Date d;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.malzeme_ekleme, container, false);
        dbBaglanti=new DBBaglanti(getContext());
        barkotSonuc = view.findViewById(R.id.Stok_Kodu);
        BarkotOku = view.findViewById(R.id.btnScan);
        btnListele = view.findViewById(R.id.btnListele);
        btnVeriTabaniyeEkle = view.findViewById(R.id.btnVeritabaniEkle);
        BarkotOku.setOnClickListener(this);
        btnListele.setOnClickListener(BarkodFragment.this);
        btnVeriTabaniyeEkle.setOnClickListener(this);
        malzemeGroupSpinner=view.findViewById(R.id.malzemeGroupSP);
        malzemeSpinner = view.findViewById(R.id.malzeme_adiSP);
        CikisYerSpinner = view.findViewById(R.id.cikis_yeriSP);
        teslimSpinner = view.findViewById(R.id.teslimAlanSp);
        teslimEdenSpinner=view.findViewById(R.id.spTeslimEden);
        okunanBrkotlari = new ArrayList<>();
        liste = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvMalzamaListe);
        secilenIslemTuru=islemTuruVeri[0];
        myRadioGroup=view.findViewById(R.id.rdgislemTuru);
        giris=view.findViewById(R.id.rdGiris);
        cikis=view.findViewById(R.id.rdCikis);
        d=new Date();

        //teslimEden="halit Bakir";

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdGiris:
                        secilenIslemTuru=islemTuruVeri[0];
                        Teslim_Alan=dbBaglanti.getVeri("Teslim_Alan_Eden","Adi_SoyAdi","Is_Yeri='"+secilenGirisCikisYeri+"'");
                        Teslim_Eden=dbBaglanti.getVeri("Teslim_Alan_Eden","Adi_SoyAdi","");
                        getCikisYerspinner();
                        break;
                    case R.id.rdCikis:
                        secilenIslemTuru=islemTuruVeri[1];
                        Teslim_Alan=dbBaglanti.getVeri("Teslim_Alan_Eden","Adi_SoyAdi","");
                        Teslim_Eden=dbBaglanti.getVeri("Teslim_Alan_Eden","Adi_SoyAdi","Is_Yeri='"+secilenGirisCikisYeri+"'");
                        getCikisYerspinner();

                }

            }
        });

        ///#######################
        //Spinners data tanimlamasi
        Malzeme_Group_Veri=dbBaglanti.getMAlzemeGrouplari();
        secilenMalzemeGroupu=Malzeme_Group_Veri[0];
        Malzeme_Adi_Veri=dbBaglanti.getGrouptakiMalzamaAdilari(secilenMalzemeGroupu);
        getMalzemeAdiSpinner();



        //#####Malzeme spinner
        malzemeGroupArrayAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Malzeme_Group_Veri);
        malzemeGroupArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        malzemeGroupSpinner.setAdapter(malzemeGroupArrayAdapter);
        malzemeGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilenMalzemeGroupu = parent.getItemAtPosition(position).toString();
                Malzeme_Adi_Veri=dbBaglanti.getGrouptakiMalzamaAdilari(secilenMalzemeGroupu);

                getMalzemeAdiSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Teslim_Alan=dbBaglanti.getVeri("Teslim_Alan_Eden","Adi_SoyAdi","Is_Yeri='KAMAN'");
        Teslim_Eden=dbBaglanti.getVeri("Teslim_Alan_Eden","Adi_SoyAdi","");
        getTeslimAlanspinner();
        getTEdenAlanspinner();
        Çıkış_Giriş_Yeri = dbBaglanti.getGirisCikisYerleri();
        cikisYerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Çıkış_Giriş_Yeri);
        cikisYerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CikisYerSpinner.setAdapter(cikisYerArrayAdapter);
        CikisYerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilenGirisCikisYeri = parent.getItemAtPosition(position).toString();
                if (secilenIslemTuru.equals(islemTuruVeri[0])) {

                    Teslim_Alan = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "Is_Yeri='" + secilenGirisCikisYeri + "'");
                    Teslim_Eden = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "");
                    getTeslimAlanspinner();
                    getTEdenAlanspinner();
                } else if (secilenIslemTuru.equals(islemTuruVeri[1])) {
                    Teslim_Alan = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "");
                    Teslim_Eden = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "Is_Yeri='" + secilenGirisCikisYeri + "'");
                    getTeslimAlanspinner();
                    getTEdenAlanspinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    //#####CikisYer spinner
    public void getCikisYerspinner() {
        Çıkış_Giriş_Yeri = dbBaglanti.getGirisCikisYerleri();
        cikisYerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Çıkış_Giriş_Yeri);
        cikisYerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CikisYerSpinner.setAdapter(cikisYerArrayAdapter);
        CikisYerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilenGirisCikisYeri = parent.getItemAtPosition(position).toString();
                if (secilenIslemTuru.equals(islemTuruVeri[0])) {

                    Teslim_Alan = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "Is_Yeri='" + secilenGirisCikisYeri + "'");
                    Teslim_Eden = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "");
                    getTeslimAlanspinner();
                    getTEdenAlanspinner();
                } else if (secilenIslemTuru.equals(islemTuruVeri[1])) {
                    Teslim_Alan = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "");
                    Teslim_Eden = dbBaglanti.getVeri("Teslim_Alan_Eden", "Adi_SoyAdi", "Is_Yeri='" + secilenGirisCikisYeri + "'");
                    getTeslimAlanspinner();
                    getTEdenAlanspinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //#####Teslim Alan spinner
public void getTeslimAlanspinner() {
    teslimAlanArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Teslim_Alan);
    teslimAlanArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    teslimSpinner.setAdapter(teslimAlanArrayAdapter);
    teslimSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            secilenTeslimAlan = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });
}

//#################
    //#####Teslim Eden spinner

    public void getTEdenAlanspinner() {
        teslimEdenArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Teslim_Eden);
        teslimEdenArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teslimEdenSpinner.setAdapter(teslimEdenArrayAdapter);
        teslimEdenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilenTeslimEden = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    //#####Malzeme spinner
public void getMalzemeAdiSpinner(){


    malzemeArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Malzeme_Adi_Veri);
    malzemeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    malzemeSpinner.setAdapter(malzemeArrayAdapter);
    malzemeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            secilenMalzemeAdi = parent.getItemAtPosition(position).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String barkotListe = "";
        liste = new ArrayList<>();
        ArrayList<Malzeme> l=new ArrayList<>();


        //Listeyi Bosaltma
        mAdapter = new MalzemeListAdapter(getActivity(), l);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();

        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    okunanBrkotlari = data.getStringArrayListExtra("okunanBarkotlari");
                    birimSayaisi = data.getIntExtra("birimSayisi", 1);
                    partiNo=data.getStringExtra("partiNo");
                    Utils ut=new Utils();
                    tarih=ut.getDateTime();
                    String sart=" Malzeme_Adi= '"+secilenMalzemeAdi+"'";
                    String katogeri []=dbBaglanti.getVeri("Malzeme_Kategori","Kategori",sart);
                    sart=" Malzeme_Adi= '"+secilenMalzemeAdi+"'";
                    String Olcum []=dbBaglanti.getVeri("Malzeme_Kategori","Olcu_Birim",sart);
                    sart=" Malzeme_Adi= '"+secilenMalzemeAdi+"'";
                    String stokKodu []=dbBaglanti.getVeri("Malzeme_Kategori","Stok_Kodu",sart);

                    for (int i = 0; i < okunanBrkotlari.size(); i++) {
                        barkotListe += okunanBrkotlari.get(i) + "\n";
                        malzeme = new Malzeme();
                        malzeme.setIslemTuru(secilenIslemTuru);
                        malzeme.setMalzemeGroupu(secilenMalzemeGroupu);
                        malzeme.setMalzemeAdi(secilenMalzemeAdi);
                        malzeme.setTeslim_Alan(secilenTeslimAlan);
                        malzeme.setBar_Kodu(okunanBrkotlari.get(i));
                        malzeme.setCikis_yer(secilenGirisCikisYeri);
                        malzeme.setBirimSayisi(birimSayaisi);
                        malzeme.setParti_No(partiNo);
                        malzeme.setCikis_Tarih(tarih);
                        malzeme.setKategori(katogeri[0]);
                        malzeme.setOlcu_Birim(Olcum[0]);
                        malzeme.setTeslim_Eden(secilenTeslimEden);
                        malzeme.setStokKodu(stokKodu[0]);
                        liste.add(malzeme);

                    }
                    barkotSonuc.setText(barkotListe);

                } else {
                    barkotSonuc.setText("Her hangi bir barkot Okunmamıştır...lütfen tekrar deneyin");
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnScan: {
                Intent intent = new Intent(getActivity(), Okuycu.class);
                startActivityForResult(intent, 0);
                break;

            }
            case R.id.btnListele: {
                mAdapter = new MalzemeListAdapter(getActivity(), liste);
                recyclerView.setHasFixedSize(true);
                Toast.makeText(getContext(), liste.size() + " Kalem Listlenecektir...", Toast.LENGTH_LONG).show();

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(mAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.btnVeritabaniEkle: {
                if(liste.size()>0){
                dbBaglanti.veriTabaniyaEkle(liste);
                Toast.makeText(getContext(), liste.size() + " Kalem Veri Tabanaya Eklenmiştir...", Toast.LENGTH_LONG).show();
                liste.clear();
                mAdapter = new MalzemeListAdapter(getActivity(), liste);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(mAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter.notifyDataSetChanged();
                barkotSonuc.setText("");
                }else{
                    Toast.makeText(getContext(), " Her hangi malzeme okunmamistir...", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }


    }
}
