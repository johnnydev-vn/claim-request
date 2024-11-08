package com.claimrequest.repositoty;

import com.claimrequest.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findAll();

//    @Query(value = "SELECT * FROM project WHERE project_name = ?1", nativeQuery = true)
    Project findByProjectName(String projectName);

    Page<Project> findAll(Pageable pageable);
}
