package com.gian.todos.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/users")
public class UsersController {
  @Autowired
  private IUsersRepository repository;

  @PostMapping("/")
  public ResponseEntity create(@Valid @RequestBody UserModel userModel) {
    var user = this.repository.findByUsername(userModel.getUsername());

    if (user != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário informado já existe!");
    }

    var hashedPassword = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

    userModel.setPassword(hashedPassword);

    var createdUser = this.repository.save(userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }
}
