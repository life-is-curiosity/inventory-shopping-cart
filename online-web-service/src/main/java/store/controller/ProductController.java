package store.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import store.common.JsonUtils;
import store.entity.BundleDeal;
import store.entity.DiscountDeal;
import store.entity.Product;
import store.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
public class ProductController {

  @Autowired ProductService productService;

  @RequestMapping(path = "/info", method = RequestMethod.GET)
  public JSONObject getProduct(@RequestParam("id") int id) {
    return JsonUtils.responseBodyBuilder(true, null, productService.getProductInfo(id));
  }

  @RequestMapping(path = "/list", method = RequestMethod.GET)
  public List<Product> getScoreList() {
    return productService.getProductList();
  }

  @RequestMapping(path = "/add", method = RequestMethod.POST)
  public JSONObject addProductInfo(@RequestBody Product product) {
    return productService.addProduct(product);
  }

  @RequestMapping(path = "/update", method = RequestMethod.PUT)
  public JSONObject updateProductInfo(@RequestBody Product product) {
    return productService.updateProductInfo(product);
  }

  @RequestMapping(path = "/discount_deal/add", method = RequestMethod.POST)
  public JSONObject addDiscountDeal(@RequestBody DiscountDeal discountDeal) {
    return productService.addDiscountDeal(discountDeal);
  }

  @RequestMapping(path = "/bundle_deal/add", method = RequestMethod.POST)
  public JSONObject addDiscountDeal(@RequestBody BundleDeal bundleDeal) {
    return productService.addBundleDeal(bundleDeal);
  }
}
