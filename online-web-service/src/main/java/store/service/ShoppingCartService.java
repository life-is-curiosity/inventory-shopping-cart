package store.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.common.JsonUtils;
import store.entity.*;
import store.enums.ProductStatusEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ShoppingCartService {

  @Autowired private ProductService productService;

  @Transactional
  public JSONObject addItemToCart(
      int userId, Map<Integer, ShoppingCart> cartMap, ShoppingCartItem item) {
    ShoppingCart cart = cartMap.get(userId);
    Product product = productService.getProductInfo(Integer.parseInt(item.getProductId()));
    product.setQuantity(product.getQuantity() - item.getQuantity());
    List<ShoppingCartItem> items;
    if (cart == null) {
      items = new CopyOnWriteArrayList<>();
      items.add(item);
      cart = new ShoppingCart(userId, items, false);
      cartMap.put(userId, cart);
      productService.updateProductInfo(product);
    } else {
      items = cart.getItems();
      for (ShoppingCartItem cartItem : items) {
        if (cartItem.getProductId().equals(item.getProductId())) {
          if (!item.isBundle()) {
            cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
            productService.updateProductInfo(product);
            return JsonUtils.responseBodyBuilder(true, "");
          } else {
            item.setBundle(true);
            items.add(item);
            productService.updateProductInfo(product);
            return JsonUtils.responseBodyBuilder(true, "");
          }
        }
      }
      items.add(item);
      productService.updateProductInfo(product);
    }
    return JsonUtils.responseBodyBuilder(true, "");
  }

  public JSONObject removeItemFromCart(
      int userId, Map<Integer, ShoppingCart> cartMap, ShoppingCartItem item) {
    ShoppingCart cart = cartMap.get(userId);
    if (cart == null) {
      return JsonUtils.responseBodyBuilder(
          false, "Failed to remove the product from shopping cart");
    } else {
      int counter = 0;
      List<ShoppingCartItem> items = cart.getItems();
      for (Iterator<ShoppingCartItem> iter = items.listIterator(); iter.hasNext(); ) {
        ShoppingCartItem cartItem = iter.next();
        if (cartItem.getProductId().equals(item.getProductId()) && !cartItem.isBundle()) {
          items.remove(counter);
          return JsonUtils.responseBodyBuilder(true, "");
        }
        counter++;
      }
      return JsonUtils.responseBodyBuilder(
          false, "Failed to remove the product from shopping cart");
    }
  }

  public JSONObject updateItemFromCart(
      int userId, Map<Integer, ShoppingCart> cartMap, ShoppingCartItem item) {
    ShoppingCart cart = cartMap.get(userId);
    if (cart == null) {
      return JsonUtils.responseBodyBuilder(
          false, "Failed to update the product from shopping cart");
    } else {
      List<ShoppingCartItem> items = cart.getItems();
      for (ShoppingCartItem cartItem : items) {
        if (cartItem.getProductId().equals(item.getProductId())) {
          Product product = productService.getProductInfo(Integer.parseInt(item.getProductId()));
          product.setQuantity(product.getQuantity() + cartItem.getQuantity() - item.getQuantity());
          productService.updateProductInfo(product);
          cartItem.setQuantity(item.getQuantity());
          return JsonUtils.responseBodyBuilder(true, "");
        }
      }
      return JsonUtils.responseBodyBuilder(
          false, "Failed to remove the product from shopping cart");
    }
  }

  public JSONObject checkOutFromCart(int userId, Map<Integer, ShoppingCart> cartMap) {
    ShoppingCart checkOutCart = cartMap.get(userId);
    if (checkOutCart == null) {
      return JsonUtils.responseBodyBuilder(false, "Shopping cart is empty");
    }
    if (checkOutCart.isCheckedOut()) {
      return JsonUtils.responseBodyBuilder(false, "Shopping cart has already checked out", cartMap);
    } else {
      checkOutCart.setCheckedOut(true);
      List<ShoppingCartItem> items = checkOutCart.getItems();
      if (items != null && items.size() > 0) {
        int counter = 0;
        for (ShoppingCartItem item : items) {
          Product product = productService.getProductInfo(Integer.parseInt(item.getProductId()));
          if (product.getStatus() == ProductStatusEnum.DISABLE.getProductStatus()) {
            items.remove(counter);
          } else {
            addBundleProducts(userId, cartMap, item);
          }
          counter++;
        }
        BigDecimal totalPrice = applyDiscountForProducts(userId, cartMap, items);
        checkOutCart = cartMap.remove(userId);
        checkOutCart.setTotalPrice(totalPrice.stripTrailingZeros().toPlainString());
        return JsonUtils.responseBodyBuilder(true, "", checkOutCart);
      } else {
        return JsonUtils.responseBodyBuilder(false, "Shopping cart does not have any product");
      }
    }
  }

  public BigDecimal applyDiscountForProducts(
      int userId, Map<Integer, ShoppingCart> cartMap, List<ShoppingCartItem> items) {
    BigDecimal totalPrice = BigDecimal.ZERO;
    for (ShoppingCartItem item : items) {
      if (!item.isBundle()) {
        Product product = productService.getProductInfo(Integer.parseInt(item.getProductId()));
        int totalQuantity = item.getQuantity();
        BigDecimal originalPrice;
        if (totalQuantity > 1) {
          DiscountDeal discountDeals =
              productService.getDiscountDeals(Integer.parseInt(item.getProductId()));
          if (discountDeals != null) {
            BigDecimal halfQuantity =
                new BigDecimal(totalQuantity).divide(new BigDecimal(2), 0, RoundingMode.UP);
            BigDecimal discountPrice =
                halfQuantity.multiply(BigDecimal.valueOf(product.getPrice()));
            if (totalQuantity % 2 != 0) {
              halfQuantity = halfQuantity.subtract(BigDecimal.ONE);
              originalPrice =
                  halfQuantity
                      .multiply(BigDecimal.valueOf(product.getPrice()))
                      .multiply(BigDecimal.valueOf(discountDeals.getDiscountRate()));
            } else {
              discountPrice =
                  discountPrice.multiply(BigDecimal.valueOf(discountDeals.getDiscountRate()));
              originalPrice = halfQuantity.multiply(BigDecimal.valueOf(product.getPrice()));
            }
            BigDecimal unitPrice = discountPrice.add(originalPrice);
            item.setPrice(unitPrice.stripTrailingZeros().toPlainString());
            totalPrice = totalPrice.add(discountPrice.add(originalPrice));
          } else {
            originalPrice = BigDecimal.valueOf(product.getPrice());
            item.setPrice(originalPrice.stripTrailingZeros().toPlainString());
            totalPrice =
                totalPrice.add(originalPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
          }
        } else {
          originalPrice = BigDecimal.valueOf(product.getPrice());
          item.setPrice(originalPrice.stripTrailingZeros().toPlainString());
          totalPrice =
              totalPrice.add(originalPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
        }
      }
    }
    return totalPrice;
  }

  public void addBundleProducts(
      int userId, Map<Integer, ShoppingCart> cartMap, ShoppingCartItem item) {
    List<BundleDeal> bundleDeals =
        productService.getBundleDeals(Integer.parseInt(item.getProductId()));
    if (bundleDeals != null && bundleDeals.size() > 0) {
      for (BundleDeal bundleDeal : bundleDeals) {
        ShoppingCartItem bundleProduct = new ShoppingCartItem();
        bundleProduct.setProductId(String.valueOf(bundleDeal.getBundleProductId()));
        Product bundleProductInfo = productService.getProductInfo(bundleDeal.getBundleProductId());
        if (bundleProductInfo.getQuantity() > 0
            && bundleProductInfo.getStatus() == ProductStatusEnum.ENABLE.getProductStatus()) {
          bundleProduct.setQuantity(item.getQuantity());
          bundleProduct.setBundle(true);
          addItemToCart(userId, cartMap, bundleProduct);
        }
      }
    }
  }
}
