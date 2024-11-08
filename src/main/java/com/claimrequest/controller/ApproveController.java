package com.claimrequest.controller;

import com.claimrequest.entities.Claim;
import com.claimrequest.entities.StaffProject;
import com.claimrequest.entities.Status;
import com.claimrequest.service.ClaimService;
import com.claimrequest.service.StaffProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ApproveController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private StaffProjectService staffProjectService;

    @GetMapping(value = {"/approve/ForMyVetting"})
    public String ForMyVetting(
            Model model
    ) {
        List<Claim> claimList = claimService.getAllClaimPending();
        model.addAttribute("claimList", claimList );
        return "claim/ForMyVetting";
    }

    @GetMapping(value = {"/approve/ApprovedOrPaid"})
    public String ApprovedOrPaid(
            Model model
    ) {
        List<Claim> claimList = claimService.getAllClaimApproveOrPaid();
        model.addAttribute("claimList", claimList );
        return "claim/ApprovedOrPaid";
    }

    @GetMapping("/approve/detailApprove/{claimId}")
    public String showClaimOption(
            Model model,
            @PathVariable Integer claimId) {
        Claim claim = claimService.getOne(claimId);
        List<StaffProject> staffProjects = staffProjectService.getProjectsForStaff(claim.getStaff().getStaffId());

        model.addAttribute("staffProjects", staffProjects);
        model.addAttribute("claim", claim);

        return "claim/Detail";
    }

    @GetMapping("/approve/doneapprove")
    public String submitClaim(
            @RequestParam String claimID
    ) {
        Claim claim = claimService.getOne(Integer.parseInt(claimID));
        claim.setStatus(Status.APPROVED);
        claim.setAuditTrail(claimService.setAuditTrail(4, claim));

        claimService.saveClaim(claim);

        return "redirect:/approve/ApprovedOrPaid";
    }

    @GetMapping("/approve/reject")
    public String rejectClaim(
            @RequestParam String claimID
    ) {
        Claim claim = claimService.getOne(Integer.parseInt(claimID));
        claim.setStatus(Status.REJECTED);
        claim.setAuditTrail(claimService.setAuditTrail(6, claim));

        claimService.saveClaim(claim);

        return "redirect:/approve/ForMyVetting";
    }

    @GetMapping("/approve/cancel")
    public String cancelClaim(
            @RequestParam String claimID
    ) {
        Claim claim = claimService.getOne(Integer.parseInt(claimID));
        claim.setStatus(Status.CANCELLED);
        claim.setAuditTrail(claimService.setAuditTrail(8, claim));

        claimService.saveClaim(claim);

        return "redirect:/approve/ForMyVetting";
    }
}
