package com.example.demo.basicClasses.entity;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


import javax.persistence.Entity;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailableSpecializationsInLocation {

    public enum SpecializationsInLocationsStatuses {AVAILABLE, NOT_AVAILABLE}

    @JsonIgnore
    private UUID id;
    @JsonIgnore
    private Specification specification;

    private Location location;
    private SpecializationsInLocationsStatuses status;

    public AvailableSpecializationsInLocation(UUID id, Specification specId, Location locId, SpecializationsInLocationsStatuses newStatus){
        this.id = id;
        specification = specId;
        location = locId;
        status = newStatus;
    }




    public UUID getId() {
        return id;
    }

    public Specification getSpecification() {
        return specification;
    }

    public Location getLocation() {
        return location;
    }

    public SpecializationsInLocationsStatuses getStatus() {
        return status;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setStatus(SpecializationsInLocationsStatuses status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableSpecializationsInLocation that = (AvailableSpecializationsInLocation) o;
        return id == that.id &&
                specification.equals(that.specification) &&
                location.equals(that.location) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, specification, location, status);
    }

    @Override
    public String toString() {
        return "AvailableSpecializationsInLocation{" +
                "uniqueId=" + id +
                ", specificationId=" + specification +
                ", locationId=" + location.toString() +
                ", status=" + status +
                '}';
    }

    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    @JsonGetter
    private UUID getSpecId(){
        return specification.getId();
    }

    @JsonSetter
    public static AvailableSpecializationsInLocation deserialize(String str)throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AvailableSpecializationsInLocation res = mapper.readValue(str, AvailableSpecializationsInLocation.class);
        return res;
    }
}
