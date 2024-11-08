package com.claimrequest.repositoty;

import com.claimrequest.entities.Claim;
import com.claimrequest.entities.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer> {

    List<Claim> findByStatusAndStaff_StaffId(Status status, String staffId);

    Page<Claim> findAll(Pageable pageable);

    List<Claim> findByStatusInAndStaff_StaffId(List<Status> status, String staffId);
    List<Claim> findByStatusIn(List<Status> statuses);
    List<Claim> findByStatus(Status status);
}
