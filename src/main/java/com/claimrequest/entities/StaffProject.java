package com.claimrequest.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "STAFF_PROJECT")
public class StaffProject implements Serializable {

    @EmbeddedId
    private StaffProjectId staffProjectId;

    @NotBlank(message = "Please specify value for this field")
    @Column(name = "ROLE", nullable = false, length =50)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", columnDefinition = "DATE")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", columnDefinition = "DATE")
    private Date endDate;

    @ManyToOne()
    @MapsId("staffId")
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
    private Staff staff;

    @ManyToOne()
    @MapsId("projectId")
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "PROJECT_ID")
    private Project project;

}
