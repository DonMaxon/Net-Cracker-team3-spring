package com.example.demo.basicClasses.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity

@Table(name="specification")
public class Specification implements ObjectWithId {

    @Id
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Transient
    private List<Attribute> attributes;
    @Transient
    private List<Location> availableLocations;

    @JsonIgnore
    public List<Location> getAvailableLocations() {
        return availableLocations;
    }

    @JsonIgnore
    public void setAvailableLocations(List<Location> availableLocations) {
        this.availableLocations = availableLocations;
    }

    public Specification() {
    }

    public Specification(UUID id){
        this.id = id;
        attributes = new ArrayList<>(0);
        availableLocations = new ArrayList<>(0);
    }

    public Specification(UUID id, String name){
        this.id = id;
        this.name=name;
        attributes = new ArrayList<>(0);
        availableLocations = new ArrayList<>(0);
    }

    public Specification(UUID id, String name, String desc){
        this.id = id;
        this.name=name;
        description=desc;
        attributes = new ArrayList<>(0);
        availableLocations = new ArrayList<>(0);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specification that = (Specification) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(attributes, that.attributes) &&
                Objects.equals(availableLocations, that.availableLocations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, attributes, availableLocations);
    }

    @Override
    public String toString() {
        return "Specification{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", attributes=" + attributes +
                ", availableLocations=" + availableLocations +
                '}';
    }

    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static Specification deserialize(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Specification spec = mapper.readValue(str, Specification.class);
        for (int i =0; i <spec.attributes.size(); ++i) {
            spec.attributes.get(i).setSpecification(spec);
        }
        return spec;
    }

    public void addAttribute(String name, boolean isMandatory, Attribute.AttributeTypes type)throws InstantiationException{
        if (!isCorrectName(name)){
            throw new InstantiationException("Error during adding attribute, name is not correct");
        }
        Attribute attribute = AttributeFactory.createAttribute(name, isMandatory, type);
        attribute.setSpecification(this);
        attributes.add(attribute);

    }

    public List<Attribute> getMandatoryAttribute(){
        List<Attribute> mandatoryAttributes = new ArrayList<>(0);
        for (int i = 0; i < attributes.size(); ++i){
            if (attributes.get(i).isMandatory()) {
                mandatoryAttributes.add(attributes.get(i));
            }
        }
        return mandatoryAttributes;
    }

    @JsonGetter
    private ArrayList<UUID> getAvailableLocationId(){
        ArrayList<UUID> availableLocationIds = new ArrayList<>(0);
        for (int i = 0; i < availableLocations.size(); ++i){
            availableLocationIds.add(availableLocations.get(i).getId());
        }
        return availableLocationIds;
    }

    @JsonSetter
    private void setAvailableLocationId(ArrayList<Location> locations) {
        availableLocations=locations;
    }



    private boolean isCorrectName(String name){
        for (int i =0; i < this.getAttributes().size();++i){
            if (name.equals(this.getAttributes().get(i).getName())){
                return false;
            }
        }
        return true;
    }

    public static class AttributeFactory {
        private static AttributeFactory instance;

        private AttributeFactory(){

        }

        public static AttributeFactory getInstance(){
            if (instance==null){
                return new AttributeFactory();
            }
            else{
                return instance;
            }
        }

        public static Attribute createAttribute(String name, boolean isMandatory, Attribute.AttributeTypes type){
            return new Attribute(UUID.randomUUID(), name, isMandatory, type);
        }

        public static Attribute createAttributeString(String name, boolean isMandatory){
            return new Attribute(UUID.randomUUID(), name, isMandatory, Attribute.AttributeTypes.STRING);
        }
        public static Attribute createAttributeNumber(String name, boolean isMandatory){
            return new Attribute(UUID.randomUUID(), name, isMandatory, Attribute.AttributeTypes.NUMBER);
        }
        public static Attribute createAttributeDate(String name, boolean isMandatory){
            return new Attribute(UUID.randomUUID(), name, isMandatory, Attribute.AttributeTypes.DATE);
        }

    }

}
