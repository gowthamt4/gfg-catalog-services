package com.gfg.catalog.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gfg.catalog.entities.Product;

@Repository
public class ProductDAOImpl implements ProductDAO{
	
	private static final String FIND_PRODUCTS_TITLE_DESC = 
			"select P from Product P "
                    +" where P.title like :title";
	
	@PersistenceContext
    private EntityManager entityManager;
	
	
	@Override
    public List<Product> findProdByTitleDesc(final String title, final String desc) {
        final Query query = entityManager.createQuery(FIND_PRODUCTS_TITLE_DESC);
        query.setParameter("title", "%"+title+"%");
        return query.getResultList();
    }
}
