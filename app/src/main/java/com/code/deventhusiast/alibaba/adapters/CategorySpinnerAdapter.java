package com.code.deventhusiast.alibaba.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.code.deventhusiast.alibaba.R;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ethiel on 14/11/2017.
 */

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List data;
    Category category = null;
    public Resources res;
    LayoutInflater inflater;

    public CategorySpinnerAdapter(@NonNull Context context, int textViewResourceId, @NonNull List objects, Resources resLocal) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.data = objects;
        this.res = resLocal;
        inflater = (LayoutInflater.from(context));
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.category_item_spinner, parent, false);

        category = (Category) data.get(position);

        TextView categoryName = row.findViewById(R.id.category_name);
        TextView categoryId = row.findViewById(R.id.category_id);
        CircleImageView categoryImage = row.findViewById(R.id.category_image);

        if (position == 0) {
            categoryName.setText("Categorie");
        } else {
            categoryId.setText(String.valueOf(category.getId()));
            categoryName.setText(category.getName());
            Picasso.with(context).load(APIClient.PHOTO_BASE_URL + category.getImage()).into(categoryImage);
        }
        return row;
    }
}
