/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.infrastructure.persistence;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.CategoryRepository;
import java.util.List;

/**
 *
 * @author tarasev
 */
public class CategoryRepositoryHibernate extends HibernateRepository implements CategoryRepository {

    public Category find(long categoryId) {
        return (Category) getSession().createQuery("from Category where id = :id").setParameter("id", categoryId).uniqueResult();
    }

    public void store(Category category) {
        getSession().saveOrUpdate(category);
    }
    
    public Category getRootCategory()
    {
        return (Category) getSession().createQuery("from Category where parentId is null").uniqueResult();
    }
    
    public void delete(Category category){
        getSession().delete(category);
    }
}
