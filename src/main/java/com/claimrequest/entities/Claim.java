package com.claimrequest.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CLAIM")
public class Claim {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CLAIM_ID")
    private Integer claimId;

    @Column(name = "TOTAL_WORKING_HOUR",nullable = false, columnDefinition = "DECIMAL(3, 1)")
    private Double totalWorkingHour;

    @Column(name = "REMARKS_CLAIM", columnDefinition = "TEXT")
    private String remarksClaim;

    @Column(name = "AUDIT_TRAIL", columnDefinition = "TEXT")
    private String auditTrail;

    /*CHECK([STATUS] IN ('DRAFT', 'PENDING APPROVAL', 'APPROVED', 'PAID', 'REJECTED', 'CANCELLED'))*/
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "STAFF_ID", nullable = false, referencedColumnName = "STAFF_ID")
    private Staff staff;
    /** Chỉnh sửa StaffId = Staff*/

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID",nullable = false, referencedColumnName = "PROJECT_ID")
    private Project project;
    /** Chỉnh sửa ProjectId = Project*/

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimDetail> claimDetailsList;
}
