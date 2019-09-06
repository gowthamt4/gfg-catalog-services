package com.gfg.catalog.dao;

import java.util.List;

import com.gfg.catalog.entities.Product;

public interface ProductDAO {

	List<Product> findProdByTitleDesc(final String title, final String desc);

}
