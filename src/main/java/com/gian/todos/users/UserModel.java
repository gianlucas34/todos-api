package com.gian.todos.users;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name = "tb_users")
public class UserModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @NotNull(message = "Informe o usuário!")
  @NotBlank(message = "Usuário não pode ficar em branco!")
  @Column(unique = true)
  private String username;

  @NotNull(message = "Informe o nome!")
  @NotBlank(message = "Nome não pode ficar em branco!")
  private String name;

  @NotNull(message = "Informe a senha!")
  @NotBlank(message = "Senha não pode ficar em branco!")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
