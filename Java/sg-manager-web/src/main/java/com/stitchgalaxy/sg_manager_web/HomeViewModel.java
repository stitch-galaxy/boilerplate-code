/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.util.CollectionsX;


/**
 *
 * @author Administrator
 */
public class HomeViewModel {

    Product selected;
    List<Product> products = new ArrayList<Product>();
}



 
 
 
public class ContributorViewModel {
    private Contributor selected;
    private List<String> titles = new ArrayList<String>(new ContributorData().getTitles());
    private List<Contributor> contributors = new ArrayList<Contributor>(new ContributorData().getContributors());
 
    @Init
    public void init() {    // Initialize
        selected = contributors.get(0); // Selected First One
    }
 
    public List<String> getContributorTitles() {
        return titles;
    }
 
    public List<Contributor> getContributorList() {
        return contributors;
    }
 
    public void setSelectedContributor(Contributor selected) {
        this.selected = selected;
    }
 
    public Contributor getSelectedContributor() {
        return selected;
    }
}