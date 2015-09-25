/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

import com.sg.domain.ar.Account;
import com.sg.domain.exceptions.EmailInvalidException;
import com.sg.domain.exceptions.EmailIsNotUniqueException;
import com.sg.domain.exceptions.PasswordInvalidException;
import com.sg.domain.repositories.AccountRepository;
import com.sg.domain.specs.EmailIsUniqueSpecification;
import com.sg.domain.specs.EmailIsValidSpecification;
import com.sg.domain.specs.PasswordIsValidSpecification;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.PasswordHash;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class AccountRegistrationService {

    private final EmailIsValidSpecification emailIsValidSpecification;
    private final PasswordIsValidSpecification passwordIsValidSpecification;
    private final EmailIsUniqueSpecification emailIsUniqueSpecification;
    private final PasswordHasher passwordHasher;
    private final AccountRepository accountRepository;

    public AccountRegistrationService(
            EmailIsValidSpecification emailIsValidSpecification,
            PasswordIsValidSpecification passwordIsValidSpecification,
            EmailIsUniqueSpecification emailIsUniqueSpecification,
            PasswordHasher passwordHasher,
            AccountRepository accountRepository) {
        this.emailIsValidSpecification = emailIsValidSpecification;
        this.passwordIsValidSpecification = passwordIsValidSpecification;
        this.emailIsUniqueSpecification = emailIsUniqueSpecification;
        this.passwordHasher = passwordHasher;
        this.accountRepository = accountRepository;
    }

    public void registerEmailAccount(String sEmail,
                                     String password) throws EmailIsNotUniqueException, EmailInvalidException, PasswordInvalidException {
        Email email = new Email(sEmail);
        if (!emailIsValidSpecification.isSatisfiedBy(email))
        {
            throw new EmailInvalidException();
        }
        if (!passwordIsValidSpecification.isSatisfiedBy(password))
        {
            throw new PasswordInvalidException();
        }
        if (!emailIsUniqueSpecification.isSatisfiedBy(email)) {
            throw new EmailIsNotUniqueException();
        }
        PasswordHash hash = passwordHasher.getHash(password);
        Account account = Account.registerEmailAccount(email, hash);
        accountRepository.create(account);
    }
}
