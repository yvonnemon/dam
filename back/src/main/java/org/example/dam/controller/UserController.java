package org.example.dam.controller;

import org.example.dam.dto.UserDTO;
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
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));


    }


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.findById(id) != null) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO login
    //TODO users/bookings
}
