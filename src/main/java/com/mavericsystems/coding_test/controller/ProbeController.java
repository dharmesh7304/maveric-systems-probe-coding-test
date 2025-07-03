package com.mavericsystems.coding_test.controller;

import com.mavericsystems.coding_test.dto.ControlRequest;
import com.mavericsystems.coding_test.dto.ControlResponse;
import com.mavericsystems.coding_test.service.ProbeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/probe")
@RequiredArgsConstructor
public class ProbeController {

    private final ProbeService probeService;


    @PostMapping("/control")
    public ResponseEntity<ControlResponse> controlProbe(@RequestBody ControlRequest request) {
        ControlResponse response = probeService.controlProbe(request);
        return ResponseEntity.ok(response);
    }
}