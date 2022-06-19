package com.example.demo.basicClasses.entity;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonDeserialize(using = ServiceDeserializer.class)
@Entity
@Table(name = "service")
public class Service implements  ObjectWithId {

    public List<AttributeValue> getValues() {
        return values;
    }

    public void setValues(List<AttributeValue> values) {
        this.values = values;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public enum ServiceStatus  {PLANNED, ACTIVE, DISCONNECTED};


    @Id
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    private UUID id;
    @Column(name = "name")
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    private String name;
    @Column(name = "description")
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    private String description;
    @Column(name = "status")
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    private ServiceStatus status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "specification_id")
    private Specification specification;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @JsonDeserialize(keyUsing = Attribute.AttributeDeserializer.class)
    @JoinColumn(name = "parameters")
    @OneToMany
    @Transient
    @JsonIgnore
    private Map<UUID, AttributeValue> params;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service")
    private List<AttributeValue> values;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service")
    private List<Order> orders;


    public Service() {
        params = new HashMap<>();
    }



    public Service(UUID id){
        this.id = id;
        params = new HashMap<>();
    }

    public Service(UUID id, String name, String description, ServiceStatus status, Specification specification, Customer customer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.specification = specification;
        this.customer = customer;
        params = new HashMap<>();
    }

    public Service(UUID id, String name, String description, ServiceStatus status, Specification specId, Customer customer, Map<UUID, AttributeValue> params) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.specification = specId;
        this.customer = customer;
        this.params=params;
    }

    public Service(UUID id, String name, ServiceStatus status, Specification specId, Customer customer) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.specification = specId;
        this.customer = customer;
        params = new HashMap<>();
    }

    public Map<UUID, AttributeValue> getParams() {
        Map<UUID, AttributeValue> params = new HashMap<>();
        for(AttributeValue value : values){
            params.put(value.getAttributeId(), value);
        }
        this.params = params;
        return params;
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

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonGetter
    public List<AttributeValue> getAttributeValues(){
        return values;
    }
    @JsonSetter
    public void setAttributeValues(List<AttributeValue> values){
        this.values = values;
        params = new HashMap<>();
        for(AttributeValue value : values){
            params.put(value.getAttributeId(), value);
        }
    }

    public String serialize() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static Service deserialize(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Service service = mapper.readValue(str, Service.class);

        return service;
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    public UUID getSpecificationID(){
        return specification.getId();
    }

    public void setSpecificationID(UUID specID){
        specification=new Specification(specID);
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithCustomerID.class)
    public UUID getCustomerID(){
        return customer.getId();
    }

    public void setCustomerID(UUID specID){
        customer=new Customer(specID);
    }



}
