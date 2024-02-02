
package com.simme.lektion_5_java_ee.controllers;

import com.simme.lektion_5_java_ee.config.AppPasswordConfig;
import com.simme.lektion_5_java_ee.models.ToDo;
import com.simme.lektion_5_java_ee.models.user.Roles;
import com.simme.lektion_5_java_ee.models.user.UserEntity;
import com.simme.lektion_5_java_ee.models.user.UserRepository;

import com.simme.lektion_5_java_ee.repositories.ToDoRepository;
import com.simme.lektion_5_java_ee.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final AppPasswordConfig appPasswordConfig;
    private final UserRepository userRepository;

    private final ToDoRepository toDoRepository;

    @Autowired
    private ToDoService toDoService;

    @Autowired
    public UserRestController(AppPasswordConfig appPasswordConfig, UserRepository userRepository, ToDoRepository toDoRepository) {
        this.appPasswordConfig = appPasswordConfig;
        this.userRepository = userRepository;
        this.toDoRepository = toDoRepository;
    }

    @GetMapping("/myPerms")
    public ResponseEntity<String> viewPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the principal (authenticated user)
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            // If the principal is a UserDetails object (which it should be), you can cast it
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            String authorities = userDetails.getAuthorities().toString();

            // Now you have the username of the currently logged-in user
            System.out.println("Currently logged-in user: " + username + authorities);
        } else {
            // Handle the case where the principal is not a UserDetails object
            System.out.println("Unable to determine the currently logged-in user");
        }

        // Your method logic...

        return new ResponseEntity<>("Check log", HttpStatus.ACCEPTED);
    }

    @PostMapping("/user")
    public ResponseEntity<UserEntity> createNewUser(@RequestBody UserEntity newUser) {

        UserEntity userEntity = new UserEntity(
                newUser.getUsername(),
                appPasswordConfig.bCryptPasswordEncoder().encode(newUser.getPassword()),
                Roles.ADMIN,
                newUser.isAccountNonExpired(),
                newUser.isAccountNonLocked(),
                newUser.isEnabled(),
                newUser.isCredentialsNonExpired()
        );

        return new ResponseEntity<>(userRepository.save(userEntity), HttpStatus.CREATED);
    }

    @PostMapping("/todo")
    public ResponseEntity<ToDo> createNewToDo(@RequestBody ToDo newToDo) {

        ToDo toDo = new ToDo(

                newToDo.getContent(),
                newToDo.getSubject()

        );

        return new ResponseEntity<>(toDoRepository.save(toDo), HttpStatus.CREATED);
    }


    @GetMapping("/hash")
    public ResponseEntity<String> testBcryptHash(
            @RequestParam(defaultValue = "", required = false) String continueParam,
            @RequestParam(defaultValue = "", required = false) String inputPassword
    ) {

        return new ResponseEntity<>(
                appPasswordConfig.bCryptPasswordEncoder().encode(inputPassword),
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/helloAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> sayHelloToAdmin() {

        return new ResponseEntity<>("Hello ADMIN!", HttpStatus.ACCEPTED);
    }

    @GetMapping("/helloUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> sayHelloToUser() {

        return new ResponseEntity<>("Hello USER!", HttpStatus.ACCEPTED);
    }

    @GetMapping("/sayGet")
    @PreAuthorize("hasAuthority('GET')")
    public ResponseEntity<String> checkGetAuthority() {

        return new ResponseEntity<>("You can only enter with GET Authority!", HttpStatus.ACCEPTED);
    }


    @PostMapping("/todo/post/many")
    public ResponseEntity<List<ToDo>> createToDo(@RequestBody List<ToDo> toDos) throws Exception {
        List<ToDo> newTodos = toDoService.addAll(toDos);
        return ResponseEntity.ok(newTodos);

    }

    @GetMapping("/todo")
    ResponseEntity<List<ToDo>> getAll() {
        return new ResponseEntity<>(toDoService.getToDos(), HttpStatus.OK);

    }

}