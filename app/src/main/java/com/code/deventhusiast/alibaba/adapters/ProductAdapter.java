package com.code.deventhusiast.alibaba.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.code.deventhusiast.alibaba.ProductDetailActivity;
import com.code.deventhusiast.alibaba.R;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.models.Category;
import com.code.deventhusiast.alibaba.models.Product;
import com.code.deventhusiast.alibaba.models.ProductPhotosItem;
import com.code.deventhusiast.alibaba.models.User;
import com.code.deventhusiast.alibaba.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    private Context context;
    private int LIST_ITEM = 0;
    private int GRID_ITEM = 1;
    private boolean isSwitchView = true;

    //    public static String BASE_URL = "http://192.168.137.1:8080/kassouaApp/storage/app/";

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = viewType == LIST_ITEM ?
                LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_list, parent, false) :
                LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_grid, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().useFont(Typeface.SANS_SERIF).width(30).height(30).endConfig().round();

        Product product = products.get(position);
        User user = product.getUser();
        Category category = product.getCategory();
        ProductPhotosItem productPhotos = product.getProductPhotos().get(0);

        String username = user.getFname().substring(0, 1) + user.getLname().substring(0, 1);
        TextDrawable drawable = builder.build(username, R.color.colorPrimaryDark);

        holder.price.setText(String.format("%s FCFA/pieces", Integer.toString(product.getPrice())));

        if (product.getTitle().length() < 25) {
            holder.title.setText(product.getTitle());
        } else {
            holder.title.setText(String.format("%s...", product.getTitle().substring(0, 24)));
        }

        holder.min_quantity.setText(String.format("Min: %s", String.valueOf(product.getMinQuantity())));

        holder.created_at.setText(String.format("il ya %s", DateUtil.dateToElapsed(product.getCreatedAt())));

        String photo = user.getPhoto();

        // user.setPhoto(APIClient.PHOTO_BASE_URL + photo);

        if (photo != null) {
            Picasso.with(context).load(APIClient.PHOTO_BASE_URL + user.getPhoto()).fit().centerInside().noFade().into(holder.user_image);
        } else {
            holder.user_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ico_kassoua));
//            holder.user_image.setImageDrawable(drawable);
        }
        // holder.user_image.setImageDrawable(drawable);

        if (holder.getItemViewType() == LIST_ITEM) {
            Picasso.with(context).load(APIClient.PHOTO_BASE_URL + productPhotos.getFilename())
                    .resize(1150, 570).centerInside().into(holder.image);
        } else if (holder.getItemViewType() == GRID_ITEM) {
            Picasso.with(context).load(APIClient.PHOTO_BASE_URL + productPhotos.getFilename())
                    .resize(1150, 560).centerInside().into(holder.image);
        }

        //viewHolder click listener
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            Bundle bundle = new Bundle();

            Toasty.info(context, "Click on " + products.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            bundle.putParcelable("PRODUCT", products.get(position));
            bundle.putParcelable("USER", products.get(position).getUser());
            bundle.putParcelableArrayList("PHOTOS", (ArrayList<? extends Parcelable>) products.get(position).getProductPhotos());
            bundle.putParcelable("CATEGORY", category);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price, title, min_quantity, created_at;
        private ImageView image;
        private CircleImageView user_image;

        public ViewHolder(View view) {
            super(view);
            price = (TextView) view.findViewById(R.id.price);
            title = (TextView) view.findViewById(R.id.title);
            min_quantity = view.findViewById(R.id.min_quantity);
            image = view.findViewById(R.id.image);
            created_at = view.findViewById(R.id.created_at);
            user_image = view.findViewById(R.id.user_image);

        }

    }

}
