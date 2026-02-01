package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.projections.SaleMinProjection;
import com.devsuperior.dsmeta.projections.SellerMinProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	@Transactional(readOnly = true)
	public Page<SaleMinProjection> findByDateAndName(String minDate, String maxDate, String name, Pageable pageable) {
		DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;
		LocalDate endDate;
		if(maxDate == null || maxDate.isBlank()){
			endDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}else{
			endDate = LocalDate.parse(maxDate, fmt);
		}

		LocalDate startDate;
		if(minDate == null || minDate.isBlank()){
			startDate = endDate.minusYears(1L);
		}else{
			startDate = LocalDate.parse(minDate, fmt);
		}

		return repository.searchByDateAndName(startDate, endDate, name, pageable);

	}

	@Transactional(readOnly = true)
	public List<SellerMinProjection> findTotal(String minDate, String maxDate){
		DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;
		LocalDate max;
		if(maxDate == null || maxDate.isBlank()){
			max = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}else{
			max = LocalDate.parse(maxDate, fmt);
		}

		LocalDate min;
		if(minDate == null || minDate.isBlank()){
			min = max.minusYears(1L);
		}else{
			min = LocalDate.parse(minDate, fmt);
		}
		return repository.searchTotal(min, max);
	}


}
