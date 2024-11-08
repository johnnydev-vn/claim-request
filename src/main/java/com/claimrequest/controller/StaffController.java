package com.claimrequest.controller;

import com.claimrequest.entities.Staff;
import com.claimrequest.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping(value = "/admin/staff")
    public String getAll(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            Model model
    ) {
        Page<Staff> staff = null;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        staff = staffService.findAll(pageable); // Tải lại danh sách staff
        model.addAttribute("page", staff);
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= staff.getTotalPages(); i++) {
            pageNums.add(i);
        }
        model.addAttribute("pageNums", pageNum);
        return "admin/view-staff";
    }

    @GetMapping(value = "admin/delete-staff/{staffId}")
    public String delete(
            @PathVariable("staffId") String staffId

    ){
        staffService.deleteStaff(staffId);
        return "redirect:/admin/staff";
    }

    @GetMapping(value = "/admin/create-staff")
    public String create(
            Model model
    ) {
        model.addAttribute("staff", new Staff());
        return "admin/create-staff";
    }

    @PostMapping(value = "/admin/create-staff")
    public String save(
            Model model,
            @Validated @ModelAttribute("staff") Staff staff,
            RedirectAttributes redirectAttributes,
            BindingResult bindingResult
    ) {
        if (staffService.getOne(staff.getStaffId()) != null) {
            bindingResult.addError(
                    new FieldError( "staff", "staffId", staff.getStaffId(), false, null, null, "Staff ID existed")
            );
        }

        if (staffService.findByEmail(staff.getEmail()) != null) {
            bindingResult.addError(
                    new FieldError( "staff", "email", staff.getEmail(), false, null, null, "Email existed")
            );
        }

        if (staffService.findByPhoneNumber(staff.getPhoneNumber()) != null) {
            bindingResult.addError(
                    new FieldError( "staff", "phoneNumber", staff.getPhoneNumber(), false, null, null, "Phone number existed")
            );
        }
        if (staffService.findByUsername(staff.getUsername()) != null) {
            bindingResult.addError(
                    new FieldError( "staff", "username", staff.getUsername(), false, null, null, "Username existed")
            );
        }
        if (bindingResult.hasErrors()) {
            return "admin/create-staff";
        }
        else {
            staffService.saveStaff(staff);
        }
        return "redirect:/admin/staff";
    }

    @GetMapping(value = "/admin/edit-staff/{staffId}")
    public String edit(
            @PathVariable("staffId") String staffId,
            Model model
    ) {
        if(staffId == null) {
            return "redirect:/admin/staff";
        }
        Staff staff = staffService.getOne(staffId);
        model.addAttribute("staff", staff);
        return "admin/edit-staff";
    }

    @PostMapping(value = "/admin/update-staff")
    public String update(
            @Validated @ModelAttribute("staff") Staff staff,
            BindingResult bindingResult
    ) {
        Staff staffDB = staffService.getOne(staff.getStaffId());
        /** check trùng email*/
        if (staff.getEmail().equals(staffDB.getEmail())) {

        }else {
            if (staffService.findByEmail(staff.getEmail()) != null) {
                bindingResult.addError(
                        new FieldError( "staff", "email", staff.getEmail(), false, null, null, "Email existed")
                );
            }
        }
        /** check trùng username */
        if (staff.getUsername().equals(staffDB.getUsername())) {

        }else {
            if (staffService.findByUsername(staff.getUsername()) != null) {
                bindingResult.addError(
                        new FieldError( "staff", "username", staff.getUsername(), false, null, null, "Username existed")
                );
            }
        }

        /** check trùng phone*/
        if (staff.getPhoneNumber().equals(staffDB.getPhoneNumber())) {

        }else {
            if (staffService.findByPhoneNumber(staff.getEmail()) != null) {
                bindingResult.addError(
                        new FieldError( "staff", "phoneNumber", staff.getEmail(), false, null, null, "Phone Number existed")
                );
            }
        }

        if (bindingResult.hasErrors()) {
            return "/admin/edit-staff";
        }
        else {
            staffService.updateStaff(staff);
        }
        return "redirect:/admin/staff";
    }



}
