package com.code.deventhusiast.alibaba.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.code.deventhusiast.alibaba.R;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * Created by Ethiel on 10/11/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;
    private Context context;
    private int LIST_ITEM = 0;
    private int GRID_ITEM = 1;
    private boolean isSwitchView = true;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = viewType == LIST_ITEM ?
                LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row_list, parent, false) :
                LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {

        Category category = categories.get(position);

        holder.category_name.setText(category.getName());
        Picasso.with(context).load(APIClient.PHOTO_BASE_URL + category.getImage())
                .fit().centerCrop().placeholder(R.drawable.ico_kassoua).into(holder.category_image);

        holder.itemView.setOnClickListener(view -> {
            Toasty.info(context, "Click on " + category.getName(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, ProductDetailActivity.class);
            Bundle bundle = new Bundle();

            bundle.putParcelable("CATEGORY", category);
           /* intent.putExtras(bundle);
            context.startActivity(intent);*/
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitchView) {
            return GRID_ITEM;
        } else {
            return LIST_ITEM;
        }
    }

    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView category_name;
        private CircleImageView category_image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.category_image = itemView.findViewById(R.id.category_image);
            this.category_name = itemView.findViewById(R.id.category_name);
        }
    }
}
