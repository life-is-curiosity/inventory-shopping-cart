package store.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@JsonDeserialize
public class ShoppingCartItem {

  public String productId;

  public int quantity;

  public boolean isBundle;

  public String price;

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public boolean isBundle() {
    return isBundle;
  }

  public void setBundle(boolean bundle) {
    isBundle = bundle;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "ShoppingCartItem{"
        + "productId='"
        + productId
        + '\''
        + ", quantity="
        + quantity
        + ", isBundle="
        + isBundle
        + '}';
  }
}
