package com.gian.todos.tasks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gian.todos.utils.HandleProps;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/tasks")
public class TasksController {
  @Autowired
  private ITasksRepository repository;

  @GetMapping("/")
  public List<TaskModel> findAll(HttpServletRequest request) {
    return this.repository.findAllByUserId((UUID) request.getAttribute("userId"));
  }

  @PostMapping("/")
  public ResponseEntity create(@Valid @RequestBody TaskModel taskModel, HttpServletRequest request) {
    taskModel.setUserId((UUID) request.getAttribute("userId"));

    var currentDate = LocalDateTime.now();

    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("As datas informadas devem ser maior do que a data atual!");
    }

    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de início deve ser menor do que a data de término!");
    }

    var createdTask = this.repository.save(taskModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
  }

  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
    var task = this.repository.findById(id).orElse(null);

    if (task == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Tarefa não encontrada!");
    }

    if (!task.getUserId().equals((UUID) request.getAttribute("userId"))) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Você não tem permissão para alterar essa tarefa!");
    }

    HandleProps.copyNonNullProps(taskModel, task);

    var updatedTask = this.repository.save(task);

    return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
  }
}
