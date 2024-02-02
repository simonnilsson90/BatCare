package com.simme.lektion_5_java_ee.services;

import com.simme.lektion_5_java_ee.models.ToDo;
import com.simme.lektion_5_java_ee.repositories.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    public List<ToDo> getToDos(){
        return toDoRepository.findAll();
    }

    public  List<ToDo> addAll(List<ToDo> toDos) {
        toDoRepository.saveAll(toDos);
        return null;
    }





}
