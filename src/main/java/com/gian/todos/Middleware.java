package com.gian.todos;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.gian.todos.users.IUsersRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Middleware extends OncePerRequestFilter {
  @Autowired
  private IUsersRepository repository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var servletMethod = request.getMethod();
    var servletPath = request.getServletPath();

    if (servletPath.equals("/users/") && servletMethod.equals("POST")) {
      filterChain.doFilter(request, response);
    } else {
      var authorization = request.getHeader("Authorization");
      var encodedAuth = authorization.substring("Basic".length()).trim();
      byte[] decodedAuth = Base64.getDecoder().decode(encodedAuth);
      var authString = new String(decodedAuth);
      String[] credentials = authString.split(":");

      if (Array.getLength(credentials) == 0) {
        response.sendError(401);
      } else {
        var username = credentials[0];
        var password = credentials[1];

        var user = this.repository.findByUsername(username);

        if (user == null) {
          response.sendError(401);
        } else {
          var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

          if (passwordVerify.verified) {
            request.setAttribute("userId", user.getId());

            filterChain.doFilter(request, response);
          } else {
            response.sendError(401);
          }
        }
      }
    }
  }
}
