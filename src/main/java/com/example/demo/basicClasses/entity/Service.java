package com.example.demo.basicClasses.entity;


import com.example.demo.basicClasses.Repo;
import com.example.demo.basicClasses.deserializers.ServiceDeserializer;
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
public class Service implements OrderService,  ObjectWithId {

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

    public void addAttribute(UUID id, AttributeValue value){
        params.put(id, value);
    }

    public void changeValue(UUID id, AttributeValue newVal){
        params.put(id, newVal);
    }

    public Map<UUID, AttributeValue> getParams() {
        return params;
    }

    public void setParams(Map<UUID, AttributeValue> params) {
        this.params = params;
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
    public ArrayList<AttributeValue> getAttributeValues(){
        ArrayList<AttributeValue> values = new ArrayList<>(params.values());
        return values;
    }
    @JsonSetter
    public void setAttributeValues(List<AttributeValue> values){
        params = new HashMap<>();
        for (int i =0; i < values.size(); ++i){
            params.put(values.get(i).getAttributeId(), values.get(i));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(id, service.id) &&
                Objects.equals(name, service.name) &&
                Objects.equals(description, service.description) &&
                status == service.status &&
                Objects.equals(specification, service.specification) &&
                Objects.equals(customer.getId(), service.customer.getId())&&
                Objects.equals(params, service.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, specification, customer, params);
    }


    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", specification=" + specification +
                ", customer=" + customer.getId().toString() +
                ", params=" + params +
                '}';
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
