package com.gfg.catalog.service.impl;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.util.ReflectionTestUtils;

import com.gfg.catalog.dao.ProductRepository;
import com.gfg.catalog.entities.Product;
import com.gfg.catalog.exception.ProductNotFoundException;


public class CatalogServiceImplTest {
  @InjectMocks
  private CatalogServiceImpl catalogServiceImpl;

  private Product product;
  private Product newProduct;
  private Pageable pageable;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    product = new Product();
    product.setId(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    product.setBrand("Apple");
    product.setColor("white");
    product.setDescription("Apple");
    product.setPrice(35000.0);
    product.setTitle("Laptop");

    Sort sort = null;
    List<Order> orders = new ArrayList<Order>();
    orders.add(new Sort.Order(Direction.fromString("ASC"), "brand"));
    sort = new Sort(orders);
    pageable = new PageRequest(0, 1, sort);
  }

  @Test
  public void testGetProductDetails() {
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    Mockito.when(productRepo.findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f")))
        .thenReturn(Optional.of(product));

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);

    Product product = catalogServiceImpl
        .getProductDetails(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Assert.assertNotNull(product);
    Mockito.verify(productRepo).findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
  }

  @Test(expected = ProductNotFoundException.class)
  public void testProductNotFoundExceptionToGetSpecificProduct() {
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    Mockito.when(productRepo.findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f")))
        .thenReturn(Optional.empty());

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);
    Product product = catalogServiceImpl
        .getProductDetails(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Mockito.verify(productRepo).findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
  }

  @Test
  public void testDeleteProduct() {
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    Mockito.when(productRepo.findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f")))
        .thenReturn(Optional.of(product));
    Mockito.doNothing().when(productRepo)
        .deleteById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);

    catalogServiceImpl.deleteProduct(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Mockito.verify(productRepo).findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Mockito.verify(productRepo).deleteById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
  }

  @Test(expected = ProductNotFoundException.class)
  public void testProductNotFoundExceptionToDeleteSpecificProduct() {
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    Mockito.when(productRepo.findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f")))
        .thenReturn(Optional.empty());

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);
    catalogServiceImpl.deleteProduct(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Mockito.verify(productRepo).findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Mockito.verify(productRepo, never())
        .deleteById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
  }

  @Test
  public void testEditProduct() {
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    Mockito.when(productRepo.findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f")))
        .thenReturn(Optional.of(product));
    Mockito.when(productRepo.save(product)).thenReturn(product);

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);

    catalogServiceImpl.editProductDetails(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"),
        product);
    Mockito.verify(productRepo).findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Mockito.verify(productRepo).save(product);
  }

  @Test(expected = ProductNotFoundException.class)
  public void testProductNotFoundExceptionToEditSpecificProduct() {
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    Mockito.when(productRepo.findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f")))
        .thenReturn(Optional.empty());

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);
    catalogServiceImpl.editProductDetails(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"),
        product);
    Mockito.verify(productRepo).findById(UUID.fromString("bb4576cc-ba99-44b7-9cda-af23ef9cee0f"));
    Mockito.verify(productRepo, never()).save(product);
  }

  @Test
  public void testCreateNewProduct() {
    newProduct = new Product();
    newProduct.setBrand("Apple");
    newProduct.setColor("white");
    newProduct.setDescription("Apple");
    newProduct.setPrice(35000.0);
    newProduct.setTitle("Laptop");
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    Mockito.when(productRepo.save(newProduct)).thenReturn(newProduct);

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);

    catalogServiceImpl.createProduct(newProduct);
    Mockito.verify(productRepo).save(newProduct);
  }



  @Test
  public void testCreateNewProducts() {
    ProductRepository productRepo = Mockito.mock(ProductRepository.class);
    newProduct = new Product();
    newProduct.setBrand("Apple");
    newProduct.setColor("white");
    newProduct.setDescription("Apple");
    newProduct.setPrice(35000.0);
    newProduct.setTitle("Laptop");
    List<Product> productList = new ArrayList<Product>();
    productList.add(newProduct);
    Mockito.when(productRepo.saveAll(productList)).thenReturn(productList);

    ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);

    catalogServiceImpl.createNewProducts(productList);
    Mockito.verify(productRepo).saveAll(productList);
  }



  /*
   * @Test public void testPaginatedProductsWithTitle() { ProductRepository productRepo =
   * Mockito.mock(ProductRepository.class); String title = "Apple"; Page p = new Page
   * //Page<Product> p = Mockito.when(productRepo.findProdByTitle(title,
   * pageable)).thenReturn(value);
   * 
   * ReflectionTestUtils.setField(catalogServiceImpl, "productRepo", productRepo);
   * 
   * catalogServiceImpl.createProduct(newProduct); Mockito.verify(productRepo).save(newProduct); }
   */

}
