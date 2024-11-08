package com.claimrequest.controller;

import com.claimrequest.repositoty.StaffProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StaffProjectController {
    @Autowired
    private StaffProjectRepository staffProjectRepository;


}
