package com.android.shopmanga;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MangadetailsActivity extends AppCompatActivity {

    ImageView image;
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
    }
    private void LoadImageFromUrl(String url){
        Picasso.with(this).load(url)
                .error(R.mipmap.ic_launcher)
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
