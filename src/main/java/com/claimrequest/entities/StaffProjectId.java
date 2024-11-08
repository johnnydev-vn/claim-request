package com.claimrequest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StaffProjectId implements Serializable {
    @NotBlank(message = "Please specify value for this field")
    @Column(name = "STAFF_ID", length = 7)
    private String staffId;

    @NotBlank(message = "Please specify value for this field")
    @Column(name = "PROJECT_ID", length = 7)
    private String projectId;
}
