package store.enums;

public enum ProductStatusEnum {
  ENABLE(1),
  DISABLE(0);

  int status;

  ProductStatusEnum(int status) {
    this.status = status;
  }

  public int getProductStatus() {
    return this.status;
  }
}
