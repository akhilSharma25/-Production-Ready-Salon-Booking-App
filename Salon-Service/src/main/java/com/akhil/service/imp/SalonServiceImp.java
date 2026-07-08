package com.akhil.service.imp;

import com.akhil.model.Saloon;
import com.akhil.payload.DTO.SaloonDTO;
import com.akhil.payload.DTO.UserDTO;
import com.akhil.repo.SalonRepo;
import com.akhil.service.SaloonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalonServiceImp implements SaloonService {

    private final SalonRepo repo;
    @Override
    public Saloon createSaloon(SaloonDTO req, UserDTO user) {

        Saloon salon=new Saloon();
        salon.setName(req.getName());
        salon.setAddress(req.getAddress());
        salon.setEmail(req.getEmail());
        salon.setCity(req.getCity());
        salon.setOwnerId(user.getId());
        salon.setImages(req.getImages());
        salon.setPhoneNumber(req.getPhoneNumber());
        salon.setOpenTime(req.getOpenTime());
        salon.setCloseTime(req.getCloseTime());
        return repo.save(salon);
    }

    @Override
    public Saloon updateSaloon(SaloonDTO saloonDTO, UserDTO user, Long salonId) throws Exception {
        Saloon exitingSalon=repo.findById(salonId).orElse(null);
        if(exitingSalon!=null && exitingSalon.getOwnerId().equals(user.getId())){
            exitingSalon.setName(saloonDTO.getName());
            exitingSalon.setAddress(saloonDTO.getAddress());
            exitingSalon.setEmail(saloonDTO.getEmail());
            exitingSalon.setCity(saloonDTO.getCity());
            exitingSalon.setOwnerId(user.getId());
            exitingSalon.setImages(saloonDTO.getImages());
            exitingSalon.setPhoneNumber(saloonDTO.getPhoneNumber());
            exitingSalon.setOpenTime(saloonDTO.getOpenTime());
            exitingSalon.setCloseTime(saloonDTO.getCloseTime());

           return repo.save(exitingSalon);
        }


        if (!exitingSalon.getOwnerId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        throw new RuntimeException("Salon not found");

    }

    @Override
    public List<Saloon> getAllSalon() {
        return repo.findAll();
    }

    @Override
    public Saloon getSalonById(Long salonId) {
        return repo.findById(salonId)
                .orElseThrow(() -> new RuntimeException("Salon does not exist"));    }

    @Override
    public Saloon getSalonByOwnerId(Long ownerId) {
        return  repo.findByOwnerId(ownerId).orElseThrow(() -> new RuntimeException("Salon does not exist"));
    }

    @Override
    public List<Saloon> searchSalonByCity(String city) {
        return repo.searchSalon(city);
    }
}
