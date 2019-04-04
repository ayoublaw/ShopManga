package com.android.shopmanga;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MangadetailsActivity extends AppCompatActivity {

    ImageView image;
    TelephonyManager telephonyManager; // pour recuperer l'etat de l'application 'telephone'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangadetails);

        Manga manga = (Manga) getIntent().getSerializableExtra("manga");

        TextView mangaName = findViewById(R.id.mangaName);
        TextView price = findViewById(R.id.price);
        TextView sellerName = findViewById(R.id.sellerName);
        TextView address = findViewById(R.id.address);
        TextView telephone = findViewById(R.id.telephone);
        image = findViewById(R.id.imageView);
        LoadImageFromUrl("https://cdn.mangaeden.com/mangasimg/"+ manga.getImageUrl());

        mangaName.setText( "Manga name :  "+manga.getMangaName());
        price.setText(     "Price :       "+Double.toString(manga.getPrice()));
        sellerName.setText("Seller Name : "+manga.getSellerName());
        address.setText(   "Addresse :    "+manga.getAddress());
        telephone.setText( "Telephone :   "+manga.getTelephone());

        // ici on gere le bouton call
        ImageView callButton = findViewById(R.id.call_button);
        telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        callButton.setOnClickListener(v -> {
            if(telephonyManager.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
                Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephone.getText()));
                startActivity(appel);
            }
        });
    }
    private void LoadImageFromUrl(String url){
        Picasso.with(this).load(url)
                .error(R.mipmap.ic_launcher)
                .resize(720,720)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {

                    }
                });
    }
}
