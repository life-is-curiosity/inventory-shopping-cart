package store.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.common.JsonUtils;
import store.dao.ProductDAO;
import store.entity.BundleDeal;
import store.entity.DiscountDeal;
import store.entity.Product;
import store.enums.ProductStatusEnum;

import java.util.List;

@Service
public class ProductService {

  @Autowired ProductDAO productDao;

  public JSONObject addProduct(Product product) {
    boolean success;
    String reason = null;
    try {
      success =
          productDao.addProduct(
                  product.getName(),
                  product.getDescription(),
                  product.getStatus(),
                  product.getPrice(),
                  product.getQuantity())
              == 1;
    } catch (Exception e) {
      success = false;
      reason = "can not add product, product->" + product;
    }
    return JsonUtils.responseBodyBuilder(success, reason);
  }

  public List<Product> getProductList() {
    return productDao.getProductList();
  }

  public Product getProductInfo(int id) {
    return productDao.getProductInfo(id);
  }

  public JSONObject updateProductInfo(Product product) {
    boolean success;
    String reason = null;
    Product currentProduct = productDao.getProductInfo(product.getId());
    if (currentProduct == null) {
      return JsonUtils.responseBodyBuilder(false, "Product does not exist");
    } else {
      currentProduct.setName(
          product.getName() == null ? currentProduct.getName() : product.getName());
      currentProduct.setStatus(
          product.getStatus() == null ? currentProduct.getStatus() : product.getStatus());
      currentProduct.setQuantity(
          product.getQuantity() == null ? currentProduct.getQuantity() : product.getQuantity());
      currentProduct.setDescription(
          product.getDescription() == null
              ? currentProduct.getDescription()
              : product.getDescription());
      currentProduct.setPrice(
          product.getPrice() == null ? currentProduct.getPrice() : product.getPrice());
    }
    try {
      success =
          productDao.updateProductById(
                  currentProduct.getName(),
                  currentProduct.getStatus(),
                  currentProduct.getQuantity(),
                  currentProduct.getDescription(),
                  currentProduct.getPrice(),
                  currentProduct.getId())
              == 1;
    } catch (Exception e) {
      success = false;
      reason = "can not update product, product->" + product;
    }
    return JsonUtils.responseBodyBuilder(success, reason, currentProduct);
  }

  public JSONObject addDiscountDeal(DiscountDeal discountDeal) {
    boolean success;
    String reason = "";
    try {
      success =
          productDao.addDiscountDeal(discountDeal.getProductId(), discountDeal.getDiscountRate())
              == 1;
    } catch (Exception e) {
      success = false;
      reason = "can not add discount deal, params->" + discountDeal;
    }
    return JsonUtils.responseBodyBuilder(success, reason);
  }

  public JSONObject addBundleDeal(BundleDeal bundleDeal) {
    boolean success;
    String reason = "";
    Product product = getProductInfo(bundleDeal.getBundleProductId());
    Product bundleProduct = getProductInfo(bundleDeal.getBundleProductId());
    if (product == null || bundleProduct == null) {
      return JsonUtils.responseBodyBuilder(
          false,
          "Product does not exist, product->" + product + ", bundle product ->" + bundleProduct);
    } else {
      if (product.getStatus() == ProductStatusEnum.DISABLE.getProductStatus()
          || bundleProduct.getStatus() == ProductStatusEnum.DISABLE.getProductStatus()) {
        return JsonUtils.responseBodyBuilder(
            false,
            "Product and bundle can not be disabled, product->"
                + product
                + ", bundle product ->"
                + bundleProduct);
      } else {
        try {
          success =
              productDao.addBundleDeal(bundleDeal.getProductId(), bundleDeal.getBundleProductId())
                  == 1;
        } catch (Exception e) {
          success = false;
          reason = "can not add bundle deal, params->" + bundleDeal;
        }
        return JsonUtils.responseBodyBuilder(success, reason);
      }
    }
  }

  public List<BundleDeal> getBundleDeals(int productId) {
    return productDao.getBundleDeals(productId);
  }

  public DiscountDeal getDiscountDeals(int productId) {
    return productDao.getDiscountDeals(productId);
  }
}
