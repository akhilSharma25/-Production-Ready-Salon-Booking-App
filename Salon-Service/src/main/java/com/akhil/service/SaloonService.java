package com.akhil.service;

import com.akhil.model.Saloon;
import com.akhil.payload.DTO.SaloonDTO;
import com.akhil.payload.DTO.UserDTO;

import java.util.List;

public interface SaloonService {

    Saloon createSaloon(SaloonDTO saloon, UserDTO user);
    Saloon updateSaloon(SaloonDTO saloonDTO,UserDTO user,Long salonId) throws Exception;
    List<Saloon> getAllSalon();
    Saloon getSalonById(Long salonId);
    Saloon getSalonByOwnerId(Long ownerId);
    List<Saloon> searchSalonByCity(String city);


}
