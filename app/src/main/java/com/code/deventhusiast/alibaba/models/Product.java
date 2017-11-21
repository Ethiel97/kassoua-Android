package com.code.deventhusiast.alibaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product implements Parcelable {

    @SerializedName("category")
    private Category category;

    @SerializedName("comments")
    private List<Object> comments;

    @SerializedName("min_quantity")
    private int minQuantity;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("price")
    private int price;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("product_photos")
    private List<ProductPhotosItem> productPhotos;

    @SerializedName("title")
    private String title;

    @SerializedName("user")
    private User user;

    public Product(int price) {
        this.price = price;
    }

    protected Product(Parcel in) {
        categoryId = in.readInt();
        minQuantity = in.readInt();
        updatedAt = in.readString();
        userId = in.readInt();
        price = in.readInt();
        description = in.readString();
        createdAt = in.readString();
        id = in.readInt();
        productPhotos = in.createTypedArrayList(ProductPhotosItem.CREATOR);
        title = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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

    public void setProductPhotos(List<ProductPhotosItem> productPhotos) {
        this.productPhotos = productPhotos;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ProductPhotosItem> getProductPhotos() {
        return productPhotos;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(categoryId);
        parcel.writeInt(minQuantity);
        parcel.writeString(updatedAt);
        parcel.writeInt(userId);
        parcel.writeInt(price);
        parcel.writeString(description);
        parcel.writeString(createdAt);
        parcel.writeInt(id);
        parcel.writeTypedList(productPhotos);
        parcel.writeString(title);
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(category,i);
    }
}