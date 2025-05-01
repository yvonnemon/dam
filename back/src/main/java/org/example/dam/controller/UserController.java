package org.example.dam.controller;

import org.example.dam.model.User;
import org.example.dam.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

/*
*
*
*
* TODO
*  TODO
*   TODO
*    TODO
*     HAY QUE HACER UN DTO PARA USER PORQUE LA PASSWORD NO ESTA EN JSONIGNORE
*
*
*
* */
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //TODO move to /auth
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.findById(id)
                        .map(user -> {
                            user.setFirstName(newUser.getFirstName());
                            user.setLastName(newUser.getLastName());
                            user.setEmail(newUser.getEmail());
                            user.setPassword(newUser.getPassword());
                            user.setRole(newUser.getRole());
                            user.setPhone(newUser.getPhone());
                            user.setDni(newUser.getDni());
                            // TODO If dateOfBirth is a LocalDate or Date, uncomment this:
                            // user.setDateOfBirth(newUser.getDateOfBirth());
                            return userService.save(user);
                        })
                        .orElseGet(() -> {
                            newUser.setId(id); // optional: set ID manually
                            return userService.save(newUser);
                        })
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO login
    //TODO users/bookings
}
