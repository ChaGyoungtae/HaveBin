package com.HaveBinProject.HaveBin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://www.have-bin.com",
        "https://have-bin.com",})
public class health {

    @GetMapping("/health")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok("200.OK");
    }
}
