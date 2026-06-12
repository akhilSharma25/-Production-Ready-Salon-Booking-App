package com.akhil.service;

import com.akhil.DTO.CategoryDto;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.ServiceDto;
import com.akhil.model.ServiceOffering;

import java.util.List;
import java.util.Set;

public interface ServiceOfferingService {

    ServiceOffering createService(SaloonDTO salonDto, ServiceDto serviceDto, CategoryDto categoryDto);
    ServiceOffering updateService(Long serviceId,ServiceOffering service);
    Set<ServiceOffering> getAllServiceBySalon(Long salonId,Long categoryId);
    Set<ServiceOffering> getServicesByIds(Set<Long> ids);
    ServiceOffering getServiceById(Long id);
}
