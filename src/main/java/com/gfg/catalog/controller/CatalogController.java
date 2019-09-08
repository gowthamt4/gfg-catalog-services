package com.gfg.catalog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gfg.catalog.dto.ProductResponse;
import com.gfg.catalog.entities.Product;
import com.gfg.catalog.exception.ProductNotFoundException;
import com.gfg.catalog.service.CatalogService;
import com.gfg.catalog.utils.HateoasFormation;
import com.gfg.catalog.utils.Link;

/**
 * This is the RestController to manage Products
 */
@RestController
public class CatalogController {

  @Value("${spring.data.web.pageable.default-page-size}")
  private Integer defaultSize;
  @Value("${page.default}")
  private Integer defaultPage;
  @Value("${orderByField.default}")
  private String defaultOrderByField;
  @Value("${direction.default}")
  private String defaultOrderDirection;

  /**
   * Injecting CatalogService bean to handle CRUD Operations for products
   */
  @Autowired
  private CatalogService catalogService;

  /**
   * This method invokes the Catalog Service implementation to fetch the details of a specific
   * product when the id is a Path Parameter
   */
  @RequestMapping("/products/{id}")
  public Product getProduct(@PathVariable("id") UUID id) {
    Product product = catalogService.getProductDetails(id);
    if (product == null) {
      throw new ProductNotFoundException("Product with ProductID " + id + " doesn't exists");
    }
    return product;
  }

  /**
   * This method invokes the Catalog Service implementation to fetch the details of the products in
   * the form of pages conditions apply
   */
  @RequestMapping("/products")
  public ProductResponse getAllProducts(HttpServletRequest request,
      @RequestParam(value = "id", required = false) String productIds,
      @RequestParam(value = "title", required = false) final String title,
      @RequestParam(value = "description", required = false) final String description,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "orderBy", required = false) String orderBy,
      @RequestParam(value = "direction", required = false) String direction) {
    List<UUID> formattedProdIds = new ArrayList<UUID>();
    formattedProdIds = formatStringtoUUID(productIds);
    if (page == null) {
      page = defaultPage;
    }
    if (size == null) {
      size = defaultSize;
    }
    if (direction == null) {
      direction = defaultOrderDirection;
    }
    if (orderBy == null) {
      orderBy = defaultOrderByField;
    }

    Page<Product> pageFormatProds = catalogService.getPaginatedProducts(formattedProdIds, title,
        description, page, size, orderBy, direction);
    HateoasFormation hateoas = new HateoasFormation();
    List<Link> links = hateoas.formHateoas(request, pageFormatProds.getTotalPages(), page, size);
    return new ProductResponse(pageFormatProds, links);
  }

  /**
   * This method invokes the Catalog Service implementation to create a new Product
   */
  @RequestMapping(method = RequestMethod.POST, value = "/products")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void createNewProduct(@Valid @RequestBody Product product) {
    catalogService.createProduct(product);
  }

  /**
   * This method invokes the Catalog Service implementation to edit the existing Product
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/products/{id}")
  public void editProductDetails(@PathVariable UUID id, @Valid @RequestBody Product product) {
    catalogService.editProductDetails(id, product);
  }

  /**
   * This method invokes the Catalog Service implementation to delete the existing Product
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/products/{id}")
  public void deleteProduct(@PathVariable UUID id) {
    catalogService.deleteProduct(id);
  }

  /**
   * This method invokes the Catalog Service implementation to create a new Product
   */
  @RequestMapping(method = RequestMethod.POST, value = "/products/batch")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void createNewProducts(@Valid @RequestBody List<Product> products) {
    catalogService.createNewProducts(products);
  }

  /**
   * This method converts the productIds obtained from requestbody from String to UUID List
   */
  private List<UUID> formatStringtoUUID(String productIds) {
    if (productIds != null) {
      final List<UUID> formattedProdIds = new ArrayList<UUID>();
      final String[] prodIds = productIds.split(",");
      for (String prodId : prodIds) {
        formattedProdIds.add(UUID.fromString(prodId));
      }
      return formattedProdIds;
    } else {
      return new ArrayList<UUID>();
    }
  }

}
