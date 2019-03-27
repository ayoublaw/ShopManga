package com.android.shopmanga;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class AddMangaFragment extends Fragment {

    private FusedLocationProviderClient client;
    String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_manga, container, false);
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.AddManga_autocomplete_fragment);
        EditText text = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        Places.initialize(getContext(), "AIzaSyAejI8898winqzlekeYkhyJ2m1ZEPb3im0");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                text.setText(place.getAddress());
                address = place.getAddress();
            }

            @Override
            public void onError(Status status) {
                Toasty.error(getContext(),"Error : We can't use this address",Toasty.LENGTH_LONG).show();
            }
        });

        Button positionButton = view.findViewById(R.id.AddManga_MyPositionButton);
        positionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toasty.error(getContext(),"Error : Permisssion Denied",Toasty.LENGTH_LONG).show();
                    return;
                }
                client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        try {
                            String s = getAddressFromLatLng(location.getLatitude(),location.getLongitude());
                            text.setText(s);
                            address = s;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                client.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getContext(),"Error : We can't find your position",Toasty.LENGTH_LONG).show();
                    }
                });
            }

            private void requestPermission() {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        });

        Button button =  view.findViewById(R.id.AddFragementButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressBar progressbar = view.findViewById(R.id.AddMangaFragmentprogressBar);
                progressbar.setVisibility(View.VISIBLE);

                EditText mangaName = view.findViewById(R.id.AddMangaName);
                EditText price = view.findViewById(R.id.AddMangaPrice);
                EditText sellerName = view.findViewById(R.id.AddMangaSellerName);
                EditText telephone = view.findViewById(R.id.AddMangaTelephone);

                String mangaNameString = mangaName.getText().toString();
                String priceString = price.getText().toString();
                String sellerNameString = sellerName.getText().toString();
                String telephoneString = telephone.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="https://shopmangamobileapi.herokuapp.com/AddManga";

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("name", mangaNameString);
                    jsonBody.put("price", priceString);
                    jsonBody.put("sellerName", sellerNameString);
                    jsonBody.put("telephone", telephoneString);
                    jsonBody.put("adresse", address);
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
    private String getAddressFromLatLng(double lat,double lng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        addresses = geocoder.getFromLocation(lat, lng, 1);
        return addresses.get(0).getAddressLine(0);
    }
 }
