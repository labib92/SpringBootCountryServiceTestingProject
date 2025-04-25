package com.countryservice.demo.services;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.controllers.AddResponse;
import com.countryservice.demo.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class CountryService {

    @Autowired
    CountryRepository countryRepository;


    private int getMaxId(){
        return countryRepository.findAll().size() + 1;
    }


    public List<Country> getAllCountries(){
       return countryRepository.findAll();
    }

    public Country getCountryByID(int id){
       return countryRepository.findById(id).get();
    }

    public Country getCountryByName(String countryName){
        List<Country> countries = countryRepository.findAll();
        Country country = null;
        for (Country i : countries) {
            if (i.getCountryName().equalsIgnoreCase(countryName)) {
                country = i;
            }
        }
        return country;
    }

    public Country addNewCountry(Country country){
        country.setId(getMaxId());
        countryRepository.save(country);
        return country;
    }

    public Country updateCountry(Country country){
        countryRepository.save(country);
        return country;
    }

    public AddResponse deleteCountry(int id){
        countryRepository.deleteById(id);
        AddResponse response = new AddResponse();
        response.setMessage("Country deleted...");
        response.setId(id);
        return response;
    }
}
