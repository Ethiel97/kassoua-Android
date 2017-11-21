package com.code.deventhusiast.alibaba.models;

import org.joda.time.DateTime;

/**
 * Created by Ethiel on 24/10/2017.
 */
public class ProductPhotos {
    private String id;
    private String product_id;
    private String filename;
    private DateTime createdAt;
    private DateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
