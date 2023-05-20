
package com.example.demo.domain.service;

import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.repository.UserAdditionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserAdditionalRepository repository;

    public AuthService(UserAdditionalRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserAdditional GetAuthenticatedUserData()
    {
        var user = (UserAdditional) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  repository.findById(user.getId()).get();
    }

        public boolean checkIsUserAuthenticated()
        {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
        }


}

