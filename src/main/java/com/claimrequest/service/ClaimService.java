package com.claimrequest.service;

import com.claimrequest.entities.Claim;
import com.claimrequest.entities.Status;
import com.claimrequest.repositoty.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.claimrequest.entities.Status.*;

@Service
public class ClaimService {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    @Autowired
    private ClaimRepository claimRepository;

    public Claim getOne(Integer staffId) {
        Optional<Claim> claim = claimRepository.findById(staffId);
        if (claim.isPresent()) {
            return claim.get();
        }
        return null;
    }

    public void saveClaim(Claim claim) {
        claimRepository.save(claim);
    }

    public List<Claim> getListClaimByStatus(String status, String staffId) {
        return claimRepository.findByStatusAndStaff_StaffId(Status.valueOf(status), staffId);
    }

    public List<Claim> getListClaimByListStatus(List<String> statuses, String staffId) {
        List<Status> statusList = statuses.stream()
                .map(Status::valueOf)
                .collect(Collectors.toList());
        return claimRepository.findByStatusInAndStaff_StaffId(statusList, staffId);
    }


    public void saveClaimWithDeletedDetails(Claim claim, List<Integer> deletedDetailIds) {
        claim.getClaimDetailsList().removeIf(
                detail -> deletedDetailIds.contains(detail.getId())
        );
        claimRepository.save(claim); // Save the claim without the details first
    }

    public String setAuditTrail(int type, Claim claim){
        LocalDateTime now = LocalDateTime.now();
        String message = claim.getStaff().getUsername() + " on " + dtf.format(now);
        String current = claim.getAuditTrail();
        switch (type){
            case 1:
                return "Created by " + message;
            case 2:
                return current + "_Updated by " + message;
            case 3:
                return current + "_Submitted by " + message;
            case 4:
                return current + "_Approved by " + message;
            case 5:
                return current + "_Returned by " + message;
            case 6:
                return current + "_Rejected by " + message;
            case 7:
                return current + "_Paid by " + message;
            case 8:
                return current + "_Cancelled by " + message;
            default:
                return "Invalid type";
        }
    }

    public Page<Claim> findAll(Pageable pageable) {
        return claimRepository.findAll(pageable);
    }
    public List<Claim> getAllClaimPending() {
        return claimRepository.findByStatus(PENDING_APPROVAL);
    }

    public List<Claim> getAllClaimApproveOrPaid() {
        return claimRepository.findByStatusIn(Arrays.asList(APPROVED, PAID));
    }
    public List<Claim> getAllClaimApproved() {
        return claimRepository.findByStatus(APPROVED);
    }
    public List<Claim> getAllClaimPaid() {
        return claimRepository.findByStatus(PAID);
    }
}
