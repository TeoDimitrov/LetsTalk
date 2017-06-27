package com.wecode.letstalk.domain.product;

public class Product implements Payable {

    private String productId;

    private String title;

    private String price;

    public Product(String productId, String title, String price) {
        this.productId = productId;
        this.title = title;
        this.price = price;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
