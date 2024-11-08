package com.claimrequest.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@Entity
@Table(name = "CLAIM_DETAIL")
public class ClaimDetail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", columnDefinition = "INT")
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_CLAIM",nullable = false, columnDefinition = "DATE")
    private Date dateClaim;

    @Column(name = "DAY", nullable = false)
    private String day;

    @Temporal(TemporalType.TIME)
    @Column(name = "FROM_TIME",nullable = false, columnDefinition = "TIME")
    private Time fromTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "TO_TIME",nullable = false, columnDefinition = "TIME")
    private Time toTime;

    @Column(name = "REMARKS_DETAILS",nullable = false, length = 100, columnDefinition = "NVARCHAR(100)")
    private String remarksDetails;

    @Column(name = "TOTAL_NO_OF_HOURS", columnDefinition = "DECIMAL(3,1)")
    private Double totalNoOfHours;

    @ManyToOne
    @JoinColumn(name = "CLAIM_ID",nullable = false, referencedColumnName = "CLAIM_ID", columnDefinition = "INT")
    private Claim claim;
    /** chỉnh sủa ClaimId = Claim*/

}
