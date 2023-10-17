package com.gian.todos.tasks;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @NotNull(message = "Informe o título!")
  @NotBlank(message = "Título não pode ficar em branco!")
  @Column(length = 50)
  private String title;

  @NotNull(message = "Informe a descrição!")
  @NotBlank(message = "Descrição não pode ficar em branco!")
  private String description;

  @NotNull(message = "Informe a data de início!")
  private LocalDateTime startAt;

  @NotNull(message = "Informe a data de término!")
  private LocalDateTime endAt;

  @NotNull(message = "Infome a prioridade!")
  @NotBlank(message = "Prioridade não pode ficar em branco!")
  private String priority;

  private UUID userId;

  @CreationTimestamp
  private LocalDateTime createdAt;

  public void setTitle(String title) throws Exception {
    if (title.length() > 50) {
      throw new Exception("O título deve conter no máximo 50 caracteres!");
    }

    this.title = title;
  }
}
