package com.vti.service;

import com.vti.entity.Account;
import com.vti.form.AccountCreateForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdateForm;
import com.vti.repository.IAccountRepository;
import com.vti.specification.AccountSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private IAccountRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<Account> findAll(Pageable pageable, AccountFilterForm form){
        Specification<Account> spec = AccountSpecification.buildWhere(form);
        return repository.findAll(spec, pageable);
    }

    @Override
    public Account findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    @Override
    public void create(AccountCreateForm form) {
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        Account account = mapper.map(form, Account.class);
        account.setPassword(encodedPassword);
        repository.save(account);
    }

    @Override
    public void update(AccountUpdateForm form) {
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        Account account = mapper.map(form, Account.class);
        account.setPassword(encodedPassword);
        repository.save(account);
    }

    @Override
    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(
                account.getUsername(),
                account.getPassword(),
                AuthorityUtils.createAuthorityList(account.getRole().toString()));
    }

    @Override
    public void deleteAll(List<Integer> ids){
        repository.deleteAllById(ids);
    }

    @Override
    public Account findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
