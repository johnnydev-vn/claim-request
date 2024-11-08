package com.claimrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private String projectId;
    private String projectName;
    private Date startDate;
    private Date endDate;
    private String customerName;
    private Double budget;
    private List<StaffProjectDTO> staffProjectListDTO = new ArrayList<>();
}
