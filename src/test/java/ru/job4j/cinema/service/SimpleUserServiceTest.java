package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleUserServiceTest {

    private UserService userService;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new SimpleUserService(userRepository);
    }

    @Test
    public void whenSaveThenReturnUser() {
        var user = new User(1, "ivan", "ivan@test", "pass");
        when(userRepository.save(user)).thenReturn(Optional.of(user));

        var savedUser = userService.save(user);

        assertThat(savedUser).isPresent().get()
                .isEqualTo(user);
    }

    @Test
    public void whenSaveWithSameEmailThenReturnOptionalEmpty() {
        when(userRepository.save(any(User.class))).thenReturn(Optional.empty());

        var result = userService.save(new User());

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void whenFindByEmailAndPasswordThenReturnUser() {
        var user = new User(1, "ivan", "ivan@test", "pass");
        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));

        var foundUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(foundUser).isPresent().get()
                .isEqualTo(user);
    }

    @Test
    public void whenFindByNonExistentEmailAndPasswordThenReturnOptionalEmpty() {
        when(userRepository.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        var foundUser = userService.findByEmailAndPassword("", "");

        assertThat(foundUser).isEmpty();
    }

    @Test
    public void whenFindByNullsThenReturnOptionalEmpty() {
        when(userRepository.findByEmailAndPassword(null, null)).thenReturn(Optional.empty());

        var foundUser = userService.findByEmailAndPassword(null, null);

        assertThat(foundUser).isEmpty();
    }

}
