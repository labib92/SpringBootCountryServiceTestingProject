package com.countryservice.demo;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.repositories.CountryRepository;
import com.countryservice.demo.services.CountryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = {ServiceMockitoTests.class})
public class ServiceMockitoTests {

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryService countryService;

    private List<Country> myCountries;

    @BeforeEach
    public void setup(){
        myCountries = new ArrayList<Country>();
        myCountries.add(new Country(1,"Russia", "Moscow"));
        myCountries.add(new Country(2,"USA","Washington"));
        myCountries.add(new Country(3, "UK", "London"));
    }

    @Test
    @Order(1)
    public void testGetAllCountries(){
        when(countryRepository.findAll()).thenReturn(myCountries); //Mocking
        Assertions.assertEquals(3, countryService.getAllCountries().size(),
                "Total countries should be = "+countryService.getAllCountries().size());
    }

    @Test
    @Order(2)
    public void testGetCountryById(){
        when(countryRepository.findAll()).thenReturn(myCountries); //Mocking
        int countryId = 1;
        Assertions.assertEquals(countryId, countryService.getCountryByID(countryId).getId(),
                "Country Id should be = "+ countryId);
    }

    @Test
    @Order(3)
    public void testGetCountryByName(){
        when(countryRepository.findAll()).thenReturn(myCountries); //Mocking
        String countryName = "Russia";
        Assertions.assertEquals(countryName,countryService.getCountryByName(countryName).getCountryName(),
                "Country name should be = "+ countryName);
    }

    @Test
    @Order(4)
    public void testAddNewCountry(){
        Country country = new Country(4, "Yemen","Sana'a");
        when(countryRepository.save(country)).thenReturn(country);
        Assertions.assertEquals(country,countryService.addNewCountry(country),
                "New added country should be = "+ country);
    }

    @Test
    @Order(5)
    public void testUpdateCountry(){
        Country country = new Country(4, "Japan", "Tokyo");
        when(countryRepository.save(country)).thenReturn(country);
        Assertions.assertEquals(country, countryService.updateCountry(country),
                "Updated country should be = "+country);
    }

    @Test
    @Order(6)
    public void testDeleteCountry(){
        Country country = new Country(4, "Japan", "Tokyo");
        countryService.deleteCountry(country.getId());
        verify(countryRepository ,times(1)).deleteById(country.getId()); //Assertion + Mock
    }
}
