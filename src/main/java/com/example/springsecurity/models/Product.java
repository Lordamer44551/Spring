package com.example.springsecurity.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Название товара не может быть пустым")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание товара не может быть пустым")
    private String description;

    @Column(name="price", nullable = false)
    @Min(value = 1, message = "Цена не может быть меньше 1 рубля")
    private float price;

    @Column(name="warehouse", nullable = false)
    @NotEmpty(message = "Склад не может отсутствовать")
    private String warehouse;

    @Column(name = "seller", nullable = false)
    @NotEmpty(message = "Информация о продавце не может быть пустой")
    private String seller;

    @ManyToOne(optional = false)
    private ProductCategory category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
    private List<Image> imageList = new ArrayList<>();

    // Данный метод позволит добавить фотографию в лист текущего продукта
    public void addImageToProduct(Image image) {
        image.setProduct(this);
        imageList.add(image);
    }
    private LocalDateTime dateTime;

    // Метод заполняет поле даты и времени при создании объекта класса
    @PrePersist
    private void init() {
        dateTime = LocalDateTime.now();
    }

    @ManyToMany
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Order> productList;

    public Product(String title, String description, float price, String warehouse, String seller, ProductCategory category, List<Image> imageList, LocalDateTime dateTime) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
        this.category = category;
        this.imageList = imageList;
        this.dateTime = dateTime;
    }

    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
