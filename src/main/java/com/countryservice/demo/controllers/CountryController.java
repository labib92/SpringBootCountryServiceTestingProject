package com.countryservice.demo.controllers;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping("/getcountries")
    public ResponseEntity<List<Country>> getCountries(){
        try {
            List<Country> countries = countryService.getAllCountries();
            return new ResponseEntity<List<Country>>(countries, HttpStatus.FOUND);
        }catch (Exception e){
            return new ResponseEntity<List<Country>>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getcountries/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable(value = "id") int id){
        try {
            Country country = countryService.getCountryByID(id);
            return new ResponseEntity<Country>(country, HttpStatus.FOUND);
        }catch (Exception e){
            return new ResponseEntity<Country>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getcountries/countryname")
    public ResponseEntity<Country> getCountryByName(@RequestParam(value = "name") String countryName){
        try {
            Country country = countryService.getCountryByName(countryName);
            return new ResponseEntity<Country>(country, HttpStatus.FOUND);
        }catch (Exception e){
            return new ResponseEntity<Country>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addcountry")
    public ResponseEntity<Country> addNewCountry(@RequestBody Country country){
        try {
            country = countryService.addNewCountry(country);
            return new ResponseEntity<Country>(country, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Country>(country, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/updatecountry/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") int id, @RequestBody Country country){
        try {
            Country existCountry = countryService.getCountryByID(id);
            existCountry.setCountryName(country.getCountryName());
            existCountry.setCountryCapital(country.getCountryCapital());
            Country updatedCountry = countryService.updateCountry(existCountry);
            return new ResponseEntity<Country>(updatedCountry, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Country>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/deletecountry/{id}")
    public ResponseEntity<AddResponse> deleteCountry(@PathVariable(value = "id") int id){
        Country country;
        AddResponse response;
        try {
            country = countryService.getCountryByID(id);
            response = countryService.deleteCountry(country.getId());
        }catch (NoSuchElementException e){
            return new ResponseEntity<AddResponse>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AddResponse>(response , HttpStatus.OK);
    }

}
