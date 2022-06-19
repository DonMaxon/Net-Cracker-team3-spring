package com.example.demo.basicClasses.entity;


import com.example.demo.basicClasses.deserializers.LocationDeserializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


import javax.persistence.*;
import java.io.IOException;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@JsonDeserialize(using = LocationDeserializer.class)
@Table(name = "Location")
@Access(AccessType.FIELD)
public class Location implements ObjectWithId {

    public enum Types{
        COUNTRY(1), STATE(2), CITY(3), ADDRESS(4);
        private int value;
        private Types(int i){
            this.value = i;
        }

        public int getVal(){
            return value;
        }

        public static Types returnType(int val) {
            switch (val) {
                case 1:
                    return COUNTRY;
                case 2:
                    return STATE;
                case 3:
                    return CITY;
                case 4:
                    return ADDRESS;
            }
            return null;
        }
    }

    @Id
    //@JsonView(Views.firstSerialize.class)
    @JsonView(LocationViews.LocationWithoutParent.class)
    private UUID id;
    @JsonView(LocationViews.LocationWithoutParent.class)
    @Column(name = "name")
    private String name;
    @JsonView(LocationViews.LocationWithoutParent.class)
    @Column(name = "type")
    private Types type;
    @JsonView(LocationViews.LocationWithParent.class)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Location parent;

    @JsonView(LocationViews.LocationWithChildren.class)
    @OneToMany(targetEntity = Location.class, mappedBy = "parent")
    private List<Location> children;

    public Location(){
        children = new ArrayList<>();
    }

    public Location(UUID id) {
        this.id = id;
        children = new ArrayList<>();
    }

    public Location(String str){
        this.id = UUID.fromString(str);
        children = new ArrayList<>();
    }

    public Location(String name, Types t){
        this.id = UUID.randomUUID();
        this.name=name;
        type=t;
        children = new ArrayList<>();
    }

    public Location(UUID id, String name, Types t){
        this.id = id;
        this.name=name;
        type=t;
        children = new ArrayList<>();
    }

    public Location(UUID id, String name, Types t, Location loc){
        this.id = id;
        this.name=name;
        type=t;
        parent = loc;
        children = new ArrayList<>();
    }

    public Location(String name, Types type, Location parent) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.parent = parent;
        children = new ArrayList<>();
        parent.children.add(this);
    }

    public Location(UUID id, String name, Types type, Location parent, List<Location> children) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.children = children;
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

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    @JsonIgnore
    public Location getParent() {
        return parent;
    }

    @JsonIgnore
    public void setParent(Location parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public List<Location> getChildren(){
        return children;
    }

    @JsonIgnore
    public void setChildren(List<Location> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) &&
                Objects.equals(name, location.name) &&
                type == location.type &&
                Objects.equals(parent, location.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, parent);
    }

    @Override
    public String toString() {
        if (parent==null){
            return "Location{" +
                    "uniqueId=" + id +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
        else{
            return "Location{" +
                    "uniqueId=" + id +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    ", parentLoc=" + parent.toString() +
                    '}';
        }

    }


    public String serialize() throws JsonProcessingException{
        if (parent==null){
            StringBuilder resBuilder = new StringBuilder();
            resBuilder.append(this.simpleSerializeWithoutID());
            resBuilder.append(writeToStr()).toString();
            return resBuilder.toString();
        }
        else{
            return parent.serialize();
        }
    }


    public String simpleSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper
                .writerWithView(LocationViews.LocationWithParent.class)
                .writeValueAsString(this);
        return result;

    }

    public static Location simpleDeserialize(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Location location = mapper
                .readerWithView(LocationViews.LocationWithParent.class)
                .forType(Location.class)
                .readValue(str);
        if (location.id == null) {
            location.setId(UUID.randomUUID());
        }
        return location;
    }

    public static ArrayList<Location> deserialize(String str, Location parent)throws IOException{
        ArrayList<Location> locations = new ArrayList<>(0);
        if (!str.equals("")){
            int ind = 0;
            while (!str.equals("")) {
                locations.add(Location.simpleDeserializeWithoutID(str));
                locations.get(locations.size()-1).setParent(parent);
                locations.addAll(deserialize(str.substring(str.indexOf('[')+1, findBracket(str)),
                        locations.get(locations.size()-1)));
                str = str.substring(findBracket(str)+1);
            }

        }

        return locations;
    }

    public boolean isBelongsTo(Location location){
        try {
            if (this.equals(location)
                    || (this.getParent().equals(location))
                    || (this.parent.parent.equals(location))
                    || this.parent.parent.parent.equals(location)) {
                return true;
            }
        }
        catch (NullPointerException e){
        }
        return false;
    }

    private static int findBracket(String str){
        int c = 0;
        boolean wasBracket = false;
        for (int i = 0; i < str.length(); ++i){
            if (str.charAt(i)=='['){
                c+=1;
                wasBracket = true;
            }
            if (str.charAt(i)==']'){
                c-=1;
            }
            if (c==0 && wasBracket){
                return i;
            }
        }
        return str.length()-1;
    }

    private String writeToStr()throws JsonProcessingException{
        ArrayList<Location> children = new ArrayList<Location>(0);
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append('[');
        /*Repo repo = Repo.getInstance();
        for (int i = 0; i<repo.getLocations().size(); ++i){
            if (repo.getLocations().get(i).parent!=null &&
                    repo.getLocations().get(i).parent.equals(this)){
                children.add(repo.getLocations().get(i));
            }
        }*/ //TODO FIX THAT!!
        for (int i = 0; i < children.size(); ++i){
            resBuilder.append(children.get(i).simpleSerializeWithoutID());
            resBuilder.append(children.get(i).writeToStr());
        }
        resBuilder.append(']');
        return resBuilder.toString();
    }



    @JsonGetter
    @JsonView(LocationViews.LocationWithParent.class)
    public UUID getParentID(){
        if (parent!=null) {
            return this.parent.id;
        }
        else {
            return null;
        }
    }


    @JsonSetter
    @JsonView(LocationViews.LocationWithParent.class)
    public void setParentID(UUID id){
        parent=new Location(id);
    }

    private String simpleSerializeWithoutID() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper
                .writerWithView(LocationViews.LocationWithoutParent.class)
                .writeValueAsString(this);
        return result;
    }

    private static Location simpleDeserializeWithoutID(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Location location = mapper
                .readerWithView(LocationViews.LocationWithoutParent.class)
                .forType(Location.class)
                .readValue(str);
        return location;
    }

    public static class LocationViews {
        public static class LocationWithoutParent {

        }

        public static class LocationWithParent extends LocationWithoutParent {

        }

        public static class LocationWithChildren extends  LocationWithoutParent{

        }
    }
}
