package store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "discount_deal")
public class DiscountDeal {

  @Column(nullable = false, name = "product_id")
  @Id
  private int productId;

  @Column(nullable = false, name = "discount_rate")
  private Double discountRate;

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public Double getDiscountRate() {
    return discountRate;
  }

  public void setDiscountRate(Double discountRate) {
    this.discountRate = discountRate;
  }

  @Override
  public String toString() {
    return "DiscountDeal{" + "productId=" + productId + ", discountRate=" + discountRate + '}';
  }
}
