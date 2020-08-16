package store.entity;

import javax.persistence.*;

@Entity
@Table(name = "bundle_deal")
public class BundleDeal {

  @Id @GeneratedValue private int id;

  @Column(nullable = false, name = "product_id")
  private int productId;

  @Column(nullable = false, name = "bundle_product_id")
  private int bundleProductId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getBundleProductId() {
    return bundleProductId;
  }

  public void setBundleProductId(int bundleProductId) {
    this.bundleProductId = bundleProductId;
  }

  @Override
  public String toString() {
    return "BundleDeal{"
        + "id="
        + id
        + ", productId="
        + productId
        + ", bundleProductId="
        + bundleProductId
        + '}';
  }
}
