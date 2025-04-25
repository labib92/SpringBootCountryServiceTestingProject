package com.countryservice.demo.repositories;

import com.countryservice.demo.beans.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
