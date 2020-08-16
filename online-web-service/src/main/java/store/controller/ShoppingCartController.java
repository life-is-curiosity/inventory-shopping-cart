package store.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import store.common.JsonUtils;
import store.entity.Product;
import store.entity.ShoppingCart;
import store.entity.ShoppingCartItem;
import store.entity.User;
import store.service.ProductService;
import store.service.ShoppingCartService;
import store.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path = "/shopping-cart")
public class ShoppingCartController {

  public static Map<Integer, ShoppingCart> SHOPPING_CART = new ConcurrentHashMap();
  @Autowired private ProductService productService;
  @Autowired private ShoppingCartService shoppingCartService;
  @Autowired private UserService userService;

  @RequestMapping(path = "/{userId}/add", method = RequestMethod.POST)
  @ResponseBody
  public JSONObject addProductToShoppingCart(
      @PathVariable(value = "userId") int userId, @RequestBody ShoppingCartItem cartItem) {
    User user = userService.getUser(userId);
    if (user == null) {
      return JsonUtils.responseBodyBuilder(false, "User does not exist");
    }
    Product product = productService.getProductInfo(Integer.parseInt(cartItem.getProductId()));
    if (product == null || product.getStatus() <= 0) {
      return JsonUtils.responseBodyBuilder(false, "Product does not exist");
    }
    int onStockQuantity = product.getQuantity();
    if (cartItem.getQuantity() > onStockQuantity) {
      return JsonUtils.responseBodyBuilder(false, "Product quantity is insufficient on stock");
    } else {
      if (cartItem.getQuantity() < 1) {
        return JsonUtils.responseBodyBuilder(false, "Product quantity can not be less than 1");
      }
    }
    return shoppingCartService.addItemToCart(userId, SHOPPING_CART, cartItem);
  }

  @RequestMapping(path = "/{userId}/remove", method = RequestMethod.DELETE)
  @ResponseBody
  public JSONObject removeProductFromShoppingCart(
      @PathVariable(value = "userId") int userId, @RequestBody ShoppingCartItem cartItem) {
    User user = userService.getUser(userId);
    if (user == null) {
      return JsonUtils.responseBodyBuilder(false, "User does not exist");
    }
    return shoppingCartService.removeItemFromCart(userId, SHOPPING_CART, cartItem);
  }

  @RequestMapping(path = "/{userId}/update_quantity", method = RequestMethod.PUT)
  @ResponseBody
  public JSONObject updateProductToShoppingCart(
      @PathVariable(value = "userId") int userId, @RequestBody ShoppingCartItem cartItem) {
    User user = userService.getUser(userId);
    if (user == null) {
      return JsonUtils.responseBodyBuilder(false, "User does not exist");
    }
    Product product = productService.getProductInfo(Integer.parseInt(cartItem.getProductId()));
    if (product == null || product.getStatus() <= 0) {
      return JsonUtils.responseBodyBuilder(false, "Product does not exist");
    }
    if (cartItem.getQuantity() < 1) {
      return JsonUtils.responseBodyBuilder(false, "Product quantity can not be less than 1");
    }
    return shoppingCartService.updateItemFromCart(userId, SHOPPING_CART, cartItem);
  }

  @RequestMapping(path = "/{userId}/check_out", method = RequestMethod.POST)
  public JSONObject getCheckOutInfo(@PathVariable(value = "userId") int userId) {
    User user = userService.getUser(userId);
    if (user == null) {
      return JsonUtils.responseBodyBuilder(false, "User does not exist");
    }
    return shoppingCartService.checkOutFromCart(userId, SHOPPING_CART);
  }
}
