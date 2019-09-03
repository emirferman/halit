package com.demo.kirgaz.barcodekirgaz.Fragmentler;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.kirgaz.barcodekirgaz.BarKodkirgaz;
import com.demo.kirgaz.barcodekirgaz.R;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Okuycu extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> okunanBrkotlari;
    private BarcodeDetector myBarcodeDetector;
    private TextView okunanliste;
    private CameraSource myCameraSource;
    private SurfaceView mysurfaceView;
    private String okunan="";
    private Button okunanVeriEkle,geriyeDon, sonOkunanSil,btnFlashCalistir;
    private Camera camera = null;
    private boolean flashmode=false;
    private CameraSource cameraSource;


    private MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okuycu);
        mysurfaceView = findViewById(R.id.svBarkod);
        okunanBrkotlari=new ArrayList<>();
        okunanliste=findViewById(R.id.tvOkunanBarkotlari);
        okunanVeriEkle=findViewById(R.id.btnMalzemeEkle);
        geriyeDon=findViewById(R.id.btnGeriDon);
        sonOkunanSil=findViewById(R.id.btn_son_okunanSil);
        okunanVeriEkle.setOnClickListener(this);
        geriyeDon.setOnClickListener(this);
        sonOkunanSil.setOnClickListener(this);

        song= MediaPlayer.create(Okuycu.this, R.raw.beep);
        btnFlashCalistir=findViewById(R.id.btnFlashCalistir);
        btnFlashCalistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashCalistir();

            }
        });
        camerayaBaglan();
    }
    private void camerayaBaglan() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)

                .build();
      /* if(!barcodeDetector.isOperational()){
            Toast.makeText(Okuycu.this," detectora Baglanamamistir!",Toast.LENGTH_LONG).show();
            return;
        }*/
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setAutoFocusEnabled(true)

                .setRequestedPreviewSize(640, 480)

                .build();
        mysurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        Okuycu.this.checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                try {
                    cameraSource.start(mysurfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }}
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                /*final SparseArray<Barcode> barcodeSparseArray=detections.getDetectedItems();
                String okunan="";

                if(barcodeSparseArray.size()>0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            Okuycu.this.checkSelfPermission(Manifest.permission.VIBRATE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.VIBRATE}, 0);
                    } else {
                        Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {

                            v.vibrate(250);
                        }
                    }
                   Intent intent=new Intent();
                    intent.putExtra("barkot",barcodeSparseArray.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();*/


                Barcode barcode;
                final SparseArray<Barcode> barcodeSparseArray=detections.getDetectedItems();
                if(barcodeSparseArray.size()>0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            Okuycu.this.checkSelfPermission(Manifest.permission.VIBRATE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.VIBRATE}, 0);
                    } else {
                        Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {

                            v.vibrate(250);
                        }
                    }
                    for(int i=0;i<barcodeSparseArray.size();i++){
                        //Toast.makeText(Okuycu.this,"Bu Brkot daha önce okunmuş...!",Toast.LENGTH_LONG).show();

                        barcode=barcodeSparseArray.valueAt(i);
                        if(!okunanBrkotlari.contains(barcode.displayValue)){
                            song.start();


                            okunanBrkotlari.add(barcode.displayValue);
                            okunan=okunanliste.getText()+barcode.displayValue+"\n";
                            okunanliste.setText(okunan);


                        }else{
                            Okuycu.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(Okuycu.this, "Bu Brkot daha önce okunmuş...!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnMalzemeEkle:{

            malzemeEklemeDialog();
            break;

        }
            case R.id.btnGeriDon:{
            donusDialog();
            break;

        }case R.id.btn_son_okunanSil:{
                sonOkunan_Sil();
            break;
            }

        }
    }

   public void donusDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Geriye döneceğinden emin misiniz?");
        alertDialogBuilder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        alertDialogBuilder.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(Okuycu.this, BarKodkirgaz.class);
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

    public void sonOkunan_Sil() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(okunanBrkotlari.get(okunanBrkotlari.size()-1)+" Sileceğinden emin misiniz?");
        alertDialogBuilder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        alertDialogBuilder.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String yeniListe = "";
                        okunanBrkotlari.remove(okunanBrkotlari.size()-1);
                        for(int i=0;i<okunanBrkotlari.size();i++){
                            yeniListe+=okunanBrkotlari.get(i)+"\n";
                            
                        }
                        okunanliste.setText(yeniListe);
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

   public void malzemeEklemeDialog() {
    AlertDialog.Builder alert = new AlertDialog.Builder(Okuycu.this);

            alert.setTitle("Ekleme Detayı");
            alert.setMessage(okunanBrkotlari.size()+" Kalem Okunmuştur, Olçü/Birim varsa onu girer misiniz?");

    // Set an EditText view to get user input
    final EditText birimAdedi = new EditText(Okuycu.this);
    final EditText etPartiNo = new EditText(Okuycu.this);

       birimAdedi.setHint("Ölçü/Birim adedi");
       etPartiNo.setHint("Varsa Lot No Giriniz...");
       LinearLayout layout = new LinearLayout(Okuycu.this);
       layout.setOrientation(LinearLayout.VERTICAL);
       layout.addView(birimAdedi);
       layout.addView(etPartiNo);
       alert.setView(layout);

            alert.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent=new Intent();

                    String malzemeAdedi = birimAdedi.getText().toString();
                    String partiNo=etPartiNo.getText().toString();
                    int birimSayisi=1;


                    if (!malzemeAdedi.equals("")) {
                        birimSayisi=Integer.parseInt(malzemeAdedi);

                    }else{
                        birimSayisi=1;
                    }

                    intent.putStringArrayListExtra("okunanBarkotlari",okunanBrkotlari);

                    intent.putExtra("birimSayisi",birimSayisi);
                    intent.putExtra("partiNo",partiNo);
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();


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

    private void flashCalistir() {
        camera=getCamera(cameraSource);
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                if(!flashmode){
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
                else {
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }

                camera.setParameters(param);
                flashmode = !flashmode;
                if(flashmode){
                    Toast.makeText(this,"Flash Çalıştırılmıştır",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this,"Flash Kaptılmıştır",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }
}
