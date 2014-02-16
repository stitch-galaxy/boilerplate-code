/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.infrastructure.persistence;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.CategoryRepository;

/**
 *
 * @author tarasev
 */
public class CategoryRepositoryHibernate extends HibernateRepository implements CategoryRepository {

    public Category find(long categoryId) {
        return (Category) getSession().createQuery("from category where id = :id").setParameter("id", categoryId).uniqueResult();
    }

    public void store(Category category) {
        getSession().saveOrUpdate(category);
    }
}
