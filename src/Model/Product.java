/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author francisco
 */
public class Product {

    private ObservableList<Part> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    // Constructor
    public Product() {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    // Getters
    public IntegerProperty productIDProperty() {
        return productID;
    }

    public StringProperty productNameProperty() {
        return name;
    }

    public DoubleProperty productPriceProperty() {
        return price;
    }

    public IntegerProperty productInvProperty() {
        return inStock;
    }

    public IntegerProperty productMinProperty() {
        return min;
    }

    public IntegerProperty productMaxProperty() {
        return max;
    }

    public int getProductID() {
        return this.productID.get();
    }

    public String getProductName() {
        return this.name.get();
    }

    public double getProductPrice() {
        return this.price.get();
    }

    public int getProductInStock() {
        return this.inStock.get();
    }

    public int getProductMin() {
        return this.min.get();
    }

    public int getProductMax() {
        return this.max.get();
    }

    public ObservableList getProductParts() {
        return parts;
    }

    //// Setters
    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public void setProductName(String name) {
        this.name.set(name);
    }

    public void setProductPrice(double price) {
        this.price.set(price);
    }

    public void setProductInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setProductMin(int min) {
        this.min.set(min);
    }

    public void setProductMax(int max) {
        this.max.set(max);
    }

    public void setProductParts(ObservableList<Part> parts) {
        this.parts = parts;
    }

    //// Validation
    public static String validateProduct(String name, int min, int max, int inventory, double price, ObservableList<Part> parts, String errorMessage) {
        double priceOfParts = 0.00;
        for (int i = 0; i < parts.size(); i++) {
            priceOfParts = priceOfParts + parts.get(i).getPartPrice();
        }

        if (inventory < 1) {
            errorMessage = errorMessage + "The inventory needs at least one part  ";
        }
        if (price <= 0) {
            errorMessage = errorMessage + "The price must be higher than zero. ";
        }
        if (max < min) {
            errorMessage = errorMessage + "The Max must be greater than or equal to the Min. ";
        }
        if (inventory < min || inventory > max) {
            errorMessage = errorMessage + "The inventory must be between the Min and Max values. ";
        }
        if (priceOfParts > price) {
            errorMessage = errorMessage + "Price of the Product cannot be less than the cost of the parts ";
        }

        if (name == null) {
            errorMessage = errorMessage + "Name field is required ";
        }
        if (parts.size() < 1) {
            errorMessage = errorMessage + ("Product must contain at least 1 part");
        }
        return errorMessage;
    }
}
