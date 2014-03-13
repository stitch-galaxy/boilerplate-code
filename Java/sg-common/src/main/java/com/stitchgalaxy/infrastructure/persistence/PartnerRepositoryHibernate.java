/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.infrastructure.persistence;

import com.stitchgalaxy.domain.PartnerRepository;
import com.stitchgalaxy.domain.Partner;
import java.util.List;

/**
 *
 * @author tarasev
 */
public class PartnerRepositoryHibernate extends HibernateRepository implements PartnerRepository {

    
    public List<Partner> getPartners()
    {
        List<Partner> result = getSession().createQuery("from Partner").list();
        return result;
    }
    
    
    public void store(Partner partner)
    {
        getSession().saveOrUpdate(partner);
    }
}
