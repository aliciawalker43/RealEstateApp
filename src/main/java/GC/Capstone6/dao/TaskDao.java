package GC.Capstone6.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import GC.Capstone6.Pojo.Task;

public interface TaskDao extends JpaRepository< Task, Long> {

}
