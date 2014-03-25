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