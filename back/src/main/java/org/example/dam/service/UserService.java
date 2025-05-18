package org.example.dam.service;

import org.example.dam.dto.FlightDTO;
import org.example.dam.dto.UserDTO;
import org.example.dam.model.*;
import org.example.dam.repository.UserRepository;
import org.example.dam.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        List<UserDTO> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(entityToDto(user));
        }

        return users;

    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return entityToDto(user);
    }

    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::entityToDto)
                .orElse(null);
    }


    public UserDTO save(UserDTO user, boolean isAdmin) {
        if(!isAdmin) {
            user.setRole(Role.USER);
        }

        return entityToDto( userRepository.save(dtoToEntity(user)));

    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    private User dtoToEntity(UserDTO dto) {
        User user = new User();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(dto.getRole());
            user.setPhone(dto.getPhone());
            user.setDni(dto.getDni());

        return user;
    }

    private UserDTO entityToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());

        dto.setRole(user.getRole());
        dto.setPhone(user.getPhone());
        dto.setDni(user.getDni());
        return dto;
    }

}
