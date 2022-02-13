package com.example.demo.basicClasses.entity;


import com.example.demo.basicClasses.Repo;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "service")
public class Service implements OrderService,  ObjectWithId {

    public enum ServiceStatus  {PLANNED, ACTIVE, DISCONNECTED};

    @Id
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private ServiceStatus status;
    @JsonIgnore
    @Column(name = "specification id")
    private Specification specification;

    @JsonIgnore
    @Column(name = "customer id")
    private Customer customer;


    @JsonDeserialize(keyUsing = Attribute.AttributeDeserializer.class)
    private Map<Attribute, AttributeValue> params;

    public Service() {
    }

    public Service(UUID id){
        this.id = id;
        params = new HashMap<>();
    }


    public Service(UUID id, String name, String description, ServiceStatus status, Specification specId, Customer customer, Map<Attribute, AttributeValue> params) {
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

    public Map<Attribute, AttributeValue> getParams() {
        return params;
    }

    public void setParams(Map<Attribute, AttributeValue> params) {
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
        for(Map.Entry<Attribute, AttributeValue> entry: service.getParams().entrySet()){
            String val;
            switch(entry.getKey().getType()){
                case DATE: val = entry.getValue().getValue();
                    entry.setValue(new AttributeValue());
                    entry.getValue().setType(Attribute.AttributeTypes.DATE);
                    entry.getValue().setValue(val);
                    break;
                case NUMBER: val = entry.getValue().getValue();
                    entry.setValue(new AttributeValue());
                    entry.getValue().setType(Attribute.AttributeTypes.NUMBER);
                    entry.getValue().setValue(val);
                    break;
                case STRING:
                    entry.getValue().setType(Attribute.AttributeTypes.STRING);
            }
        }
        return service;
    }

    @JsonGetter
    private UUID getSpecificationID(){
        return specification.getId();
    }

    @JsonSetter
    private void setSpecificationID(UUID specID){
        Repo repo = Repo.getInstance();
        if (repo!=null&&repo.getSpecs()!=null) {
            for (int i = 0; i < repo.getSpecs().size(); ++i) {
                if (repo.getSpecs().get(i).getId() != null &&
                        repo.getSpecs().get(i).getId().equals(specID))
                    specification = repo.getSpecs().get(i);
                return;
            }
        }
        specification=new Specification(id);
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithCustomerID.class)
    private UUID getCustomerID(){
        return customer.getId();
    }

    @JsonSetter
    @JsonView(OrderAndServiceViews.WithCustomerID.class)
    private void setCustomerID(UUID specID){
        Repo repo = Repo.getInstance();
        if (repo!=null&&repo.getCustomers()!=null) {
            for (int i = 0; i < repo.getCustomers().size(); ++i) {
                if (repo.getCustomers().get(i).getId() != null &&
                        repo.getCustomers().get(i).getId().equals(specID))
                    customer = repo.getCustomers().get(i);
                return;
            }
        }
        customer=new Customer(specID);
    }

}
