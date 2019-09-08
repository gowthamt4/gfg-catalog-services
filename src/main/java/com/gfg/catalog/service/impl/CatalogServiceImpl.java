package com.gfg.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import com.gfg.catalog.dao.ProductRepository;
import com.gfg.catalog.entities.Product;
import com.gfg.catalog.exception.ProductNotFoundException;
import com.gfg.catalog.service.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private ProductRepository productRepo;
	
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
	public List<Product> getAllProducts(List<UUID> formattedProdIds) {
		if(formattedProdIds != null && !formattedProdIds.isEmpty()) {
			return (List<Product>)productRepo.findAllById(formattedProdIds);
		}else {
			return (List<Product>) productRepo.findAll();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Page<Product> getPaginatedProducts(final List<UUID> formattedProdIds, final String title, final String desc, final int page, final int size, String orderBy, String direction){
		Sort sort = null;
		List<Order> orders = new ArrayList<Order>();
		String[] orderByFields = orderBy.split(",");
		for(String orderByField : orderByFields) {
			orders.add(new Sort.Order(Direction.fromString(direction), orderByField)); 
		}
		sort = new Sort(orders);
		Pageable pageable = new PageRequest(page, size, sort);
		if(title != null && !title.isEmpty()) {
			return productRepo.findProdByTitle(title, pageable);
		} else if(desc != null && !desc.isEmpty()) {
			return productRepo.findProdByDesc(desc, pageable);
		} else if(title != null && !title.isEmpty() && desc != null && !desc.isEmpty()){
			return productRepo.findProdByTitleDesc(title,desc,pageable);
		} else if(formattedProdIds != null && !formattedProdIds.isEmpty()) {
			return productRepo.findAllByIds(formattedProdIds, pageable);
		} else {
			return productRepo.findAll(pageable);
		}
	}
}
