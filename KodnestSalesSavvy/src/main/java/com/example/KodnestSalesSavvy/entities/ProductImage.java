package com.example.KodnestSalesSavvy.entities;

import jakarta.persistence.*;

@Entity
@Table(name="productimages")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int image_id;

    @Column
    String image_url;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    Product product;

    public ProductImage() {
    }

    public ProductImage(int image_id, String image_url, Product product) {
        this.image_id = image_id;
        this.image_url = image_url;
        this.product = product;
    }

    public ProductImage(String image_url, Product product) {
        this.image_url = image_url;
        this.product = product;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
