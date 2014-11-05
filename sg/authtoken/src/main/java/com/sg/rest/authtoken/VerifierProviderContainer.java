/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.authtoken;

import com.google.common.collect.Lists;
import java.util.List;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;

/**
 *
 * @author tarasev
 */
public class VerifierProviderContainer implements VerifierProvider {

    private final Verifier verifier;

    public VerifierProviderContainer(Verifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public List<Verifier> findVerifier(String issuer, String keyId) {
        return Lists.newArrayList(verifier);
    }
}
