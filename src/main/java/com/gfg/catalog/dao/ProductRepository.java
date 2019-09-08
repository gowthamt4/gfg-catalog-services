package com.gfg.catalog.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ProductRepository
    extends
      CrudRepository<Product, UUID>,
      PagingAndSortingRepository<Product, UUID> {

  @Query("from Product P where P.title LIKE CONCAT('%',:title,'%') and P.description LIKE CONCAT('%',:desc,'%')")
  public Page<Product> findProdByTitleDesc(@Param("title") final String title,
      @Param("desc") final String desc, Pageable pageable);

  @Query("from Product P where P.description LIKE CONCAT('%',:desc,'%')")
  public Page<Product> findProdByDesc(@Param("desc") final String desc, Pageable pageable);

  @Query("from Product P where P.title LIKE CONCAT('%',:title,'%')")
  public Page<Product> findProdByTitle(@Param("title") final String title, Pageable pageable);

  @Query("from Product P where P.id in (:id)")
  public Page<Product> findAllByIds(@Param("id") final List<UUID> id, Pageable pageable);


}
