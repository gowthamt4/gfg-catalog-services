package com.gfg.catalog.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.gfg.catalog.entities.Product;

/**
 * Interface for Product managing services
 */
public interface CatalogService {

  /**
   * Abstract method to get the details of a Product
   */
  Product getProductDetails(final UUID id);

  /**
   * Abstract method to create a new Product
   */
  void createProduct(final Product product);

  /**
   * Abstract method to edit the product details
   */
  void editProductDetails(final UUID id, final Product product);

  /**
   * Abstract method to delete a product
   */
  void deleteProduct(final UUID id);

  /**
   * Abstract method to create products in batches
   */
  void createNewProducts(final List<Product> products);


  /**
   * Abstract method to get the products in pages
   */
  Page<Product> getPaginatedProducts(List<UUID> formattedProdIds, String title, String description,
      int page, int size, String orderBy, String direction);

}
