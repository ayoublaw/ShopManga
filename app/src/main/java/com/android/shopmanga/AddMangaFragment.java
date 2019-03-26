package com.android.shopmanga;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import es.dmoral.toasty.Toasty;


public class AddMangaFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_manga, container, false);



        Button button =  view.findViewById(R.id.AddFragementButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressBar progressbar = view.findViewById(R.id.AddMangaFragmentprogressBar);
                progressbar.setVisibility(View.VISIBLE);

                EditText mangaName = view.findViewById(R.id.AddMangaName);
                EditText price = view.findViewById(R.id.AddMangaPrice);
                EditText sellerName = view.findViewById(R.id.AddMangaSellerName);
                EditText address = view.findViewById(R.id.AddMangaAddres);
                EditText telephone = view.findViewById(R.id.AddMangaTelephone);

                String mangaNameString = mangaName.getText().toString();
                String priceString = price.getText().toString();
                String sellerNameString = sellerName.getText().toString();
                String addressString = address.getText().toString();
                String telephoneString = telephone.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="https://shopmangamobileapi.herokuapp.com/AddManga";

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("name", mangaNameString);
                    jsonBody.put("price", priceString);
                    jsonBody.put("sellerName", sellerNameString);
                    jsonBody.put("telephone", telephoneString);
                    jsonBody.put("adresse", addressString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressbar.setVisibility(View.GONE);
                        Toasty.success(getContext(),"Operation succed",Toasty.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        Toasty.error(getContext(),"Error : We can't Add This manga",Toasty.LENGTH_LONG).show();
                    }
                });
                queue.add(jsonObjectRequest);
            }
        });
        return view;
    }

 }
