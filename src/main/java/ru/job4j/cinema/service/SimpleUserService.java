package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {
        if (user == null
                || user.getFullName() == null
                || user.getEmail() == null
                || user.getPassword() == null) {
            return Optional.empty();
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }
        return userRepository.findByEmailAndPassword(email, password);
    }

}
