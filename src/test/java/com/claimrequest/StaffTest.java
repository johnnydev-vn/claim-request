package com.claimrequest;

import com.claimrequest.entities.Gender;
import com.claimrequest.entities.Staff;
import com.claimrequest.repositoty.StaffRepository;
import com.claimrequest.service.StaffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;


@Component
@SpringBootTest
public class StaffTest {
    @Autowired
    private StaffService staffService;

    @Test
    public void createStaff() {
        Staff staff = new Staff();
        staff.setStaffId("NV0001");
        staff.setFirstName("Nguyễn Thị");
        staff.setLastName("Vân");
        staff.setGender(Gender.Female);
        staff.setEmail("admin@gmail.com");
        staff.setDepartment("IT");
        staff.setPhoneNumber("0975885111");
        staff.setSalary(2000000.000);
        staff.setRank(1);
        staff.setUsername("admin");
        staff.setPassword("123");

        System.out.println(staff.getFirstName());

        staffService.saveStaff(staff);
    }
}
