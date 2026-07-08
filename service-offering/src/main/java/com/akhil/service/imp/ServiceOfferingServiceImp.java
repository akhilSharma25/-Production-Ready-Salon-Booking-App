package com.akhil.service.imp;

import com.akhil.DTO.CategoryDto;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.ServiceDto;
import com.akhil.model.ServiceOffering;
import com.akhil.repo.ServiceOfferingRepo;
import com.akhil.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImp implements ServiceOfferingService {

    private final ServiceOfferingRepo repo;
    @Override
    public ServiceOffering createService(SaloonDTO salonDto, ServiceDto serviceDto, CategoryDto categoryDto) {
        ServiceOffering serviceOffering=new ServiceOffering();
        serviceOffering.setImage(serviceDto.getImage());
        serviceOffering.setName(serviceDto.getName());
        serviceOffering.setDuration(serviceDto.getDuration());
        serviceOffering.setPrice(serviceDto.getPrice());
        serviceOffering.setDescription(serviceDto.getDescription());
        serviceOffering.setSalonId(salonDto.getId());
        serviceOffering.setCategoryId(serviceDto.getCategoryId());
        return repo.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) {

        ServiceOffering serviceOffering=repo.findById(serviceId).orElse(null);

        if(serviceOffering==null){
            throw  new RuntimeException("Service not exist with id "+serviceId);

        }
        serviceOffering.setImage(service.getImage());
        serviceOffering.setName(service.getName());
        serviceOffering.setDuration(service.getDuration());
        serviceOffering.setPrice(service.getPrice());
        serviceOffering.setDescription(service.getDescription());

        return repo.save(serviceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServiceBySalon(Long salonId, Long categoryId) {
        Set<ServiceOffering> serviceOfferings=repo.findBySalonId(salonId);

        if(categoryId!=null){
            serviceOfferings=serviceOfferings.stream().filter((service)->service.getCategoryId()!=null && service.getCategoryId().equals(categoryId)).collect(Collectors.toSet());
        }

        return  serviceOfferings;

    }

    @Override
    public Set<ServiceOffering> getServicesByIds(Set<Long> ids) {
        List<ServiceOffering> serviceOfferingList= repo.findAllById(ids);
        return  new HashSet<>(serviceOfferingList);
    }

    @Override
    public ServiceOffering getServiceById(Long id) {
        return repo.findById(id).orElseThrow(()->new RuntimeException("Service not exist with id"+id));
    }
}
