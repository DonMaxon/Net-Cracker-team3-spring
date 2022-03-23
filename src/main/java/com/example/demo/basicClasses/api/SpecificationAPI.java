package com.example.demo.basicClasses.api;


import com.example.demo.basicClasses.Repo;
import com.example.demo.basicClasses.api.exceptions.CreatingException;
import com.example.demo.basicClasses.entity.AvailableSpecializationsInLocation;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.entity.Specification;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.UUID;

@Service
public class SpecificationAPI {

    private static SpecificationAPI instance;

    public Specification createSpecification(String name){
        return new Specification(UUID.randomUUID(), name);
    }

    public Specification createSpecification(String name, ArrayList<Location> locations){
        Specification specification = createSpecification(name);
        if (!isUniqueName(name)){
            throw new CreatingException("Error during creating specification, not unique name");
        }
        ArrayList<AvailableSpecializationsInLocation> availableSpecializationsInLocations = new ArrayList<>(0);
        for (int i =0; i< locations.size(); ++i){
            specification.getAvailableLocations().add(locations.get(i));
        }
        return specification;
    }

    private boolean isUniqueName(String name){
        Repo repo = Repo.getInstance();
        for (int i =0; i < repo.getSpecs().size();++i){
            if (name.equals(repo.getSpecs().get(i).getName())){
                return false;
            }
        }
        return true;
    }
}
