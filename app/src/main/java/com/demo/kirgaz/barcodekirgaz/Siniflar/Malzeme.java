package com.demo.kirgaz.barcodekirgaz.Siniflar;

public class Malzeme {
    int Id, birimSayisi;
    private String islemTuru, MalzemeAdi, cikis_yer, Teslim_Alan, Bar_Kodu, Kategori, stokKodu,Teslim_Eden,Cikis_Tarih,Parti_No,Olcu_Birim, malzemeGroupu;


    public Malzeme() {
    }



    public Malzeme(int id, String islemTuru, String stok_Kodu, String cikis_yer, String teslim_Alan, String bar_Kodu,int birimSayisi,String Kategori,String Teslim_Eden,String Cikis_Tarih,String Parti_No,String Olcu_Birim,String malzemeGroupu) {
        this.Id = id;
        this.islemTuru=islemTuru;
        this.MalzemeAdi = stok_Kodu;
        this.cikis_yer = cikis_yer;
        this.Teslim_Alan = teslim_Alan;
        this.Bar_Kodu = bar_Kodu;
        this.birimSayisi=birimSayisi;
        this.Kategori=Kategori;
        this.Teslim_Eden=Teslim_Eden;
        this.Cikis_Tarih=Cikis_Tarih;
        this.Parti_No=Parti_No;
        this.Olcu_Birim=Olcu_Birim;
        this.malzemeGroupu=malzemeGroupu;
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }

    public int getBirimSayisi() {
        return birimSayisi;
    }

    public void setBirimSayisi(int birimSayisi) {
        this.birimSayisi = birimSayisi;
    }



    public String getMalzemeAdi() {
        return MalzemeAdi;
    }

    public void setMalzemeAdi(String stok_Kodu) {
        MalzemeAdi = stok_Kodu;
    }

    public String getCikis_yer() {
        return cikis_yer;
    }

    public void setCikis_yer(String cikis_yer) {
        this.cikis_yer = cikis_yer;
    }

    public String getTeslim_Alan() {
        return Teslim_Alan;
    }

    public void setTeslim_Alan(String teslim_Alan) {
        Teslim_Alan = teslim_Alan;
    }

    public String getBar_Kodu() {
        return Bar_Kodu;
    }

    public void setBar_Kodu(String bar_Kodu) {
        Bar_Kodu = bar_Kodu;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getTeslim_Eden() {
        return Teslim_Eden;
    }

    public void setTeslim_Eden(String teslim_Eden) {
        Teslim_Eden = teslim_Eden;
    }

    public String getCikis_Tarih() {
        return Cikis_Tarih;
    }

    public void setCikis_Tarih(String cikis_Tarih) {
        Cikis_Tarih = cikis_Tarih;
    }

    public String getParti_No() {
        return Parti_No;
    }

    public void setParti_No(String parti_No) {
        Parti_No = parti_No;
    }

    public String getOlcu_Birim() {
        return Olcu_Birim;
    }

    public void setOlcu_Birim(String olcu_Birim) {
        Olcu_Birim = olcu_Birim;
    }

    public String getMalzemeGroupu() {
        return malzemeGroupu;
    }

    public void setMalzemeGroupu(String malzemeGroupu) {
        this.malzemeGroupu = malzemeGroupu;
    }

    public String getIslemTuru() {
        return islemTuru;
    }

    public void setIslemTuru(String islemTuru) {
        this.islemTuru = islemTuru;
    }

    @Override
    public String toString() {
        return "Malzeme{" +
                "Id=" + Id +
                ", birimSayisi=" + birimSayisi +
                ", islemTuru='" + islemTuru + '\'' +
                ", MalzemeAdi='" + MalzemeAdi + '\'' +
                ", cikis_yer='" + cikis_yer + '\'' +
                ", Teslim_Alan='" + Teslim_Alan + '\'' +
                ", Bar_Kodu='" + Bar_Kodu + '\'' +
                ", Kategori='" + Kategori + '\'' +
                ", stokKodu='" + stokKodu + '\'' +
                ", Teslim_Eden='" + Teslim_Eden + '\'' +
                ", Cikis_Tarih='" + Cikis_Tarih + '\'' +
                ", Parti_No='" + Parti_No + '\'' +
                ", Olcu_Birim='" + Olcu_Birim + '\'' +
                ", malzemeGroupu='" + malzemeGroupu + '\'' +
                '}';
    }
}
