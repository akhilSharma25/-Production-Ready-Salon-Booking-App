package com.akhil.mapper;

import com.akhil.model.Saloon;
import com.akhil.payload.DTO.SaloonDTO;

public class SalonMapper {

    public  static SaloonDTO mapToDTO(Saloon saloon){
        SaloonDTO saloonDTO=new SaloonDTO();
        saloonDTO.setName(saloon.getName());
        saloonDTO.setAddress(saloon.getAddress());
        saloonDTO.setEmail(saloon.getEmail());
        saloonDTO.setCity(saloon.getCity());
        saloonDTO.setOwnerId(saloon.getOwnerId());
        saloonDTO.setImages(saloon.getImages());
        saloonDTO.setPhoneNumber(saloon.getPhoneNumber());
        saloonDTO.setOpenTime(saloon.getOpenTime());
        saloonDTO.setCloseTime(saloon.getCloseTime());

        return  saloonDTO;
    }
}
