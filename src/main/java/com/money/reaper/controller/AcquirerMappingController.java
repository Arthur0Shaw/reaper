package com.money.reaper.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 

import com.money.reaper.dto.AcquirerMappingRequest;
import com.money.reaper.dto.AcquirerRoutingDetailsRequest;
import com.money.reaper.model.AcquirerMapping;
import com.money.reaper.model.AcquirerRoutingDetails;
import com.money.reaper.service.AcquirerMappingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/acquirer")
@Validated
public class AcquirerMappingController {

	@Autowired
	private AcquirerMappingService acquirerMappingService;

	@PostMapping("/saveMapping")
	public ResponseEntity<?> saveMapping(@Valid @RequestBody AcquirerMappingRequest request,
			HttpServletRequest httpServletRequest) {
		try {
			String ipAddress = httpServletRequest.getRemoteAddr();
			AcquirerMapping acqDetails = acquirerMappingService.saveMapping(request);
			return ResponseEntity.ok(acqDetails);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/txnRoutingDetails")
	public ResponseEntity<?> saveMapping(@Valid @RequestBody AcquirerRoutingDetailsRequest request,
			HttpServletRequest httpServletRequest) {
		try {
			String ipAddress = httpServletRequest.getRemoteAddr();
			AcquirerRoutingDetails acqDetails = acquirerMappingService.saveAcqTxnRoutingDetails(request);
			return ResponseEntity.ok(acqDetails);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}
