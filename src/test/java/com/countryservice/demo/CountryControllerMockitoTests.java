package com.countryservice.demo;


import com.countryservice.demo.beans.Country;
import com.countryservice.demo.controllers.AddResponse;
import com.countryservice.demo.controllers.CountryController;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = {CountryControllerMockitoTests.class})
public class CountryControllerMockitoTests {

    @Mock
    CountryService countryService;

    @InjectMocks
    CountryController countryController;

    private List<Country> myCountries;
    private Country country;
    private Country newCountry;
    private int id = 1;


    @BeforeEach
    public void setup(){
        myCountries = new ArrayList<Country>();
        myCountries.add(new Country(1,"Russia", "Moscow"));
        myCountries.add(new Country(2,"USA","Washington"));
        myCountries.add(new Country(3, "UK", "London"));
        country = getCountryId(id);
    }

    private Country getCountryId(int id){
        Country country = null;
        for(Country i : myCountries){
            if(i.getId() == id){
                country = i;
            }
        }
        return country;
    }

    @Test
    @Order(1)
    public void testGetCountries(){
        when(countryService.getAllCountries()).thenReturn(myCountries); //Mocking
        ResponseEntity<List<Country>> response = countryController.getCountries();
        Assertions.assertAll(() -> Assertions.assertEquals(HttpStatus.FOUND ,response.getStatusCode(),
                "Status code should be = "+HttpStatus.FOUND),
                () -> {
                    Assertions.assertNotNull(response.getBody(),
                            "Response body should not be NULL");
                    Assertions.assertEquals(3,response.getBody().size(),
                            "Response body size should be "+3);
                });
    }

    @Test
    @Order(2)
    public void testGetCountryById(){
        when(countryService.getCountryByID(id)).thenReturn(country);
        ResponseEntity<Country> response = countryController.getCountryById(id);
        Assertions.assertAll(() -> Assertions.assertEquals(HttpStatus.FOUND, response.getStatusCode(),
                "Status code should be = "+HttpStatus.FOUND),
                () -> {
                    Assertions.assertNotNull(response.getBody(),
                            "Response body should not be NULL");
                    Assertions.assertEquals(id, response.getBody().getId(),
                            "Country id should be = "+id);
                });
    }

    @Test
    @Order(3)
    public void testGetCountryByName(){
        String countryName = "Russia";
        when(countryService.getCountryByName(countryName)).thenReturn(country);
        ResponseEntity<Country> response = countryController.getCountryByName(countryName);
        Assertions.assertAll(() -> Assertions.assertEquals(HttpStatus.FOUND, response.getStatusCode(),
                "Status code should be = "+HttpStatus.FOUND),
                () -> {
                    Assertions.assertNotNull(response.getBody(),
                            "Response body should not be NULL");
                    Assertions.assertEquals(countryName, response.getBody().getCountryName(),
                            "Country name should be = "+countryName);
                });
    }

    @Test
    @Order(4)
    public void testAddNewCountry(){
        newCountry = new Country(4, "Yemen", "Sana'a");
        when(countryService.addNewCountry(newCountry)).thenReturn(newCountry);
        ResponseEntity<Country> response = countryController.addNewCountry(newCountry);
        Assertions.assertAll(() -> Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode(),"" +
                "Status code should be = "+HttpStatus.CREATED),
                () -> Assertions.assertEquals(newCountry, response.getBody(),
                        "New country should be = "+newCountry),
                () -> {
                    Assertions.assertNotNull(response.getBody(),
                            "Response body should not be NULL");
                    Assertions.assertEquals(newCountry.getCountryName(),response.getBody().getCountryName(),
                            "New country name should be "+newCountry.getCountryName());
                },
                () -> {
                    Assertions.assertNotNull(response.getBody(),
                            "Response body should not be NULL");
                    Assertions.assertEquals(newCountry.getCountryCapital(),response.getBody().getCountryCapital(),
                            "New country capital should be "+newCountry.getCountryCapital());
                });
    }

    @Test
    @Order(5)
    public void testUpdateCountry(){
        newCountry = new Country(4, "Tokyo", "Japan");
        id = 4;
        when(countryService.getCountryByID(id)).thenReturn(newCountry);
        when(countryService.updateCountry(newCountry)).thenReturn(newCountry);
        ResponseEntity<Country> response = countryController.updateCountry(id, newCountry);
        Assertions.assertAll(() -> Assertions.assertEquals(HttpStatus.OK,response.getStatusCode(),
                "Status code should be = "+HttpStatus.OK),
                () -> Assertions.assertEquals(newCountry, response.getBody(),
                        "Updated country should be = "+newCountry),
                () -> {
                    Assertions.assertNotNull(response.getBody(),
                            "Response body should not be NULL");
                    Assertions.assertEquals(newCountry.getCountryName(),response.getBody().getCountryName(),
                            "Updated country name should be "+newCountry.getCountryName());
                },
                () -> {
                    Assertions.assertNotNull(response.getBody(),
                            "Response body should not be NULL");
                    Assertions.assertEquals(newCountry.getCountryCapital(),response.getBody().getCountryCapital(),
                            "Updated country capital should be "+newCountry.getCountryCapital());
                });
    }

    @Test
    @Order(6)
    public void testDeleteCountry(){
        newCountry = new Country(4, "Tokyo", "Japan");
        id = 4;
        AddResponse addResponse = new AddResponse();
        addResponse.setMessage("Country deleted...");
        addResponse.setId(id);
        when(countryService.getCountryByID(id)).thenReturn(newCountry);
        when(countryService.deleteCountry(newCountry.getId())).thenReturn(addResponse);
        ResponseEntity<Country> response = countryController.deleteCountry(id);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Status code should be = "+HttpStatus.OK);
    }
}
