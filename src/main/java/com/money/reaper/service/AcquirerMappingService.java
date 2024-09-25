package com.money.reaper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.AcquirerMappingRequest;
import com.money.reaper.dto.AcquirerRoutingDetailsRequest;
import com.money.reaper.model.AcquirerMapping;
import com.money.reaper.model.AcquirerRoutingDetails;
import com.money.reaper.repository.AcquirerMappingRepository;
import com.money.reaper.repository.AcquirerRoutingDetailsRespository;

@Service
public class AcquirerMappingService {

	@Autowired
	private AcquirerMappingRepository acquirerMappingRepository;

	@Autowired
	private AcquirerRoutingDetailsRespository acquirerRoutingDetailsRespository;

	public AcquirerMapping saveMapping(AcquirerMappingRequest request) {
		AcquirerMapping acqDetails = new AcquirerMapping();
		acqDetails.setBank(request.getBank());
		acqDetails.setUsername(request.getUsername());
		acqDetails.setPassword(request.getPassword());
		acqDetails.setVpa(request.getVpa());
		acqDetails.setAdf1(request.getAdf1());
		acqDetails.setAdf2(request.getAdf2());

		return acquirerMappingRepository.save(acqDetails);
	}

	public AcquirerRoutingDetails saveAcqTxnRoutingDetails(AcquirerRoutingDetailsRequest request) {
		AcquirerRoutingDetails acqRoutingDetails = new AcquirerRoutingDetails();
		acqRoutingDetails.setBank(request.getBank());
		acqRoutingDetails.setTotalTxnCount(request.getTotalTxnCount());
		acqRoutingDetails.setMaximumTxnAmount(request.getMaximumTxnAmount());
		acqRoutingDetails.setThresholdTxnAmount(request.getThresholdTxnAmount());

		return acquirerRoutingDetailsRespository.save(acqRoutingDetails);
	}

}
