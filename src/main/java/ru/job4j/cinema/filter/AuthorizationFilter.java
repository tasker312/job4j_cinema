package ru.job4j.cinema.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@WebFilter
@Component
@Order(1)
public class AuthorizationFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var uri = request.getRequestURI();
        if (isPageAllowed(uri)) {
            chain.doFilter(request, response);
            return;
        }
        var user = request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isPageAllowed(String uri) {
        return uri.equals("/")
                || uri.startsWith("/css")
                || uri.startsWith("/js")
                || uri.startsWith("/img")
                || uri.startsWith("/files")
                || uri.startsWith("/users")
                || uri.startsWith("/films")
                || uri.equals("/sessions");
    }

}
