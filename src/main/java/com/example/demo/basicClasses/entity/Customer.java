package com.example.demo.basicClasses.entity;


import com.example.demo.basicClasses.Repo;
import com.example.demo.basicClasses.deserializers.CustomerDeserializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.io.IOException;
import java.util.*;

@JsonPropertyOrder({"id","firstName","lastName","contactData", "services", "orders", "locationId"})
@Entity
//@JsonDeserialize(using = CustomerDeserializer.class)
@Table(name = "Customer")
@Access(AccessType.FIELD)
public class Customer implements ObjectWithId {

    @Id
    @Column(name = "id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;
    @Column(name = "first_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;
    @Column(name = "last_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

    @JoinColumn(name = "contact_data")
    @OneToOne(cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ContactData contactData;

    @JoinColumn(name = "location")
    @OneToOne
    //@Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    private Location location;

    @Column(name = "account_balance")
    private int accountBalance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Service> services;

    @JoinColumn
    @OneToOne
    @JsonIgnore
    private User user;

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

    public Customer(UUID id, String firstName, String lastName, ContactData contactData,
                    Location location, int accountBalance, List<Order> orders, List<Service> services) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactData = contactData;
        this.location = location;
        this.accountBalance = accountBalance;
        this.orders = orders;
        this.services = services;
    }

    public Customer(UUID id, String firstName, String lastName,
                    ContactData contactData, Location location, int accountBalance,
                    List<Order> orders, List<Service> services, User user) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactData = contactData;
        this.location = location;
        this.accountBalance = accountBalance;
        this.orders = orders;
        this.services = services;
        this.user = user;
    }

    public Customer(UUID id, String firstName, String lastName,
                    ContactData contactData, Location location, int accountBalance, User user) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactData = contactData;
        this.location = location;
        this.accountBalance = accountBalance;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonGetter
    public UUID getUserID() {
        return user.getId();
    }

    @JsonSetter
    public void setUserID(UUID uuid) {
        user = new User(uuid);
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public List<Service> getServices() {
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

   /* public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writerWithView(OrderAndServiceViews.WithoutCustomerID.class).writeValueAsString(this);
    }

    public static Customer deserialize(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(str, Customer.class);
        customer.orders = new ArrayList<>();
        customer.services = new ArrayList<>();
        for (int i = 0; i < customer.orders.size(); ++i){
            customer.orders.get(i).getService().setCustomer(customer);
            customer.orders.get(i).setSpecification(customer.orders.get(i).getService().getSpecification());
            customer.orders.get(i).setCustomer(customer);
            customer.services.get(i).setCustomer(customer);
        }
        for (int i =0; i < customer.services.size(); ++i) {
            for (Map.Entry<UUID, AttributeValue> entry : customer.getServices().get(i).getParams().entrySet()) {
                entry.setValue(normalizeAttributeValues(Specification.findAttributeById(entry.getKey()), entry.getValue()));
            }
        }
        for (int i =0; i < customer.orders.size(); ++i) {
            for (Map.Entry<UUID, AttributeValue> entry : customer.getOrders().get(i).getParams().entrySet()) {
                entry.setValue(normalizeAttributeValues(Specification.findAttributeById(entry.getKey()), entry.getValue()));
            }
        }
        if (customer.id == null) {
            customer.setId(UUID.randomUUID());
        }
        return customer;
    }*/




    @JsonGetter
    private UUID getLocationId(){
        return location.getId();
    }

    @JsonSetter
    public void setLocationId(UUID id){
        location =new Location(id);
    }


}
