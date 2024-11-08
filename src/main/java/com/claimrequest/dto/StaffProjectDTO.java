package com.claimrequest.dto;

import com.claimrequest.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffProjectDTO {
    private String projectId;
    private String staffId;
    private Date startDate;
    private Date endDate;
    private Role role;
    private String position;
    private String email;
}
