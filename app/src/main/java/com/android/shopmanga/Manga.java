package com.android.shopmanga;

import org.json.JSONException;
import org.json.JSONObject;

public class Manga {
    private String address;
    private double lat;
    private double lng;
    private int price;
    private String sellerName;
    private String telephone;
    private String mangaName;

    public Manga(String address, double lat, double lng, int price, String sellerName, String telephone, String mangaName) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.price = price;
        this.sellerName = sellerName;
        this.telephone = telephone;
        this.mangaName = mangaName;
    }

    public Manga(JSONObject json) throws JSONException {
            this.address = json.getJSONObject("map").getJSONObject("address").getJSONObject("map").getString("address");
            this.lat = json.getJSONObject("map").getJSONObject("address").getJSONObject("map").getDouble("lat");
            this.lng = json.getJSONObject("map").getJSONObject("address").getJSONObject("map").getDouble("lng");
            this.price = json.getJSONObject("map").getInt("price");
            this.sellerName = json.getJSONObject("map").getString("sellerName");
            this.telephone = json.getJSONObject("map").getString("telephone");
            this.mangaName= json.getJSONObject("map").getString("mangaName");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMangaName() {
        return mangaName;
    }

    public void setMangaName(String mangaName) {
        this.mangaName = mangaName;
    }
}
