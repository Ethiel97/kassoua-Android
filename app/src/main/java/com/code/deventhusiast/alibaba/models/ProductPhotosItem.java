package com.code.deventhusiast.alibaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductPhotosItem implements Parcelable {

    @SerializedName("filename")
    private String filename;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    protected ProductPhotosItem(Parcel in) {
        filename = in.readString();
        updatedAt = in.readString();
        productId = in.readInt();
        createdAt = in.readString();
        id = in.readInt();
    }

    public static final Creator<ProductPhotosItem> CREATOR = new Creator<ProductPhotosItem>() {
        @Override
        public ProductPhotosItem createFromParcel(Parcel in) {
            return new ProductPhotosItem(in);
        }

        @Override
        public ProductPhotosItem[] newArray(int size) {
            return new ProductPhotosItem[size];
        }
    };

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "ProductPhotosItem{" +
                        "filename = '" + filename + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",product_id = '" + productId + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filename);
        parcel.writeString(updatedAt);
        parcel.writeInt(productId);
        parcel.writeString(createdAt);
        parcel.writeInt(id);
    }
}