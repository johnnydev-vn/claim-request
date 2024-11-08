package com.claimrequest.service;

import com.claimrequest.entities.Staff;
import com.claimrequest.repositoty.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public List<Staff> getAll() {
        return staffRepository.findAll();
    }

    public void deleteStaff(
            String staffId
    ) {
        staffRepository.deleteById(staffId);
    }

    public Staff getOne(String staffId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            return staff.get();
        }
        return null;
    }

    public void saveStaff(Staff staff) {
        String encodedPassword = passwordEncoder.encode(staff.getPassword());
        staff.setPassword(encodedPassword);
        staffRepository.save(staff);
    }

    public void updateStaff(Staff staff) {
        Staff existingStaff = staffRepository.findById(staff.getStaffId()).orElseThrow(null);

        if (staff.getPassword() == null || staff.getPassword().isEmpty()) {
          /**  Nếu không nhập mật khẩu mới, giữ lại mật khẩu cũ*/
            staff.setPassword(existingStaff.getPassword());
        }
      else {
         /** Nếu mật khẩu mới được nhập, mã hóa trước khi lưu */
            String encodedPassword = passwordEncoder.encode(staff.getPassword());
            staff.setPassword(encodedPassword);
        }
        staffRepository.save(staff);
    }

    public List<Staff> findStaffByRank() {
        return staffRepository.findStaffByRank();
    }

    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    public Staff findByPhoneNumber(String phone) {
        return staffRepository.findByPhoneNumber(phone);
    }

    public Staff findByUsername(String username) {
        return staffRepository.findByUsername(username);
    }

    public Page<Staff> findAll(Pageable pageable) {
        return staffRepository.findAll(pageable);
    }
}
