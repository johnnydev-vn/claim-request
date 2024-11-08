package com.claimrequest.controller;

import com.claimrequest.dto.ProjectDTO;
import com.claimrequest.dto.StaffProjectDTO;
import com.claimrequest.entities.Project;
import com.claimrequest.entities.Role;
import com.claimrequest.entities.Staff;
import com.claimrequest.entities.StaffProject;
import com.claimrequest.service.ProjectService;
import com.claimrequest.service.StaffProjectService;
import com.claimrequest.service.StaffService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StaffProjectService staffProjectService;

    @Autowired
    private StaffService staffService;

    @GetMapping(value = "/admin/project")
    public String getAll(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            Model model
    ) {
        Page<Project> projects = null;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        projects = projectService.findAll(pageable);
        model.addAttribute("page", projects);

        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= projects.getTotalPages(); i++) {
            pageNums.add(i);
        }
        model.addAttribute("pageNums", pageNum);
        return "admin/view-project";
    }

    @GetMapping(value = "/admin/delete-project/{projectId}")
    public String delete(
            @PathVariable("projectId") String projectId

    ) {
        projectService.deleteProject(projectId);
        return "redirect:/admin/project";
    }

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping(value = "/admin/create-project")
    public String create(
            Model model
    ) throws JsonProcessingException {
        List<Staff> staffs = staffService.findStaffByRank();
        model.addAttribute("projectDTO", new ProjectDTO());
        model.addAttribute("staffListByRank", staffs);
        model.addAttribute("staffListByRankJSON", objectMapper.writeValueAsString(staffs));
        return "admin/create-project";
    }

    @PostMapping(value = "/admin/save-project")
    public String saveProject(
            @Validated @ModelAttribute("projectDTO") ProjectDTO projectDTO,
            BindingResult result,
            Model model
    ) {
        if (projectService.getOne(projectDTO.getProjectId()) != null) {
            result.addError(
                    new FieldError("projectDTO", "projectId", projectDTO.getProjectId(), false, null, null, "Project ID existed")
            );
        }

        if (projectService.getOne(projectDTO.getProjectName()) != null) {
            result.addError(
                    new FieldError("projectDTO", "projectName", projectDTO.getProjectName(), false, null, null, "Project Name existed")
            );
        }
        if (result.hasErrors()) {
            model.addAttribute("staffListByRank", staffService.findStaffByRank()); // Tải lại danh sách staff
            return "admin/create-project"; // Trả lại view nếu có lỗi
        } else {
            // Gọi service để lưu projectDTO vào database
            projectService.saveProject(projectDTO);
        }
        return "redirect:/admin/project";
    }

    @GetMapping(value = "/admin/edit-project/{projectId}")
    public String edit(
            @PathVariable("projectId") String projectId,
            Model model
    ) throws JsonProcessingException {
        Project project = projectService.getOne(projectId);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setCustomerName(project.getCustomerName());
        projectDTO.setBudget(project.getBudget());

        List<StaffProject> staffProjectList = staffProjectService.findStaffProjectByProjectId(projectId);
        List<StaffProjectDTO> staffProjectListDTO = new ArrayList<>();
        for (StaffProject staffProject : staffProjectList) {
            StaffProjectDTO staffProjectDTO = new StaffProjectDTO();
            staffProjectDTO.setProjectId(staffProject.getProject().getProjectId());
            staffProjectDTO.setStaffId(staffProject.getStaff().getStaffId());
            staffProjectDTO.setStartDate(staffProject.getStartDate());
            staffProjectDTO.setEndDate(staffProject.getEndDate());
            staffProjectDTO.setPosition(staffProject.getRole().toString());
            staffProjectDTO.setEmail(staffProject.getStaff().getEmail());

            staffProjectListDTO.add(staffProjectDTO);
        }
        projectDTO.setStaffProjectListDTO(staffProjectListDTO);
        model.addAttribute("projectDTO", projectDTO);
        model.addAttribute("roleList", Role.values());
        List<Staff> staffs = staffService.findStaffByRank();
        model.addAttribute("staffListByRank", staffs);
        model.addAttribute("staffListByRankJSON", objectMapper.writeValueAsString(staffs));
        return "admin/edit-project";
    }

    @PostMapping(value = "/admin/update-project")
    public String updateProject(
            @Validated @ModelAttribute("projectDTO") ProjectDTO projectDTO,
            BindingResult bindingResult,
            Model model
    ) throws JsonProcessingException {
        Project projectDB = projectService.getOne(projectDTO.getProjectId());

        /** check trùng project name*/
        if (projectDTO.getProjectName().equals(projectDB.getProjectName())) {

        } else {
            if (projectService.findByProjectName(projectDTO.getProjectName()) != null) {
                bindingResult.addError(
                        new FieldError("projectDTO", "projectName", projectDTO.getProjectName(), false, null, null, "Project Name existed")
                );
            }
        }
        String projectId = projectDTO.getProjectId();
        if (bindingResult.hasErrors()) {
//            model.addAttribute("staffListByRank", staffService.findStaffByRank()); // Tải lại danh sách staff
            List<StaffProject> staffProjectList = staffProjectService.findStaffProjectByProjectId(projectId);
            List<StaffProjectDTO> staffProjectListDTO = new ArrayList<>();
            for (StaffProject staffProject : staffProjectList) {
                StaffProjectDTO staffProjectDTO = new StaffProjectDTO();
                staffProjectDTO.setProjectId(staffProject.getProject().getProjectId());
                staffProjectDTO.setStaffId(staffProject.getStaff().getStaffId());
                staffProjectDTO.setStartDate(staffProject.getStartDate());
                staffProjectDTO.setEndDate(staffProject.getEndDate());
                staffProjectDTO.setPosition(staffProject.getRole().toString());
                staffProjectDTO.setEmail(staffProject.getStaff().getEmail());

                staffProjectListDTO.add(staffProjectDTO);
            }
            projectDTO.setStaffProjectListDTO(staffProjectListDTO);
            model.addAttribute("projectDTO", projectDTO);
            model.addAttribute("roleList", Role.values());
            List<Staff> staffs = staffService.findStaffByRank();
            model.addAttribute("staffListByRank", staffs);
            model.addAttribute("staffListByRankJSON", objectMapper.writeValueAsString(staffs));
            return "admin/edit-project"; // Trả lại view nếu có lỗi
        } else {
            // Gọi service để lưu projectDTO vào database
            projectService.updateProject(projectDTO);
        }
        return "redirect:/admin/project";
    }

}
