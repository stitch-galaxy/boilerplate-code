/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.vo;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;

/**
 *
 * @author Андрей
 */
public class MalformedURLExceptionTest {

    @Test(expected = MalformedURLException.class)
    public void test() throws MalformedURLException {
        new URL(null);
    }
}
