package com.claimrequest.controller;

import com.claimrequest.Auth.CustomUserDetail;
import com.claimrequest.entities.*;
import com.claimrequest.service.ClaimService;
import com.claimrequest.service.StaffProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/claim")
public class ClaimController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);

    @Autowired
    private StaffProjectService staffProjectService;

    @Autowired
    private ClaimService claimService;

    @GetMapping(value = "/admin/all-claim")
    public String getAll(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            Model model
    ) {
        Page<Claim> claims = null;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        claims = claimService.findAll(pageable);
        model.addAttribute("page", claims);

        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= claims.getTotalPages(); i++) {
            pageNums.add(i);
        }
        model.addAttribute("pageNums", pageNum);
        return "admin/all-claim";
    }

    private Staff getCurrentStaff() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail cud = (CustomUserDetail) auth.getPrincipal();
        return cud.getStaffDB();
    }

    @GetMapping("/create")
    public String showCreateClaim(
            Model model
    ) {
        // Prepare a DTO with default values
        Claim claim = new Claim();
        claim.setStaff(getCurrentStaff());
        model.addAttribute("claim", claim);

        List<StaffProject> staffProjects = staffProjectService.getProjectsForStaff(getCurrentStaff().getStaffId());
        model.addAttribute("staffProjects", staffProjects);

        return "claim/create-claim";
    }

    @PostMapping("/create")
    public String createClaim(
            @Valid @ModelAttribute("claim") Claim claim,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            List<StaffProject> staffProjects = staffProjectService.getProjectsForStaff(getCurrentStaff().getStaffId());
            model.addAttribute("staffProjects", staffProjects);
            return "claim/create-claim";
        }
        for (ClaimDetail detail : claim.getClaimDetailsList()) {
            detail.setClaim(claim); // Assign the claim to the claim detail
        }
        claim.setAuditTrail(claimService.setAuditTrail(1, claim));
        claim.setStatus(Status.DRAFT);

        claimService.saveClaim(claim);

        return "redirect:/claim/view/draft";
    }

    @GetMapping("/view/{status}")
    public String showClaimsByStatus(
            @PathVariable("status") String status,
            Model model
    ) {
        List<Claim> list;
        if (status.equals("rejectedOrCancelled") ||
                status.equals("rejected") ||
                status.equals("cancelled")
        ) {
            List<String> listStatus = new ArrayList<>();
            listStatus.add("REJECTED");
            listStatus.add("CANCELLED");
            list = claimService.getListClaimByListStatus(listStatus, getCurrentStaff().getStaffId());
            model.addAttribute("status", "rejectedOrCancelled");
        } else {
            list = claimService.getListClaimByStatus(status.toUpperCase(), getCurrentStaff().getStaffId());
            model.addAttribute("status", status);
        }
        model.addAttribute("claimList", list);

        return "claim/view-claim";
    }

    @GetMapping("/detail")
    public String showDetailClaim(
            @RequestParam Integer claimId,
            Model model
    ) {
        Claim claim = claimService.getOne(claimId);
        List<StaffProject> staffProjects = staffProjectService.getProjectsForStaff(claim.getStaff().getStaffId());
        model.addAttribute("staffProjects", staffProjects);

        model.addAttribute("claim", claim);

        return "claim/detail-claim";
    }

    @PostMapping("/update")
    public String updateClaim(
            @Valid @ModelAttribute("claim") Claim claim,
            @RequestParam(name = "deletedDetailIds", required = false) String deletedDetailIds,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            List<StaffProject> staffProjects = staffProjectService.getProjectsForStaff(claim.getStaff().getStaffId());
            model.addAttribute("staffProjects", staffProjects);
            return "claim/detail-claim";
        }
        for (ClaimDetail detail : claim.getClaimDetailsList()) {
            detail.setClaim(claim); // Assign the claim to the claim detail
        }
        claim.setAuditTrail(claimService.setAuditTrail(2, claim));

        logger.info(deletedDetailIds.toString());

        List<Integer> deletedIds = new ArrayList<>();
        if (deletedDetailIds != null && !deletedDetailIds.isEmpty()) {
            for (String id : deletedDetailIds.split("-")) {
                deletedIds.add(Integer.parseInt(id));
            }
        }
        for (int id : deletedIds) {
            logger.info(String.valueOf(id));
        }

        claimService.saveClaimWithDeletedDetails(claim, deletedIds);

        return "redirect:/claim/view/draft";
    }

    @GetMapping("/cancel")
    public String cancelClaim(
            @RequestParam String claimID
    ) {
        Claim claim = claimService.getOne(Integer.parseInt(claimID));
        claim.setStatus(Status.CANCELLED);
        claim.setAuditTrail(claimService.setAuditTrail(8, claim));

        claimService.saveClaim(claim);

        return "redirect:/claim/view/rejectedOrCancelled";
    }

    @GetMapping("/submit")
    public String submitClaim(
            @RequestParam String claimID
    ) {
        Claim claim = claimService.getOne(Integer.parseInt(claimID));
        claim.setStatus(Status.PENDING_APPROVAL);
        claim.setAuditTrail(claimService.setAuditTrail(3, claim));

        claimService.saveClaim(claim);

//        List<StaffProject> staffProjects = staffProjectService.findStaffProjectByProjectId(claim.getProject().getProjectId());
//        StaffProject PMStaff = staffProjects.stream()
//                .filter(staffProject -> staffProject.getRole().equals(Role.PM))
//                .findFirst()
//                .orElse(null);;
//        String link = "";
//        String subject = "Claim Request " +
//                claim.getProject().getProjectName() + " " +
//                claim.getStaff().getFirstName() + " " + claim.getStaff().getLastName() + " - " +
//                claim.getStaff().getStaffId();
//        emailService.sendEmail(
//                PMStaff.getStaff().getEmail(),
//                subject,
//                emailService.createET1Body(
//                        PMStaff.getStaff().getFirstName(),
//                        claim.getProject().getProjectName(),
//                        claim.getStaff().getFirstName(),
//                        claim.getStaff().getStaffId(),
//                        link),
//                claim.getStaff().getStaffId()
//                );

        return "redirect:/claim/view/pending_approval";
    }

    @PostMapping("/updateProjectInfo")
    @ResponseBody
    public Map<String, String> updateProjectSelection(
            @RequestParam("selectedProject") String projectId
    ) {
        StaffProject staffProject = staffProjectService.getStaffProjectById(getCurrentStaff().getStaffId().toString(), projectId.toString());
        if (staffProject == null) {
            return null;
        }
        Map<String, String> result = Map.of(
                "role", staffProject.getRole().toString(),
                "duration", staffProject.getProject().getDuration().toString(),
                "startDate", staffProject.getStartDate().toString(),
                "endDate", staffProject.getEndDate().toString()
        );
        return result;

    }
}
