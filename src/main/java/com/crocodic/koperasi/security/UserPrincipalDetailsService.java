package com.crocodic.koperasi.security;

import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.management.CmsUsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserPrincipalDetailsService implements UserDetailsService {

    private final CmsUsersRepository cmsUsersRepository;

    public UserPrincipalDetailsService(CmsUsersRepository cmsUsersRepository) {
        this.cmsUsersRepository = cmsUsersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        CmsUsers user = cmsUsersRepository.findByEmail(s);
        if(user==null){
            throw new UsernameNotFoundException("User doesn`t exist");
        }

        //Fetching User roles form DB.

        List<String> dbRoles=new ArrayList<String>();
        dbRoles.add( user.getCmsPrivileges().getName().toUpperCase().replace(" ","_"));

        // pass user object and roles to LoggedUser
        UserPrincipal loggedUser=new UserPrincipal(user, dbRoles);
        return loggedUser;
    }


}
