package store.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.util.List;

@JsonDeserialize
public class ShoppingCart {

  private int userId;

  private List<ShoppingCartItem> items;

  private boolean checkedOut;

  private String totalPrice;

  @JsonCreator
  public ShoppingCart(int userId, List<ShoppingCartItem> items, boolean checkedOut) {
    this.userId = userId;
    this.items = items;
    this.checkedOut = checkedOut;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public List<ShoppingCartItem> getItems() {
    return items;
  }

  public void setItems(List<ShoppingCartItem> items) {
    this.items = items;
  }

  public boolean isCheckedOut() {
    return checkedOut;
  }

  public void setCheckedOut(boolean checkedOut) {
    this.checkedOut = checkedOut;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    this.totalPrice = totalPrice;
  }
}
