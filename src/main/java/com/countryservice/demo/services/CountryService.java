package com.countryservice.demo.services;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.controllers.AddResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CountryService {
    static Map<Integer, Country> countryIdMap;

    public CountryService(){
        countryIdMap = new HashMap<Integer, Country>();

        Country russiaCountry = new Country(1,"Russia", "Moscow");
        Country usaCountry = new Country(2,"USA","Washington");
        Country ukCountry =  new Country(3, "UK", "London");

        countryIdMap.put(1, russiaCountry);
        countryIdMap.put(2, usaCountry);
        countryIdMap.put(3, ukCountry);
    }

    private static int getMaxId(){
        int max = 0;
        for(int i : countryIdMap.keySet()){
            if(max <= i){
                max = i;
            }
        }
        return max + 1;
    }

    public List<Country> getAllCountries(){
        return new ArrayList<Country>(countryIdMap.values());
    }

    public Country getCountryByID(int id){
        return countryIdMap.get(id);
    }

    public Country getCountryByName(String countryName){
        Country country = null;
        for(int i : countryIdMap.keySet()){
            if(countryIdMap.get(i).getCountryName().equals(countryName)){
                country = countryIdMap.get(i);
            }
        }
        return country;
    }

    public Country addNewCountry(Country country){
        country.setId(getMaxId());
        countryIdMap.put(country.getId(), country);
        return country;
    }

    public Country updateCountry(Country country){
        if(country.getId() > 0){
            countryIdMap.put(country.getId(), country);
        }
        return country;
    }

    public AddResponse deleteCountry(int id){
        countryIdMap.remove(id);
        AddResponse response = new AddResponse();
        response.setMessage("Country deleted...");
        response.setId(id);
        return response;
    }
}
