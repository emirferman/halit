package com.demo.kirgaz.barcodekirgaz.Adapterler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.kirgaz.barcodekirgaz.R;
import com.demo.kirgaz.barcodekirgaz.Siniflar.Malzeme;


import java.util.List;


public class MalzemeListAdapter extends RecyclerView.Adapter<MalzemeListAdapter.MalzemeViewHolder> {

    private List<Malzeme>malzemeList;

    private RelativeLayout relativeLayout;
    public Context mContext;

    //LayoutInflater layoutInflater;

    public MalzemeListAdapter(Context mContext, List<Malzeme> malzemeList) {
        this.malzemeList = malzemeList;
        this.mContext=mContext;
       // layoutInflater=LayoutInflater.from(mContext);
    }



    @Override
    public MalzemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.bir_malzeme_layout, parent, false);

        return new MalzemeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MalzemeViewHolder malzemeViewHolder, int i) {
        malzemeViewHolder.malzemeAdi.setText(malzemeList.get(i).getMalzemeAdi());
        malzemeViewHolder.girisCikisYeri.setText(malzemeList.get(i).getCikis_yer());
        malzemeViewHolder.teslimAlan.setText(malzemeList.get(i).getTeslim_Alan());
        malzemeViewHolder.barKod.setText(malzemeList.get(i).getBar_Kodu());
        //malzemeViewHolder.birimSaysi.setText(malzemeList.get(i).getBirimSayisi());
        malzemeViewHolder.birimSaysi.setText(Integer.toString(malzemeList.get(i).getBirimSayisi()));
        malzemeViewHolder.partiNo.setText(malzemeList.get(i).getParti_No());

    }

    @Override
    public int getItemCount() {
        return malzemeList.size();
    }


    public class MalzemeViewHolder extends RecyclerView.ViewHolder {

        private TextView malzemeAdi, girisCikisYeri,teslimAlan,barKod,birimSaysi,partiNo;
        public MalzemeViewHolder(View view) {
            super(view);
            malzemeAdi = view.findViewById(R.id.tvMalzemeAdi);
            girisCikisYeri=view.findViewById(R.id.tvGirisCikisYeri);
            teslimAlan = view.findViewById(R.id.tvTeslimAlan);
            barKod=view.findViewById(R.id.tvBarcdu);
            birimSaysi=view.findViewById(R.id.tvBirimSayisi);
            partiNo=view.findViewById(R.id.tvPartiNo);

        }
    }


}
