package com.example.demo.basicClasses.entity;


import com.example.demo.basicClasses.Repo;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.io.IOException;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","firstName","lastName","contactData", "services", "orders", "locationId"})
@Entity
@Table(name = "Customer")
public class Customer implements ObjectWithId {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "first name")
    private String firstName;
    @Column(name = "last name")
    private String lastName;

    @JoinColumn(name = "contact data")
    @OneToOne
    private ContactData contactData;

    @JoinColumn(name = "location")
    @OneToOne
    private Location location;

    @Column(name = "account balance")
    private int accountBalance;


    private ArrayList<Order> orders;


    private ArrayList<Service> services;

    public Customer(){
    }

    public Customer(UUID id){
        this.id=id;
    }

    public Customer(UUID id, String name1, String nameLast, ContactData data, Location locId, int balance){
        this.id = id;
        this.firstName = name1;
        lastName = nameLast;
        contactData=data;
        location =locId;
        accountBalance=balance;
        orders = new ArrayList<>(0);
        services = new ArrayList<>(0);
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public ContactData getContactData() {
        return contactData;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }
    @JsonIgnore
    public Location getLocation() {
        return location;
    }

    @JsonIgnore
    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "uniqueId=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactData='" + contactData + '\'' +
                ", location=" + location.toString() +
                ", accountBalance=" + accountBalance +
                ", orders=" + orders+
                ", services=" + services +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return accountBalance == customer.accountBalance &&
                Objects.equals(id, customer.id) &&
                Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(contactData, customer.contactData) &&
                Objects.equals(location, customer.location) &&
                Objects.equals(orders, customer.orders)&&
                Objects.equals(services, customer.services);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, contactData, location, accountBalance, orders, services);
    }

    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writerWithView(OrderAndServiceViews.WithoutCustomerID.class).writeValueAsString(this);
    }

    public static Customer deserialize(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(str, Customer.class);
        for (int i = 0; i < customer.orders.size(); ++i){
            customer.orders.get(i).getService().setCustomer(customer);
            customer.orders.get(i).setSpecification(customer.orders.get(i).getService().getSpecification());
            customer.orders.get(i).setCustomer(customer);
            customer.services.get(i).setCustomer(customer);
        }
        for (int i =0; i < customer.services.size(); ++i) {
            for (Map.Entry<Attribute, AttributeValue> entry : customer.getServices().get(i).getParams().entrySet()) {
                entry.setValue(normalizeAttributeValues(entry.getKey(), entry.getValue()));
            }
        }
        for (int i =0; i < customer.orders.size(); ++i) {
            for (Map.Entry<Attribute, AttributeValue> entry : customer.getOrders().get(i).getParams().entrySet()) {
                entry.setValue(normalizeAttributeValues(entry.getKey(), entry.getValue()));
            }
        }
        return customer;
    }

    private static AttributeValue normalizeAttributeValues(Attribute key, AttributeValue value){

        String val;
        switch (key.getType()) {
            case DATE:
                val = value.getValue();
                value = new AttributeValue();
                value.setType(Attribute.AttributeTypes.DATE);
                value.setValue(val);
                break;
            case NUMBER:
                val = value.getValue();
                value = new AttributeValue();
                value.setType(Attribute.AttributeTypes.NUMBER);
                value.setValue(val);
                break;
            case STRING:
                value.setType(Attribute.AttributeTypes.STRING);
        }
        return value;
    }


    @JsonGetter
    private UUID getLocationId(){
        return location.getId();
    }

    @JsonSetter
    public void setLocationId(UUID id){
        Repo r = Repo.getInstance();
        if (r!=null&&r.getLocations()!=null) {
            for (int i = 0; i < r.getLocations().size(); ++i) {
                if (id.equals(r.getLocations().get(i).getId())) {
                    location = r.getLocations().get(i);
                }
            }
        }
        if (location==null)   {
            location = new Location(id, null, null);
        }
    }


}
