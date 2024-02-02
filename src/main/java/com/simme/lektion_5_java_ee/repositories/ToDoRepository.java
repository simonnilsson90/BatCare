package com.simme.lektion_5_java_ee.repositories;

import com.simme.lektion_5_java_ee.models.ToDo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ToDoRepository extends CrudRepository<ToDo, Long> {

    @Override
    List<ToDo> findAll();

    Optional<ToDo> findByContent(String content);



}
