package com.example.demo.basicClasses;


import com.example.demo.basicClasses.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@JsonIgnoreProperties({"allOrders", "allServices"})
public  class Repo {
    private static Repo instance;

    private  ArrayList<Customer> customers;
    private  ArrayList<Location> locations;
    private  ArrayList<Specification> specs;

    private Repo(){
        customers = new ArrayList<>(0);
        locations = new ArrayList<>(0);
        specs = new ArrayList<>(0);


        locations.add(new Location(UUID.randomUUID(), "ru", Location.Types.COUNTRY, null));
        locations.add(new Location(UUID.randomUUID(), "pfo", Location.Types.STATE, locations.get(0)));
        locations.add(new Location(UUID.randomUUID(), "smr", Location.Types.CITY, locations.get(1)));
        locations.add(new Location(UUID.randomUUID(), "dfo", Location.Types.STATE, locations.get(0)));

        customers.add(new Customer(UUID.randomUUID(), "max", "custo",
                new ContactData(UUID.randomUUID(), "89873332211", "data@mail.ru"),
                locations.get(0), 0));
        specs.add(new Specification(UUID.randomUUID(), "Spec", "No desc"));

        customers.get(0).getServices().add(new Service(UUID.randomUUID(), "service", "no desc",
                Service.ServiceStatus.PLANNED, specs.get(0), customers.get(0), new HashMap<>()));
        customers.get(0).getOrders().add(new Order(UUID.randomUUID(), "1st order",customers.get(0).getServices().get(0),
                specs.get(0), customers.get(0), Order.OrderStatus.ENTERING, Order.OrderAIM.NEW));

        specs.get(0).setAttributes(new ArrayList<>(0));
        /*specs.get(0).getAttributes().add(new Attribute(UUID.randomUUID(),
                false, Attribute.AttributeTypes.STRING, "Speed", specs.get(0)));
        specs.get(0).getAttributes().add(new Attribute(UUID.randomUUID(),
                true, Attribute.AttributeTypes.NUMBER, "Rate", specs.get(0)));*/
        AttributeValue value = new AttributeValue();
        value.setValue("2");
        customers.get(0).getServices().get(0).getParams().put(
                specs.get(0).getAttributes().get(1).getId(),
                value);
        customers.get(0).getOrders().get(0).getParams().put(specs.get(0).getAttributes().get(1).getId(), value);
    }

    private Repo(ArrayList<Customer> customers, ArrayList<Location> locations, ArrayList<Specification> specs){
        this.customers=customers;
        this.locations=locations;
        this.specs=specs;
    }

    public static Repo getInstance() {
        if (instance==null){
            return getDefaultInstance();
        }
        return instance;
    }

    public static Repo getDefaultInstance(){
        instance = new Repo();
        return instance;
    }



    public static Repo setInstance(ArrayList<Customer> customers, ArrayList<Location> locations, ArrayList<Specification> specs) {
        return new Repo(customers, locations, specs);
    }


    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public ArrayList<Specification> getSpecs() {
        return specs;
    }

    public void setSpecs(ArrayList<Specification> specs) {
        this.specs = specs;
    }


    public String serialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static Repo deserialize(String str)throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Repo res = mapper.readValue(str, Repo.class);
        for (int i = 0; i<res.getCustomers().size(); ++i){
            if (findById(res.locations, res.customers.get(i).getLocation().getId())!=null){
                res.customers.get(i).setLocation((Location)findById(res.locations, res.customers.get(i).getLocation().getId()));
            }
            for (int j = 0; j < res.customers.get(i).getOrders().size(); ++j){
                if (findById(res.getAllServices(), res.customers.get(i).getOrders().get(j).getService().getId())!=null){
                    res.customers.get(i).getOrders().get(j).setService(
                            (Service)findById(res.getAllServices(), res.customers.get(i).getOrders().get(j).getService().getId()));
                }
                for (Map.Entry<UUID, AttributeValue> entry:
                        res.customers.get(i).getOrders().get(j).getParams().entrySet())
                if (findById(res.getSpecs(), Specification.findAttributeById(entry.getKey()).getSpecification().getId())!=null){
                    Specification.findAttributeById(entry.getKey()).setSpecification((Specification)findById(res.getSpecs(), Specification.findAttributeById(entry.getKey()).getSpecification().getId()));
                }
            }
            for (int j = 0; j < res.customers.get(i).getServices().size(); ++j){
                for (Map.Entry<UUID, AttributeValue> entry:
                        res.customers.get(i).getServices().get(j).getParams().entrySet())
                    if (findById(res.getSpecs(), Specification.findAttributeById(entry.getKey()).getSpecification().getId())!=null){
                        Specification.findAttributeById(entry.getKey()).setSpecification((Specification)findById(res.getSpecs(), Specification.findAttributeById(entry.getKey()).getSpecification().getId()));
                    }
            }
        }
        for (int i = 0; i<res.getSpecs().size(); ++i) {
            for (int j = 0;
                    j<res.specs.get(i).getAttributes().size(); ++j){
                res.specs.get(i).getAttributes().get(j).setSpecification(res.specs.get(i));
            }
        }
        instance = res;
        return res;
    }

    private static ObjectWithId findById(ArrayList<? extends ObjectWithId> objects, UUID id){
        for (int i = 0; i<objects.size();++i){
            if (objects.get(i).getId().equals(id)){
                return objects.get(i);
            }
        }
        return null;
    }


    public ArrayList<Service> getAllServices(){

        ArrayList<Service> res = new ArrayList<>(0);

        for (int i = 0; i < customers.size(); ++i){
            res.addAll(customers.get(i).getServices());
        }
        return res;
    }


    public ArrayList<Order> getAllOrders(){
        ArrayList<Order> res = new ArrayList<>(0);

        for (int i = 0; i < customers.size(); ++i){
            res.addAll(customers.get(i).getOrders());
        }
        return res;
    }





}


