package com.gfg.catalog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.gfg.catalog.utils.Link;

/**
 * This is the RestController to manage users
 */
@RestController
public class CatalogController {
	
	private static Logger logger = LoggerFactory.getLogger(CatalogController.class);
	@Value("${spring.data.web.pageable.default-page-size}")
	private Integer defaultSize;
	@Value("${page.default}")
	private Integer defaultPage;
	
	/**
	 * Injecting CatalogService bean to handle CRUD Operations for products
	 */
	@Autowired
	private CatalogService catalogService;
	
	/**
	 * This method invokes the Catalog Service implementation to fetch the details of a specific product
	 */
	@RequestMapping("/products/{id}")
	public Product getProduct(@PathVariable("id") UUID id) {
		Product product = catalogService.getProductDetails(id);
		if(product == null) {
			throw new ProductNotFoundException("Product with ProductID "+id+" doesn't exists");
		}
		return product;
	}
	
	
	@RequestMapping("/products")
	public List<Product> getAllProducts(@RequestParam(value = "id", required = false) String productIds,
										@RequestParam(value = "title", required = false) final String title,
										@RequestParam(value = "description", required = false) final String desc){
		List<UUID> formattedProdIds = new ArrayList<UUID>();
		if((title != null && !title.isEmpty()) || (desc != null && !desc.isEmpty())) {
			return catalogService.searchProductsByTitleDesc(title,desc);
		} else if(productIds != null && !productIds.isEmpty()) {
			formattedProdIds = formatStringtoUUID(productIds);
		} 
		return catalogService.getAllProducts(formattedProdIds);
		
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
	
	@RequestMapping("/products/pagination")
	public ProductResponse getPaginatedProducts(HttpServletRequest request,
										@RequestParam(value = "page", required = false) Integer page,
										@RequestParam(value = "size", required = false) Integer size, 
										@RequestParam(value = "orderBy", required = false) String orderBy,
										@RequestParam(value = "direction", required = false) String direction){
		if(page == null) {
			page = defaultPage;
		} else if(size == null){
			logger.info("defaultSize :: "+defaultSize);
			size = defaultSize;
		}
		Page<Product> pageFormatProds = catalogService.getPaginatedProducts(page, size, orderBy, direction);
		List<Link> links = formHateoas(request, pageFormatProds.getTotalPages(), page);
		return new ProductResponse(pageFormatProds, links);
		
	}
	
	private List<Link> formHateoas(HttpServletRequest request, int totPages, int curPage) {
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(request.getRequestURL().toString()+"?"+request.getQueryString(), "CUR", request.getMethod()));
		links.add(generatePrevNxtLinks(request, "NEXT", totPages));
		links.add(generatePrevNxtLinks(request, "PREV", totPages));
		return links;
	}


	
	private Link generatePrevNxtLinks(HttpServletRequest request, String linkTyp, int totPages) {
		Map<String, String[]> queryParams = request.getParameterMap();
		int curPageNo = Integer.parseInt(queryParams.get("page")[0]);
		if((linkTyp == "NEXT" && curPageNo+1 == totPages) || (linkTyp == "PREV" && curPageNo == 0)) {
			return new Link(request.getRequestURL().toString()+"?"+request.getQueryString(), linkTyp, request.getMethod());
		} else {
			StringBuffer nextURI = request.getRequestURL();
			nextURI.append("?");
			for(Map.Entry<String, String[]> entry : queryParams.entrySet()) {
				nextURI.append(entry.getKey());
				nextURI.append("=");
				if(entry.getValue().length == 1) {
					if(entry.getKey() == "page" && linkTyp == "NEXT") {
						nextURI.append(curPageNo+1);
					} else if(entry.getKey() == "page" && linkTyp == "PREV"){
						nextURI.append(curPageNo-1);
					} else if(entry.getKey() == "page") {
						nextURI.append(entry.getValue()[0]);
					}
				} else {
					for(int i=0; i<=entry.getValue().length-1; i++) {
						nextURI.append(entry.getValue()[i]);
						if(i != entry.getValue().length-1) {
							nextURI.append(",");
						}
					}
				}
				nextURI.append("&");
			}
			return new Link(nextURI.toString(),"NEXT", request.getMethod());
		}
	}

	private List<UUID> formatStringtoUUID(String productIds) {
		final List<UUID> formattedProdIds = new ArrayList<UUID>();
		// TODO Auto-generated method stub
		final String[] prodIds = productIds.split(",");
		for(String prodId : prodIds) {
			formattedProdIds.add(UUID.fromString(prodId));
		}
		return formattedProdIds;
	}
	 
}
