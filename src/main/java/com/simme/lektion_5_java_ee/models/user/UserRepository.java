package com.simme.lektion_5_java_ee.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    UserEntity findByUsername(String username);


    UserEntity findById(long id);

    @Override
    List<UserEntity> findAll();




 void deleteByUsername(String username);

 /*
 void updateUser(long id);



  */

}






