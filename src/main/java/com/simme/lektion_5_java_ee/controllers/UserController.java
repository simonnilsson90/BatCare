package com.simme.lektion_5_java_ee.controllers;

import com.simme.lektion_5_java_ee.config.AppPasswordConfig;
import com.simme.lektion_5_java_ee.models.ToDo;
import com.simme.lektion_5_java_ee.models.user.Roles;
import com.simme.lektion_5_java_ee.models.user.UserEntity;
import com.simme.lektion_5_java_ee.models.user.UserEntityDetailsService;
import com.simme.lektion_5_java_ee.models.user.UserRepository;


import com.simme.lektion_5_java_ee.repositories.ToDoRepository;
import com.simme.lektion_5_java_ee.services.ToDoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private ToDoService toDoService;

    @Autowired
    private UserEntityDetailsService userEntityDetailsService;

    private final UserRepository userRepository;

    private final ToDoRepository toDoRepository;
    private final AppPasswordConfig appPasswordConfig; // Bcrypt


    @Autowired
    public UserController(ToDoService toDoService, UserRepository userRepository, UserEntityDetailsService userEntityDetailsService, ToDoRepository toDoRepository, AppPasswordConfig appPasswordConfig) {
        this.toDoService = toDoService;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication != null && authentication.isAuthenticated()) {

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();

                UserEntity user = userEntityDetailsService.getUserByUsername(username);
                model.addAttribute("currentUser", user);
            }
        }

        List<ToDo> toDos = toDoService.getToDos();
        List<UserEntity> users = userEntityDetailsService.getUserNames();
        model.addAttribute("todos", toDos);
        model.addAttribute("users", users);

        return "admin-page";
    }


    @PostMapping("/update")
    public String updateUser(
            @RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model)
    {

        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {

            model.addAttribute("errorMessage", "User not found");
            return "error-page";
        }


        UserEntity user = optionalUser.get();
        user.setUsername(username);

        user.setPassword(
                appPasswordConfig.bCryptPasswordEncoder().encode(user.getPassword())
        );

        try {

            userRepository.save(user);

            model.addAttribute("successMessage", "User updated successfully");
        } catch (Exception e) {
            // Om det uppstår ett fel, visa felmeddelande
            model.addAttribute("errorMessage", "Failed to update user");
            return "error-page";
        }

        return "redirect:admin-page";
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

    @PostMapping("/id/delete")
    public String deleteMany(@RequestParam("ids") List<Long> ids, Model model) {

        try {
            for(Long id : ids) {
                userEntityDetailsService.deleteById(id);
            }
            List<UserEntity> users = userEntityDetailsService.getUserNames();
            model.addAttribute("users", users);
            model.addAttribute("successMessage", "User deleted successfully");



        } catch (UsernameNotFoundException e) {

            model.addAttribute("errorMessage", "User with the provided ID not found");
        }
        System.out.println("ids: " + ids);
    return "admin-page";
    }



    @GetMapping("/about")
    public String aboutPage() {
        return "about";

    }

    @GetMapping("/gadgets")
    public String gadgetsPage() {
        return "gadgets";

    }

    @GetMapping("/todo")
    public String todoPage (Model model){
        List<ToDo> toDos = toDoService.getToDos();
        model.addAttribute("todos", toDos);
        return "todo";
    }
    @GetMapping("/")
    public String getUsername(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserEntity) {
                UserEntity userEntity = (UserEntity) principal;
                Long userId = userEntity.getId();
                Optional<UserEntity> currentUserOptional = userRepository.findById(userId);
                currentUserOptional.ifPresent(currentUser -> model.addAttribute("currentUser", currentUser));
            }
        }
        return "home";
    }




    @GetMapping("/users")
    public String getUsers (Model model){
        List<UserEntity> users = userEntityDetailsService.getUserNames();
        List<Long> userIds = userEntityDetailsService.getUserIds();
        List<String> userpasswords = userEntityDetailsService.getUserPasswords();
        model.addAttribute("users", users);
        return "users";
    }
}