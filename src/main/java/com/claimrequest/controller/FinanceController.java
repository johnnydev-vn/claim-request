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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FinanceController {
    @Autowired
    private ClaimService claimService;
    @Autowired
    private StaffProjectService staffProjectService;

    @GetMapping(value = {"/finance/FinanceApproved"})
    public String FinanceApproved(
            Model model
    ) {
        List<Claim> claimList = claimService.getAllClaimApproved();
        model.addAttribute("claimList", claimList );
        return "finance/FinanceApproved";
    }

    @GetMapping(value = {"/finance/Paid"})
    public String FinancePaid(
            Model model
    ) {
        List<Claim> claimList = claimService.getAllClaimPaid();
        model.addAttribute("claimList", claimList );
        return "finance/FinancePaid";
    }

    @GetMapping("/finance/detailFinance")
    public String showClaimOption(
            Model model,
            @RequestParam Integer claimID) {
        Claim claim = claimService.getOne(claimID);
        List<StaffProject> staffProjects = staffProjectService.getProjectsForStaff(claim.getStaff().getStaffId());

        model.addAttribute("staffProjects", staffProjects);
        model.addAttribute("claim", claim);

        return "claim/DetailFinance";
    }

    @GetMapping("/finance/paid")
    public String submitClaimPaid(
            @RequestParam String claimID
    ) {
        Claim claim = claimService.getOne(Integer.parseInt(claimID));
        claim.setStatus(Status.PAID);
        claim.setAuditTrail(claimService.setAuditTrail(7, claim));

        claimService.saveClaim(claim);

        return "redirect:/finance/Paid";
    }
}
