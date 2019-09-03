package com.demo.kirgaz.barcodekirgaz;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.demo.kirgaz.barcodekirgaz.Fragmentler.BarkodFragment;
import com.demo.kirgaz.barcodekirgaz.Fragmentler.ListeOlusturFragment;
import com.demo.kirgaz.barcodekirgaz.Fragmentler.SifremiDegistirFragment;
import com.demo.kirgaz.barcodekirgaz.Fragmentler.SorgulamaFRagment;

public class BarKodkirgaz extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private  Intent intent;
    private Bundle bundle;
    private String secilenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_kodkirgaz);
        intent=getIntent();
        secilenUser=intent.getStringExtra("user");
        bundle=new Bundle();
        bundle.putString("secilenUser",secilenUser);

        int navID = R.id.nav_barkod_oku;

        if (navID == R.id.nav_barkod_oku) {
            BarkodFragment barkod = new BarkodFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, barkod).addToBackStack(null).commit();
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_barkod_oku) {
            BarkodFragment barkod = new BarkodFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, barkod).addToBackStack(null).commit();
            // Handle the camera action
        }else if (id == R.id.nav_liste_olustur) {
            ListeOlusturFragment liste = new ListeOlusturFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, liste).addToBackStack(null).commit();

        } else if (id == R.id.nav_bilgisayra_gonder) {
            SorgulamaFRagment liste = new SorgulamaFRagment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, liste).addToBackStack(null).commit();

        } else if (id == R.id.nav_sifreAdmin) {
            SifremiDegistirFragment liste = new SifremiDegistirFragment();
            liste.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, liste).addToBackStack(null).commit();


        }else if (id == R.id.nav_cikis) {
            cekisDialog();

        }  /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cekisDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Çekiş yapacağından emin misiniz?");

        alertDialogBuilder.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);


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
