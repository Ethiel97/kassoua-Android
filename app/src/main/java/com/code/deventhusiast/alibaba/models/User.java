package com.code.deventhusiast.alibaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.code.deventhusiast.alibaba.api.APIClient;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("enterprise_id")
    private int enterpriseId;

    @SerializedName("photo")
    private String photo;

    @SerializedName("email")
    private String email;

    @SerializedName("job")
    private String job;

    @SerializedName("country")
    private String country;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("gender")
    private String gender;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("enterprise")
    private String enterprise;

    @SerializedName("api_token")
    private String apiToken;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("password")
    private String password;

    @SerializedName("id")
    private int id;


    public User(String country, String fname, String lname, String gender, String enterprise, String password) {
        this.country = country;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.enterprise = enterprise;
        this.password = password;
    }

    public User() {

    }

    public User(int id, String country, String enterprise, String lname, String fname, String email, String gender,
                String job, String address, String phone, String photo, String apiToken) {
        this.id = id;
        this.country = country;
        this.enterprise = enterprise;
        this.lname = lname;
        this.fname = fname;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.job = job;
        this.phone = phone;
        this.photo = APIClient.PHOTO_BASE_URL + photo;
        this.apiToken = apiToken;
    }

    protected User(Parcel in) {
        job = in.readString();
        phone = in.readString();
        photo = in.readString();
        address = in.readString();
        country = in.readString();
        fname = in.readString();
        lname = in.readString();
        gender = in.readString();
        updatedAt = in.readString();
        createdAt = in.readString();
        enterprise = in.readString();
        enterpriseId = in.readInt();
        apiToken = in.readString();
//        password = in.readString();
        id = in.readInt();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLname() {
        return lname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
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

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(job);
        parcel.writeString(phone);
        parcel.writeString(photo);
        parcel.writeString(address);
        parcel.writeString(country);
        parcel.writeString(fname);
        parcel.writeString(lname);
        parcel.writeString(gender);
        parcel.writeString(updatedAt);
        parcel.writeString(createdAt);
        parcel.writeString(enterprise);
        parcel.writeInt(enterpriseId);
        parcel.writeString(apiToken);
        parcel.writeInt(id);
        parcel.writeString(email);
    }
}