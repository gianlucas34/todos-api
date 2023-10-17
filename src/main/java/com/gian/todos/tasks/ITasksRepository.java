package com.gian.todos.tasks;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITasksRepository extends JpaRepository<TaskModel, UUID> {
  List<TaskModel> findAllByUserId(UUID userId);
}
