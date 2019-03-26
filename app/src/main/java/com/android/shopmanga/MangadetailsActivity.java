package com.android.shopmanga;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MangadetailsActivity extends AppCompatActivity {

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

        mangaName.setText( "Manga name :  "+manga.getMangaName());
        price.setText(     "Price :       "+Double.toString(manga.getPrice()));
        sellerName.setText("Seller Name : "+manga.getSellerName());
        address.setText(   "Addresse :    "+manga.getAddress());
        telephone.setText( "Telephone :   "+manga.getTelephone());
    }
}
