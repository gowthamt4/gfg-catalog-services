package com.gfg.catalog.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gfg.catalog.entities.Product;
import com.gfg.catalog.utils.Link;

public class ProductResponse {
	Page<Product> product;
	List<Link> links;
	
	public ProductResponse(Page<Product> product, List<Link> links) {
		super();
		this.product = product;
		this.links = links;
	}
	/**
	 * @return the product
	 */
	public Page<Product> getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(Page<Product> product) {
		this.product = product;
	}
	/**
	 * @return the links
	 */
	public List<Link> getLinks() {
		return links;
	}
	/**
	 * @param links the links to set
	 */
	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
