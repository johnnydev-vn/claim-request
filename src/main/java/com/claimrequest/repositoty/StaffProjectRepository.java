package com.claimrequest.repositoty;

import com.claimrequest.entities.StaffProject;
import com.claimrequest.entities.StaffProjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffProjectRepository extends JpaRepository<StaffProject, StaffProjectId> {

    List<StaffProject> findAll();
    @Query(value = "SELECT * FROM staff_project WHERE project_id = ?1", nativeQuery = true)
    List<StaffProject> findStaffProjectByProjectId(String projectId);

    List<StaffProject> findByStaff_StaffId(String staffId);
    List<StaffProject> findByProject_ProjectId(String projectId);
    StaffProject findByProject_ProjectIdAndStaff_StaffId(String projectId, String staffId);
}
