package com.geekyants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SsrController {

    @Autowired
    private SsrcService service;

    @GetMapping("/ssrc")
    public String getSsrc() {
        return service.getSsrc();
    }
}
