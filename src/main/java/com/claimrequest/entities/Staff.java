package com.claimrequest.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "STAFF")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "staffId") // Sử dụng staffId để tránh đệ quy quá sâu
public class Staff {
    @NotBlank(message = "Please specify value for this field")
    @Id
    @Column(name = "STAFF_ID", length = 7)
    private String staffId;

    @NotBlank(message = "Please specify value for this field")
    @Column(name = "FIRST_NAME", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String firstName;

    @NotBlank(message = "Please specify value for this field")
    @Column(name = "LAST_NAME", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String lastName;

    @Column(name = "GENDER", nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank(message = "Please specify value for this field")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email address")
    @Column(name = "EMAIL", nullable = false, length = 30)
    private String email;

    @NotEmpty (message = "Department cannot be empty")
    @Column(name = "DEPARTMENT",length = 50)
    private String Department;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    @Column(name = "PHONE_NUMBER", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "SALARY", nullable = false, columnDefinition = "NUMERIC(18, 3)")
    private Double salary;


    @Column(name = "RANK", nullable = false, columnDefinition = "INT CHECK(RANK BETWEEN 1 AND 4)")
    private Integer rank;

    @NotBlank(message = "Please specify value for this field")
    @Column(name = "USERNAME", nullable = false, length = 20)
    private String username;


//    @Size(min = 8, message = "Password must be at least 8 characters")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
//            message = "Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Claim> claimsList;

    @JsonIgnore
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<StaffProject> staffProjectsList;

    public List<Staff> getPM(){
        List<Staff> result = new ArrayList<>();
        if(staffProjectsList != null && staffProjectsList.size() > 0){
            for (StaffProject staffProject : staffProjectsList) {
                if(staffProject.getRole() == Role.PM){
                    result.add(staffProject.getStaff());
                }
            }
            return result;
        }
        return null;
    }

}
