package com.claimrequest.service;

import com.claimrequest.entities.StaffProject;
import com.claimrequest.repositoty.StaffProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffProjectService {

    @Autowired
    private StaffProjectRepository staffProjectRepository;

    public List<StaffProject> getAll() {
        return staffProjectRepository.findAll();
    }

    public List<StaffProject> findStaffProjectByProjectId(String projectId){
        return staffProjectRepository.findStaffProjectByProjectId(projectId);
    }

    public List<StaffProject> getProjectsForStaff(String staffId) {
        return staffProjectRepository.findByStaff_StaffId(staffId);
    }

    public List<StaffProject> getStaffsForProject(String projectId) {
        return staffProjectRepository.findByProject_ProjectId(projectId);
    }

    public StaffProject getStaffProjectById(String staffId, String projectId) {
        return staffProjectRepository.findByProject_ProjectIdAndStaff_StaffId(projectId, staffId);
    }
}
