package com.gfg.catalog.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gfg.catalog.entities.Product;

/**
 * Interface to implement the basic database interactions to manage the products data 
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, UUID>, PagingAndSortingRepository<Product, UUID> {
	
	@Query("from Product P where P.title LIKE CONCAT('%',:title,'%') and P.description LIKE CONCAT('%',:desc,'%')")
	public List<Product> findProdByTitleDesc(@Param("title") final String title, @Param("desc") final String desc);
	
	@Query("from Product P where P.description LIKE CONCAT('%',:desc,'%')")
	public List<Product> findProdByDesc(@Param("desc") final String desc);
	
	@Query("from Product P where P.title LIKE CONCAT('%',:title,'%')")
	public List<Product> findProdByTitle(@Param("title") final String title);
	
	
}
