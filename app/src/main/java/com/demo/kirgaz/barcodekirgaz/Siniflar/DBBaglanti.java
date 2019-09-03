package com.demo.kirgaz.barcodekirgaz.Siniflar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBBaglanti {
    private Context context;
    private DatabaseHelper myDataBaseHelper;

    public DBBaglanti(Context context) {
        this.context = context;
        myDataBaseHelper=new DatabaseHelper(context);
    }

    public void yeniMalzemeEkle(Malzeme malzeme) {
        String tablo="cikan_malzemeler";
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("MalzemeAdi", malzeme.getMalzemeAdi());
        contentValues.put("cikis_yer", malzeme.getCikis_yer());
        contentValues.put("Teslim_Alan", malzeme.getTeslim_Alan());
        contentValues.put("Bar_Kodu", malzeme.getBar_Kodu());
        contentValues.put("Birim_Sayisi",malzeme.getBirimSayisi());
        contentValues.put("Kategori",malzeme.getKategori());
        contentValues.put("Stok_Kodu",malzeme.getStokKodu());
        db.insert(tablo, null, contentValues);
        db.close();

    }
    public List<Malzeme> getMalzemeler(){
        List<Malzeme> malzemeList=new ArrayList<Malzeme>();
        Malzeme malzeme;
        SQLiteDatabase db = myDataBaseHelper.getReadableDatabase();

        Cursor cursor =db.rawQuery("select * from cikan_malzemeler", null);
        while (cursor.moveToNext())
        {
            malzeme=new Malzeme();
            malzeme.setId(cursor.getInt(0));
            malzeme.setMalzemeAdi(cursor.getString(1));
            malzeme.setIslemTuru(cursor.getString(2));
            malzeme.setCikis_yer(cursor.getString(3));
            malzeme.setTeslim_Alan(cursor.getString(4));
            malzeme.setTeslim_Eden(cursor.getString(5));
            malzeme.setMalzemeGroupu(cursor.getString(6));
            malzeme.setKategori(cursor.getString(7));
            malzeme.setBar_Kodu(cursor.getString(8));
            malzeme.setOlcu_Birim(cursor.getString(9));
            malzeme.setBirimSayisi(cursor.getInt(10));
            malzeme.setParti_No(cursor.getString(11));
            malzeme.setStokKodu(cursor.getString(12));
            malzeme.setCikis_Tarih(cursor.getString(13));



          /*  malzeme.setId(cursor.getInt(0));
            malzeme.setMalzemeAdi(cursor.getString(1));
            malzeme.setCikis_yer(cursor.getString(2));
            malzeme.setTeslim_Alan(cursor.getString(3));
            malzeme.setBar_Kodu(cursor.getString(4));
            malzeme.setKategori(cursor.getString(5));
            malzeme.setOlcu_Birim(cursor.getString(6));
            malzeme.setMalzemeGroupu(cursor.getString(7));
            malzeme.setParti_No(cursor.getString(8));
            malzeme.setTeslim_Eden(cursor.getString(9));
            malzeme.setCikis_Tarih(cursor.getString(10));
            malzeme.setBirimSayisi(cursor.getInt(11));
            malzeme.setStokKodu(cursor.getString(12));
            malzeme.setIslemTuru(cursor.getString(13));*/
            malzemeList.add(malzeme);
        }
        return malzemeList;
    }



    public String[] getMAlzemeGrouplari(){
        String[] l;

        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Cursor cursor =db.rawQuery("select DISTINCT MalzemeGroupu from Malzeme_Kategori", null);
        l=new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext())
        {
            l[i]=cursor.getString(0);
            i++;
        }


        return l;
    }

    public String [] getGrouptakiMalzamaAdilari(String malzemeGroupu){
        String[] l;

        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        String []data={"Malzeme_Adi"};
        //Cursor cursor=db.query("Malzeme_Kategori",data,"where MalzemeGroupu=?",new String[]{malzemeGroupu},null,null,null);
        Cursor cursor =db.rawQuery("select DISTINCT Malzeme_Adi from Malzeme_Kategori where MalzemeGroupu= '"+malzemeGroupu+"'", null);
        //Cursor cursor =db.rawQuery("select DISTINCT Malzeme_Adi from Malzeme_Kategori where MalzemeGroupu='" + malzemeGroupu+ "'", null);
        l=new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext())
        {
            l[i]=cursor.getString(0);
            i++;
        }
        return l;
    }

    public String [] getGirisCikisYerleri(){
        String[] l;

        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();

        Cursor cursor =db.rawQuery("select DISTINCT Is_Yeri from Teslim_Alan_Eden", null);
        l=new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext())
        {
            l[i]=cursor.getString(0);
            i++;
        }
        return l;
    }

    public String[] getTeslimAlan() {
        String[] l;

        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();

        Cursor cursor =db.rawQuery("select DISTINCT Adi_SoyAdi from Teslim_Alan_Eden", null);
        l=new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext())
        {
            l[i]=cursor.getString(0);
            i++;
        }
        return l;
    }

    public String [] getVeri(String tablo,String getirilecekVeri ,String sart){
        String[] l;

        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Cursor cursor;
        if(!sart.equals("")){

        cursor =db.rawQuery("select DISTINCT "+getirilecekVeri +" from "+tablo+" where "+sart, null);}
        else{
            cursor =db.rawQuery("select DISTINCT "+getirilecekVeri +" from "+tablo, null);}


        l=new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext())
        {
            l[i]=cursor.getString(0);
            i++;
        }
        return l;
    }
    public List<Malzeme> getMalzemeler(String tablo, String sart){
        List<Malzeme> liste =new ArrayList<>();
        Malzeme malzeme=new Malzeme();

        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Cursor cursor=null;
        try{
        if(!sart.equals("")){

            cursor =db.rawQuery("select * from "+tablo+" where "+sart, null);}
        else{
            cursor =db.rawQuery("select * from "+tablo, null);}

        while (cursor.moveToNext())
        {
            malzeme=new Malzeme();
            malzeme.setId(cursor.getInt(0));
            malzeme.setMalzemeAdi(cursor.getString(1));
            malzeme.setIslemTuru(cursor.getString(2));
            malzeme.setCikis_yer(cursor.getString(3));
            malzeme.setTeslim_Alan(cursor.getString(4));
            malzeme.setTeslim_Eden(cursor.getString(5));
            malzeme.setMalzemeGroupu(cursor.getString(6));
            malzeme.setKategori(cursor.getString(7));
            malzeme.setBar_Kodu(cursor.getString(8));
            malzeme.setOlcu_Birim(cursor.getString(9));
            malzeme.setBirimSayisi(cursor.getInt(10));
            malzeme.setParti_No(cursor.getString(11));
            malzeme.setStokKodu(cursor.getString(12));
            malzeme.setCikis_Tarih(cursor.getString(13));



            liste.add(malzeme);
        }
        return liste;
        }
        catch (Exception e){
            Toast.makeText(context,"Bu kriter göre malzeme bulunmamıştır...",Toast.LENGTH_LONG).show();
        }
        finally {
            //cursor.close();
            db.close();
        }
        return liste;
    }

    public void veriTabaniyaEkle(List<Malzeme> liste) {
        Malzeme malzeme;
        for(int i=0;i<liste.size();i++){
            String tablo="cikan_malzemeler";
            SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
            malzeme=new Malzeme();
            malzeme=liste.get(i);


            ContentValues contentValues = new ContentValues();
            contentValues.put("islem_Turu",malzeme.getIslemTuru());
            contentValues.put("MalzemeGroupu",malzeme.getMalzemeGroupu());
            contentValues.put("MalzemeAdi", malzeme.getMalzemeAdi());
            contentValues.put("cikis_yer", malzeme.getCikis_yer());
            contentValues.put("Teslim_Alan", malzeme.getTeslim_Alan());
            contentValues.put("Bar_Kodu", malzeme.getBar_Kodu());
            contentValues.put("Birim_Sayisi",malzeme.getBirimSayisi());
            contentValues.put("Kategori",malzeme.getKategori());
            contentValues.put("Stok_Kodu",malzeme.getStokKodu());
            contentValues.put("Parti_No",malzeme.getParti_No());
            contentValues.put("Cikis_Tarih",malzeme.getCikis_Tarih());
            contentValues.put("Olcu_Birimi",malzeme.getOlcu_Birim());
            contentValues.put("Teslim_Eden",malzeme.getTeslim_Eden());

            db.insert(tablo, null, contentValues);
            db.close();
        }
    }

    public void getTumMalzemeleri() {
    }

    public void updateVeri(String tablo, String alan, String yeniDeger, String sart) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(alan,yeniDeger);
        System.out.println("^^^^^%%%%%%%%%%%%%%%%%$$$$$$$$$$$$#############"+yeniDeger);
        try{
            db.update(tablo, cv, sart, null);

        }catch (Exception e){
            Toast.makeText(context,"Veritabanina baglanti olusturulmadigindan dolay parolayi degistirilmamistir",Toast.LENGTH_LONG).show();
        }

    }
}
