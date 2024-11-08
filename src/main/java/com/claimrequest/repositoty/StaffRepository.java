package com.claimrequest.repositoty;

import com.claimrequest.entities.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    @Override
    List<Staff> findAll();

    @Query(value = "select s.* from staff as s where s.rank = 3 or s.rank = 2", nativeQuery = true)
    List<Staff> findStaffByRank();

    Staff findByEmail(String email);

    Staff findByPhoneNumber(String phone);

    Staff findByUsername(String username);

    Page<Staff> findAll(Pageable pageable);

    Staff save(Staff staff);
}
