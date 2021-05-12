package com.epam.esm.security.service;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Permission;
import com.epam.esm.persistence.entity.Role;
import com.epam.esm.persistence.repository.AccountRepository;
import com.epam.esm.persistence.specification.account.FindByEmailSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Specification<Account> specification = new FindByEmailSpecification(email);
        Account account = accountRepository.findOne(specification)
                .orElseThrow(() -> new UsernameNotFoundException("Account does not exists! Email: " + email));

        String verifiedEmail = account.getEmail();
        String password = account.getPassword();
        Role role = account.getRole();
        Boolean blocked = account.getBlocked();
        Set<GrantedAuthority> grantedAuthorities = this.retrieveGrantedAuthoritySetFromRole(role);

        return new User(verifiedEmail, password, !blocked, !blocked, !blocked, !blocked, grantedAuthorities);
    }

    private Set<GrantedAuthority> retrieveGrantedAuthoritySetFromRole(Role role) {
        Set<Permission> permissions = role.getPermissions();
        Set<GrantedAuthority> authorities = permissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
        GrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_" + role.name());
        authorities.add(roleAuthority);
        return authorities;
    }

}
