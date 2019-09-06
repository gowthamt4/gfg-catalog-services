package com.gfg.catalog.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.gfg.catalog.dao.ProductDAO;
import com.gfg.catalog.dao.ProductRepository;
import com.gfg.catalog.entities.Product;
import com.gfg.catalog.exception.ProductNotFoundException;
import com.gfg.catalog.service.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductDAO productDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceImpl.class);
	
	/**
     * Method to pull the Specific Product details
     */
	@Override
	public Product getProductDetails(final UUID id) {
		Product product = productRepo.findById(id).get();
		if (product == null) {
			throw new ProductNotFoundException("Product with id "+id+" doesn't exist");
		}
		return product;
	}

	/**
     * Method to create a new Product
     */
	@Override
	public void createProduct(final Product product) {
		LOGGER.info("Product details "+product);
		productRepo.save(product);
	}

	/**
     * Method to edit the specific the Product details
     */
	@Override
	public void editProductDetails(final UUID id, final Product product) {
		Optional<Product> existingProduct = productRepo.findById(id);
		if(existingProduct.isPresent()) {
			productRepo.save(product);
		} else {
			throw new ProductNotFoundException("Product with Id "+id+" not exists");
		}
	}

	/**
     * Method to delete the product by passing Product ID
     */
	@Override
	public void deleteProduct(final UUID id) {
		Optional<Product> existingProduct = productRepo.findById(id);
		if(existingProduct.isPresent()) {
			productRepo.deleteById(id);
		} else {
			throw new ProductNotFoundException("Product with Id "+id+" not exists");
		}
	}

	@Override
	public void createNewProducts(final List<Product> productList) {
		productRepo.saveAll(productList);
	}

	@Override
	public List<Product> searchProductsByTitleDesc(final String title, final String desc) {
		if(desc == null || desc.isEmpty()) {
			return productRepo.findProdByTitle(title);
		} else if(title ==null || title.isEmpty()) {
			return productRepo.findProdByDesc(desc);
		} else {
			return productRepo.findProdByTitleDesc(title,desc);
		}
	}

	@Override
	public List<Product> getAllProducts(List<UUID> formattedProdIds) {
		if(formattedProdIds != null && !formattedProdIds.isEmpty()) {
			return (List<Product>)productRepo.findAllById(formattedProdIds);
		}else {
			return (List<Product>) productRepo.findAll();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Page<Product> getPaginatedProducts(int page, int size){
		
		 List<Order> orders = new ArrayList<Order>();
		 orders.add(new Sort.Order(Direction.ASC, "brand")); 
		 orders.add(new Sort.Order(Direction.ASC, "price")); 
		 orders.add(new Sort.Order(Direction.ASC, "color")); 
		 Sort sort = new Sort(orders);
		 
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Product> data = productRepo.findAll(pageable);
		return data;
	}


}
