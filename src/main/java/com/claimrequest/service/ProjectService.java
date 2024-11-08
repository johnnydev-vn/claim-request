package com.claimrequest.service;

import com.claimrequest.dto.ProjectDTO;
import com.claimrequest.dto.StaffProjectDTO;
import com.claimrequest.entities.Project;
import com.claimrequest.entities.Role;
import com.claimrequest.entities.StaffProject;
import com.claimrequest.entities.StaffProjectId;
import com.claimrequest.repositoty.ProjectRepository;
import com.claimrequest.repositoty.StaffProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffProjectService staffProjectService;

    @Autowired
    private StaffProjectRepository staffProjectRepository;

    public List<Project> getAll() {
        return projectRepository.findAll();
    }


    public void deleteProject(String projectId) {
        projectRepository.deleteById(projectId);
    }

    public Project getOne(String staffId) {
        Optional<Project> project = projectRepository.findById(staffId);
        if (project.isPresent()) {
            return project.get();
        }
        return null;
    }


    public void saveProject(ProjectDTO projectDTO) {

        /** Tạo đối tượng ProjectEntity từ ProjectDTO */
        Project project = new Project();
        project.setProjectId(projectDTO.getProjectId());
        project.setProjectName(projectDTO.getProjectName());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setCustomerName(projectDTO.getCustomerName());
        project.setBudget(projectDTO.getBudget());

        projectRepository.save(project);

        /** Lưu danh sách StaffProjectDTO */
        List<StaffProject> staffProjectList = new ArrayList<>();
        for (StaffProjectDTO staffProjectDTO : projectDTO.getStaffProjectListDTO()) {
            StaffProject staffProject = new StaffProject();
            staffProject.setProject(project);
            staffProject.setStaff(staffService.getOne(staffProjectDTO.getStaffId()));
            staffProject.setStartDate(staffProjectDTO.getStartDate());
            staffProject.setEndDate(staffProjectDTO.getEndDate());

            StaffProjectId id = new StaffProjectId();
            id.setProjectId(project.getProjectId());
            id.setStaffId(staffProjectDTO.getStaffId());
            staffProject.setStaffProjectId(id);

            String position = staffProjectDTO.getPosition();
            staffProject.setRole(Role.valueOf(position));

            staffProjectList.add(staffProject);
        }
        staffProjectRepository.saveAll(staffProjectList);
        System.out.println("Done!");
    }

    public void updateProject(ProjectDTO projectDTO) {

        /**Tạo đối tượng Project từ ProjectDTO */
        Project project = new Project();
        project.setProjectId(projectDTO.getProjectId());
        project.setProjectName(projectDTO.getProjectName());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setCustomerName(projectDTO.getCustomerName());
        project.setBudget(projectDTO.getBudget());

        /** Lưu thông tin Project */
        projectRepository.save(project);

        /** Lưu danh sách StaffProjectDTO */
        List<StaffProject> staffProjectList = new ArrayList<>();
        for (StaffProjectDTO staffProjectDTO : projectDTO.getStaffProjectListDTO()) {
            StaffProject staffProject = new StaffProject();
            staffProject.setProject(project);
            staffProject.setStaff(staffService.getOne(staffProjectDTO.getStaffId()));
            staffProject.setStartDate(staffProjectDTO.getStartDate());
            staffProject.setEndDate(staffProjectDTO.getEndDate());

            StaffProjectId id = new StaffProjectId();
            id.setProjectId(project.getProjectId());
            id.setStaffId(staffProjectDTO.getStaffId());
            staffProject.setStaffProjectId(id);

            String position = staffProjectDTO.getPosition();
            staffProject.setRole(Role.valueOf(position));

            staffProjectList.add(staffProject);
        }

        List<StaffProject> staffProjectListDB = staffProjectService.findStaffProjectByProjectId(projectDTO.getProjectId());
        for (StaffProject staffProjectDB : staffProjectListDB) {
            for (StaffProject staffProject : staffProjectList) {
                if (!staffProject.getStaffProjectId().getStaffId().equals(staffProjectDB.getStaffProjectId().getStaffId()))
                    staffProjectRepository.delete(staffProjectDB);
            }
        }
        /** Lưu thông tin StaffProject */
        staffProjectRepository.saveAll(staffProjectList);
        System.out.println("Done!");
    }


    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public Project findByProjectName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }
}
