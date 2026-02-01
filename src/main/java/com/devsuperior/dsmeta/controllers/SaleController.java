package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.projections.SaleMinProjection;
import com.devsuperior.dsmeta.projections.SellerMinProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.services.SaleService;

import java.util.List;


@RestController
@RequestMapping(value = "/sales")
public class SaleController {

	@Autowired
	private SaleService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
		SaleMinDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/report")
	public ResponseEntity<Page<SaleMinProjection>> getReport(@RequestParam(name = "name", defaultValue = "")String name,
															 @RequestParam(name = "minDate", defaultValue = "")String startDate,
															 @RequestParam(name = "maxDate", defaultValue = "")String endDate,
															 Pageable pageable) {
		Page<SaleMinProjection> page = service.findByDateAndName(startDate, endDate, name, pageable);
		return ResponseEntity.ok(page);
	}

	@GetMapping(value = "/summary")
	public ResponseEntity<List<SellerMinProjection>> getSummary(@RequestParam(name = "minDate", defaultValue = "") String minDate,
																@RequestParam(name = "maxDate", defaultValue = "") String maxDate) {
		List<SellerMinProjection> sellers = service.findTotal(minDate, maxDate);
		return ResponseEntity.ok(sellers);
	}
}
