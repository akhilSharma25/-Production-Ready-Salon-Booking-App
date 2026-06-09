package com.akhil.controller;

import com.akhil.mapper.SalonMapper;
import com.akhil.model.Saloon;
import com.akhil.payload.DTO.SaloonDTO;
import com.akhil.payload.DTO.UserDTO;
import com.akhil.service.SaloonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SaloonService service;

    @PostMapping
    public ResponseEntity<SaloonDTO> createSalon(@RequestBody SaloonDTO saloonDTO){
        UserDTO user=new UserDTO();
        user.setId(1L);
       Saloon salon= service.createSaloon(saloonDTO,user);

       SaloonDTO saloonDTO1= SalonMapper.mapToDTO(salon);
       return  new ResponseEntity<>(saloonDTO1,HttpStatus.CREATED);
    }

    @PatchMapping("/{salonId}")
    public ResponseEntity<SaloonDTO> updateSalon(@RequestBody SaloonDTO saloonDTO,@PathVariable Long salonId ) throws Exception {
        UserDTO user=new UserDTO();
        user.setId(1L);
        Saloon salon= service.updateSaloon(saloonDTO,user,salonId);

        SaloonDTO saloonDTO1= SalonMapper.mapToDTO(salon);
        return  new ResponseEntity<>(saloonDTO1,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SaloonDTO>> getSalons() {

        List<Saloon> saloons=service.getAllSalon();

        List<SaloonDTO>saloonDTO1=saloons.stream().map((salon)->{
            SaloonDTO saloonDTO=SalonMapper.mapToDTO(salon);
            return  saloonDTO;
        }).toList() ;
        return  new ResponseEntity<>(saloonDTO1,HttpStatus.OK);
    }
    @GetMapping("/{salonId}")
    public ResponseEntity<SaloonDTO> getSalonById(@PathVariable Long salonId) {


        Saloon salon= service.getSalonById(salonId);

        SaloonDTO saloonDTO1= SalonMapper.mapToDTO(salon);
        return  new ResponseEntity<>(saloonDTO1,HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SaloonDTO>> searchSalons(@RequestParam String city) {


        List<Saloon> saloons= service.searchSalonByCity(city);

        List<SaloonDTO>saloonDTO1=saloons.stream().map((salon)->{
            SaloonDTO saloonDTO=SalonMapper.mapToDTO(salon);
            return  saloonDTO;
        }).toList() ;
        return  new ResponseEntity<>(saloonDTO1,HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<SaloonDTO> getSalonByOwnerId() {
UserDTO userDTO=new UserDTO();
userDTO.setId(1L);

        Saloon saloons= service.getSalonByOwnerId(userDTO.getId());

        SaloonDTO saloonDTO=SalonMapper.mapToDTO(saloons);
        return  new ResponseEntity<>(saloonDTO,HttpStatus.OK);
    }



}
