package com.simme.lektion_5_java_ee.models.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service    // TODO - Is this optional?
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



    public void updateUser(String username){
        UserEntity user = userRepository.findByUsername(username);

        if(user != null) {
            userRepository.updateUsers(username);
        }else {
            throw  new UsernameNotFoundException("User: " + username + " not found");
        }
    }


    public List<UserEntity> getUserNames(){
        return userRepository.findAll();
    }

}
