package com.simme.lektion_5_java_ee.controllers;

import com.simme.lektion_5_java_ee.config.AppPasswordConfig;
import com.simme.lektion_5_java_ee.models.ToDo;
import com.simme.lektion_5_java_ee.models.user.Roles;
import com.simme.lektion_5_java_ee.models.user.UserEntity;
import com.simme.lektion_5_java_ee.models.user.UserRepository;


import com.simme.lektion_5_java_ee.repositories.ToDoRepository;
import com.simme.lektion_5_java_ee.services.ToDoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private ToDoService toDoService;

    private final UserRepository userRepository;

    private final ToDoRepository toDoRepository;
    private final AppPasswordConfig appPasswordConfig; // Bcrypt



    @Autowired
    public UserController(UserRepository userRepository, ToDoRepository toDoRepository, AppPasswordConfig appPasswordConfig) {
        this.userRepository = userRepository;
        this.toDoRepository = toDoRepository;
        this.appPasswordConfig = appPasswordConfig;
    }

    @GetMapping("/register")
    public String registerUserPage(UserEntity userEntity, Model model) {

        model.addAttribute("roles", Roles.values());

        return "register";
    }



    @PostMapping("/register")
    public String registerUser(
            @Valid UserEntity userEntity,   // Enables Error Messages
            BindingResult result,           // Ties the object with result
            Roles roles
    ) {

        // Check FOR @Valid Errors
        if (result.hasErrors()) {
            return "register";
        }

        // TODO - Optionally: handle duplicate entries (@Unique PREFERABLY within ENTITY)
        userEntity.setPassword(
                appPasswordConfig.bCryptPasswordEncoder().encode(userEntity.getPassword())
        );

        userEntity.setAccountEnabled(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setCredentialsNonExpired(true);

        userRepository.save(userEntity);

        return "redirect:/login";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model) {
        List<ToDo> toDO = new ArrayList<>(); // Initialisera med en tom lista

        model.addAttribute("toDO", toDO);
        return "admin-page";
    }

    @PostMapping("/todo/post")
    public String postToDoMany(
            @Valid ToDo toDo,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {

            return "admin-page";
        }


        toDoRepository.save(toDo);


        return "redirect:/admin-page";
    }


    @GetMapping("/gadgets")
    public String gadgetsPage() {
        return "gadgets"; // Returnerar vyn gadgets.html
    }

    @GetMapping("/todo")
    public String todoPage(Model model) {
        List<ToDo> toDos = toDoService.getToDos();
        model.addAttribute("todos", toDos);
        return "todo";
    }



}