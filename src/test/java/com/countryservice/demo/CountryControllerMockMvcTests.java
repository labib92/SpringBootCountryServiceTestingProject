package com.countryservice.demo;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.controllers.AddResponse;
import com.countryservice.demo.controllers.CountryController;
import com.countryservice.demo.services.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.countryservice.demo")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = {CountryControllerMockMvcTests.class})
public class CountryControllerMockMvcTests {

    @Autowired
    MockMvc mockMvc;

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
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
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
    public void testGetCountries() throws Exception {
        when(countryService.getAllCountries()).thenReturn(myCountries); //Mocking
        this.mockMvc.perform(get("/getcountries"))
                .andExpect(status().isFound())
                .andDo(print());
    }

    @Test
    @Order(2)
    public void testGetCountryById() throws Exception {
        when(countryService.getCountryByID(id)).thenReturn(country);
        this.mockMvc.perform(get("/getcountries/{id}",id)).
                andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath(".countryName").value(country.getCountryName()))
                .andExpect(MockMvcResultMatchers.jsonPath(".countryCapital").value(country.getCountryCapital()))
                .andDo(print());
    }

    @Test
    @Order(3)
    public void getCountryByName() throws Exception {
        String countryName = "Russia";
        when(countryService.getCountryByName(countryName)).thenReturn(country);
        this.mockMvc.perform(get("/getcountries/countryname").param("name",countryName))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath(".countryName").value(countryName))
                .andExpect(MockMvcResultMatchers.jsonPath(".countryCapital").value(country.getCountryCapital()))
                .andDo(print());
    }

    @Test
    @Order(4)
    public void testAddNewCountry() throws Exception {
        newCountry = new Country(4, "Yemen", "Sana'a");
        when(countryService.addNewCountry(newCountry)).thenReturn(newCountry);
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(newCountry);
        this.mockMvc.perform(post("/addcountry")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @Order(5)
    public void testUpdateCountry() throws Exception {
        newCountry = new Country(4, "Tokyo", "Japan");
        id = 4;
        when(countryService.getCountryByID(id)).thenReturn(newCountry);
        when(countryService.updateCountry(newCountry)).thenReturn(newCountry);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(newCountry);
        this.mockMvc.perform(put("/updatecountry/{id}", id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath(".countryName").value(newCountry.getCountryName()))
                .andExpect(MockMvcResultMatchers.jsonPath(".countryCapital").value(newCountry.getCountryCapital()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(6)
    public void testDeleteCountry() throws Exception {
        newCountry = new Country(4, "Tokyo", "Japan");
        id = 4;
        AddResponse addResponse = new AddResponse();
        addResponse.setMessage("Country deleted...");
        addResponse.setId(id);
        when(countryService.getCountryByID(id)).thenReturn(newCountry);
        when(countryService.deleteCountry(newCountry.getId())).thenReturn(addResponse);
        this.mockMvc.perform(delete("/deletecountry/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
