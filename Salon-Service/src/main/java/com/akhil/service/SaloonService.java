package com.akhil.service;

import com.akhil.model.Saloon;
import com.akhil.payload.DTO.SaloonDTO;
import com.akhil.payload.DTO.UserDTO;

public interface SaloonService {

    Saloon createSaloon(SaloonDTO saloon, UserDTO user);
    Saloon updateSaloon(SaloonDTO saloonDTO,UserDTO user,Long salonId);

}
