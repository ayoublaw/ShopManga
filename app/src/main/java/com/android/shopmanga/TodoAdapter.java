package com.android.shopmanga;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class TodoAdapter extends ArrayAdapter<Manga> {
    private final int layout;

    public TodoAdapter(@NonNull Context context, int resource, @NonNull List<Manga> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null){
            // Create new view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layout, null);
        } else {
            view = convertView;
        }

        Manga item = getItem(position);

        // Recycle view
        TextView itemMangaName = view.findViewById(R.id.ItemMangaName);
        TextView itemMangaPrice = view.findViewById(R.id.ItemMangaPrice);
        TextView itemMangaAddress= view.findViewById(R.id.ItemMangaAddress);
        itemMangaName.setText(item.getMangaName());
        itemMangaAddress.setText(item.getAddress());
        itemMangaPrice.setText(Double.toString(item.getPrice()) + " euro");

        return view;

    }

}

