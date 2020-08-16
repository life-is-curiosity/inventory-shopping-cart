package store.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import store.entity.BundleDeal;
import store.entity.DiscountDeal;
import store.entity.Product;

import java.util.List;

public interface ProductDAO extends CrudRepository<Product, Integer> {

  @Transactional
  @Modifying
  @Query(
      "update Product p "
          + "set p.name = :name, p.status = :status, p.quantity = :quantity, p.description = :description, p.price = :price "
          + "where p.id = :id")
  int updateProductById(
      @Param("name") String name,
      @Param("status") int status,
      @Param("quantity") int quantity,
      @Param("description") String description,
      @Param("price") Double price,
      @Param("id") int id);

  @Query("select t from Product t ")
  List<Product> getProductList();

  @Query("select t from Product t where t.id = :id ")
  Product getProductInfo(@Param("id") int id);

  @Transactional
  @Modifying
  @Query(
      value =
          "insert into product (name, description, status, price, quantity) "
              + "values (:name, :description, :status, :price, :quantity)",
      nativeQuery = true)
  int addProduct(
      @Param("name") String name,
      @Param("description") String description,
      @Param("status") int status,
      @Param("price") Double price,
      @Param("quantity") int quantity);

  @Transactional
  @Modifying
  @Query(
      value =
          "insert into discount_deal (product_id, discount_rate) values (:productId, :discountRate)",
      nativeQuery = true)
  int addDiscountDeal(
      @Param("productId") int productId, @Param("discountRate") Double discountRate);

  @Transactional
  @Modifying
  @Query(
      value =
          "insert into bundle_deal (product_id, bundle_product_id) values (:productId, :bundleProductId)",
      nativeQuery = true)
  int addBundleDeal(
      @Param("productId") int productId, @Param("bundleProductId") int bundleProductId);

  @Query("select bd from BundleDeal bd where bd.productId = :product_id")
  List<BundleDeal> getBundleDeals(@Param("product_id") int productId);

  @Query("select dd from DiscountDeal dd where dd.productId = :product_id")
  DiscountDeal getDiscountDeals(@Param("product_id") int productId);
}
