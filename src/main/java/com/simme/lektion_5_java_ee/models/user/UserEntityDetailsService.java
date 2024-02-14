package com.simme.lektion_5_java_ee.models.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserEntityDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserEntityDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username);
    }

    public void deleteByUsername(String username){
        UserEntity user = userRepository.findByUsername(username);

        if(user != null) {
            userRepository.delete(user);
        }else {
            throw  new UsernameNotFoundException("User: " + username + " not found");
        }
    }

/*
    public void updateById(long id) {
        UserEntity user = userRepository.findById(id);
        if(user!= null) {
            userRepository.updateUser(id);
        }else {
            throw new EmptyResultDataAccessException( 1);
        }

    }


 */

    public void deleteById(long id){
        UserEntity user = userRepository.findById(id);

        if(user != null) {
            userRepository.delete(user);
        }else {
            throw new EmptyResultDataAccessException("User with id: " + id + " not found", 1);
        }
    }



    public List<String> getUsernames() {
        List<UserEntity> users = userRepository.findAll();
        List<String> usernames = new ArrayList<>();
        for (UserEntity user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }








    public List<Long> getUserIds() {
        List<UserEntity> users = userRepository.findAll();
        List<Long> userIds = new ArrayList<>();
        for (UserEntity user : users) {
            userIds.add(user.getId());
        }
        return userIds;
    }

    public List<String> getUserPasswords() {
        List<UserEntity> users = userRepository.findAll();
        List<String> userPasswords = new ArrayList<>();
        for (UserEntity user : users) {
            userPasswords.add(user.getPassword());
        }
        return userPasswords;
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity getUserById(long id){return userRepository.findById(id);}



    public List<UserEntity> getUserNames(){
        return userRepository.findAll();
    }


}
