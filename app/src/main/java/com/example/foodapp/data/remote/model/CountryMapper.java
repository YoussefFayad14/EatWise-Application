package com.example.foodapp.data.remote.model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public final class CountryMapper {
    private static final Map<String, String> countryMapping = new HashMap<>();

    static {
        countryMapping.put("United States", "American");
        countryMapping.put("United Kingdom", "British");
        countryMapping.put("Canada", "Canadian");
        countryMapping.put("China", "Chinese");
        countryMapping.put("Croatia", "Croatian");
        countryMapping.put("Netherlands", "Dutch");
        countryMapping.put("Egypt", "Egyptian");
        countryMapping.put("Philippines", "Filipino");
        countryMapping.put("France", "French");
        countryMapping.put("Greece", "Greek");
        countryMapping.put("India", "Indian");
        countryMapping.put("Ireland", "Irish");
        countryMapping.put("Italy", "Italian");
        countryMapping.put("Jamaica", "Jamaican");
        countryMapping.put("Japan", "Japanese");
        countryMapping.put("Kenya", "Kenyan");
        countryMapping.put("Malaysia", "Malaysian");
        countryMapping.put("Mexico", "Mexican");
        countryMapping.put("Morocco", "Moroccan");
        countryMapping.put("Poland", "Polish");
        countryMapping.put("Portugal", "Portuguese");
        countryMapping.put("Russia", "Russian");
        countryMapping.put("Spain", "Spanish");
        countryMapping.put("Thailand", "Thai");
        countryMapping.put("Tunisia", "Tunisian");
        countryMapping.put("Turkey", "Turkish");
        countryMapping.put("Ukraine", "Ukrainian");
        countryMapping.put("Vietnam", "Vietnamese");
    }

    private CountryMapper() {}


    public static String getMappedCountry(String country) {
        return countryMapping.getOrDefault(country, "Unknown");
    }

    public static String getOriginalCountry(String mappedCountry) {
        for (Map.Entry<String, String> entry : countryMapping.entrySet()) {
            if (entry.getValue().equals(mappedCountry)) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }
    public static List<Area> getCountriesByMappedValues(List<Area> mappedCountries) {
        List<Area> matchingCountries = new ArrayList<>();

        for (Area mappedCountry : mappedCountries) {
            for (Map.Entry<String, String> entry : countryMapping.entrySet()) {
                if (entry.getValue().equals(mappedCountry.getName())) {
                    matchingCountries.add(new Area(entry.getKey()));
                }
            }
        }
        return matchingCountries;
    }

}
