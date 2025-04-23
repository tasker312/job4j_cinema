package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestRegisterThenRegisterPage() {

        var view = userController.getRegisterPage();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUserThenRedirectToLoginPage() {
        when(userService.save(any(User.class))).thenReturn(Optional.of(new User()));
        var request = new MockHttpServletRequest();

        var model = new ConcurrentModel();
        var view = userController.register(new User(), model, request);

        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenRegisterUserWithSameEmailThenShowError() {
        var user = new User(1, "ivan", "ivan@test", "pass");
        when(userService.save(user)).thenReturn(Optional.empty());
        var request = new MockHttpServletRequest();

        var model = new ConcurrentModel();
        var view = userController.register(user, model, request);
        var actualMessage = (String) model.getAttribute("error");

        assertThat(view).isEqualTo("users/register");
        assertThat(actualMessage)
                .contains("User")
                .contains("already exists")
                .contains(user.getEmail());
    }

    @Test
    public void whenRequestLoginThenReturnLoginPage() {

        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginThenAuthenticateUserAndRedirectToVacanciesPage() {
        var user = new User(1, "ivan", "ivan@test", "pass");
        var emailCaptor = ArgumentCaptor.forClass(String.class);
        var passwordCaptor = ArgumentCaptor.forClass(String.class);
        when(userService.findByEmailAndPassword(emailCaptor.capture(), passwordCaptor.capture()))
                .thenReturn(Optional.of(user));
        var request = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        var model = new ConcurrentModel();
        var view = userController.login(user, model, request);

        assertThat(view).isEqualTo("redirect:/sessions");
        assertThat(emailCaptor.getValue()).isEqualTo(user.getEmail());
        assertThat(passwordCaptor.getValue()).isEqualTo(user.getPassword());
        verify(session).setAttribute("user", user);
    }

    @Test
    public void whenLoginFailedThenShowError() {
        var user = new User(1, "ivan", "ivan@test", "pass");
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());
        var request = new MockHttpServletRequest();

        var model = new ConcurrentModel();
        var view = userController.login(user, model, request);
        var actualMessage = (String) model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualMessage).contains("Invalid email or password");
    }

    @Test
    public void whenLogoutThenInvalidateSession() {
        var session = mock(HttpSession.class);

        String result = userController.logout(session);

        assertThat(result).isEqualTo("redirect:/");
        verify(session).invalidate();
    }

}
