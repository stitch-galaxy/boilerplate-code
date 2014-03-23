package com.stitchgalaxy.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductLocalization;
import com.stitchgalaxy.infrastructure.persistence.CategoryRepositoryHibernate;
import com.stitchgalaxy.infrastructure.persistence.ProductRepositoryHibernate;
import com.stitchgalaxy.service.DomainDataServiceException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import org.joda.time.LocalDate;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/test-spring-context-persistence.xml")
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
public class ProductCategoriesDaoTests extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected ProductRepositoryHibernate productRepository;

    public ProductCategoriesDaoTests() {
    }

    @Test
    public void test() throws SQLException, Exception {
        Long productId = createProduct();
        createLocalization(productId);
        updateLocalization(productId);
        Product product = productRepository.find(productId);
        assertEquals(product.getLocalizations().size(), 1);
        for (ProductLocalization l : product.getLocalizations()) {
            assertEquals(l.getName(), "test");
        }
    }

    @Transactional
    public void createLocalization(Long productId) {
        Product product = productRepository.find(productId);
        ProductLocalization localization = new ProductLocalization();
        localization.setLocale("test");
        product.getLocalizations().clear();
        product.getLocalizations().add(localization);
        productRepository.store(product);
    }

    @Transactional
    public void updateLocalization(Long productId) {
        Product product = productRepository.find(productId);
        ProductLocalization localization = new ProductLocalization();
        localization.setLocale("test");
        localization.setName("test");
        localization.setDescription("test");
        localization.setTags("test");
        product.getLocalizations().clear();
        product.getLocalizations().add(localization);
        productRepository.store(product);
    }

    @Transactional
    public Long createProduct() {
        Product product = new Product();
        product.setName("test");
        product.setPriceUsd(new BigDecimal(BigInteger.ONE));
        product.setDate(LocalDate.now());
        productRepository.store(product);
        return product.getId();
    }
}
