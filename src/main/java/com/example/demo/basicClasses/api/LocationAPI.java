package com.example.demo.basicClasses.api;



import com.example.demo.basicClasses.api.exceptions.WrongCountryException;
import com.example.demo.basicClasses.entity.Location;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocationAPI {
    private static LocationAPI instance;


    public Location createLocation(String name){
        return new Location(UUID.randomUUID(), name, Location.Types.COUNTRY, null);
    }

    public Location createLocation(String name, Location parent) throws WrongCountryException {
        if (parent.getType().getVal()==4){
            throw new WrongCountryException("createLocation");
        }
        else{
            return new Location(UUID.randomUUID(), name, Location.Types.returnType(parent.getType().getVal()+1), parent);
        }
    }
}

