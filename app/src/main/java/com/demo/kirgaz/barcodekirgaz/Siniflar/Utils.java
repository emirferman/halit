package com.demo.kirgaz.barcodekirgaz.Siniflar;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Utils {


    public Utils() {
    }

    public String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public void Malzeme2CsvDonusturcu(FragmentActivity activity, List<Malzeme> malzemeler, String dosyaAdi){


            CSVWriter writer = null;
            FileWriter fileWriter = null;

            try {
                File file = new File("/sdcard/"+dosyaAdi+".csv");
                String a[]={"Id", "malzemeGroupu", "MalzemeAdi", "Bar_Kodu", "stokKodu", "Kategori",  "islemTuru", "cikis_yer", "Teslim_Alan",  "Teslim_Eden", "birimSayisi", "Olcu_Birim", "Parti_No", "Cikis_Tarih" };
                file.createNewFile();
                        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                csvWrite.writeNext(a);
                for(int i =0;i<malzemeler.size();i++) {
                    String[] entries = Malzeme2Array(malzemeler.get(i));
                    csvWrite.writeNext(entries);
                    System.out.println(entries.length);}
                csvWrite.close();
                Toast.makeText(activity,"Raporu Başarıyla Oluşturulmuştur",Toast.LENGTH_LONG).show();
                emailGonderDialog(activity, file.toString(),dosyaAdi);


                           } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity,"Raporu Oluşturamamıştır... Lütfen tekrar deneyin",Toast.LENGTH_LONG).show();
            }






                        /*try {
                            File csvfile = new File(Environment.getExternalStorageDirectory() + "/santyeTest.csv");
                            CSVWriter writer = new CSVWriter(new FileWriter(csvfile));
                            String[] nextLine=new String[13];
                            for(int i =0;i<malzemeler.size();i++) {
                                nextLine[0]=Integer.toString(malzemeler.get(i).getId());
                                nextLine[1]=malzemeler.get(i).getMalzemeAdi();
                                nextLine[2]=malzemeler.get(i).getBar_Kodu();

                                writer.writeNext(nextLine);
                                writer.close();
                                System.out.println(malzemeler.get(i).toString());
                            }*/


    }

    public void emailGonderDialog(final Context context, final String file, final String dosyaAdi) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Rapor Gonderilmesi");
        alert.setMessage(" Raporu Oluşturulmuştur \n Gondermek isterseniz Mail adresiniz giriniz...");

        final EditText mail = new EditText(context);
        mail.setHint("mail adres");
        alert.setView(mail);

        alert.setPositiveButton("Gonder", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String mailAdres=mail.getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                if(isOnline(context)){
                SendMail sendMail=new SendMail(context,mailAdres,date+" Raporu","Ekte "+date+" ait olan giren/cikan malzemeler icermektedir");
                sendMail.execute(file, dosyaAdi);}
                else{
                    Toast.makeText(context,"Internet Bağlantınız Olmadığı için işlem gerçekleştirememiştir",Toast.LENGTH_LONG).show();
                }



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

    public String[] Malzeme2Array(Malzeme m){
        String[] arr=new String[14];
        arr[0]=Integer.toString(m.getId());
        arr[1]=m.getMalzemeGroupu();
        arr[2]=m.getMalzemeAdi();
        arr[3]=m.getBar_Kodu();
        arr[4]=m.getStokKodu();
        arr[5]=m.getKategori();
        arr[6]=m.getIslemTuru();
        arr[7]=m.getCikis_yer();
        arr[8]=m.getTeslim_Alan();
        arr[9]=m.getTeslim_Eden();
        arr[10]=Integer.toString(m.getBirimSayisi());
        arr[11]=m.getOlcu_Birim();
        arr[12]=m.getParti_No();

        arr[13]=m.getCikis_Tarih();

          return  arr;

    }

    public class SendMail extends AsyncTask<String,Void,String> {


        private Context context;
        private Session session;
        private String email;
        private String subject;
        private String message;
        private ProgressDialog progressDialog;
        public SendMail(Context context, String email, String subject, String message){
            this.context = context;
            this.email = email;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context,"Rapor Gönderilyor","Lütfen Bekleyiniz...",false,false);
        }

        @Override
        protected String doInBackground(String... params) {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.yandex.com.tr");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        //Authenticating the password
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(Parametreler.EMAIL, Parametreler.PASSWORD);
                        }
                    });

            try {

                MimeMessage mm = new MimeMessage(session);
                mm.setFrom(new InternetAddress(Parametreler.EMAIL));
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                mm.addRecipient(Message.RecipientType.CC, new InternetAddress("halitbakir@kirgaz.com.tr"));
                mm.setSubject(subject);
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(message);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart = new MimeBodyPart();
                String filename = params[0];
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(params[1]+".csv");
                multipart.addBodyPart(messageBodyPart);
                mm.setContent(multipart);
                Transport.send(mm);
                File file = new File(filename);
                boolean deleted = file.delete();
                return "Tamam";

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Tamam")){
            Toast.makeText(context,"Rapor Başarıyla Gönderilmiştir",Toast.LENGTH_LONG).show();}
            else{
                Toast.makeText(context,"Bir Hata Oluşturulmuştur... Lütfen tekrar deneyin",Toast.LENGTH_LONG).show();}
            }
        }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    }


