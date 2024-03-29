package com.example.demo.basicClasses.entity;


import com.example.demo.basicClasses.deserializers.OrderDeserializer;
import com.example.demo.basicClasses.entity.exceptions.OrderException;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


import javax.persistence.*;
import java.io.IOException;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = OrderDeserializer.class)
@Entity
@Table(name = "Orders")
public class Order implements ObjectWithId {

    public enum OrderStatus  {ENTERING, IN_PROGRESS, COMPLETED};
    public enum OrderAIM{NEW, MODIFY, DISCONNECT};

    @Id
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    private UUID id;
    @Column(name = "name")
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    private String name;
    @Column(name = "description")
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "specification_id")
    private Specification specification;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "aim")
    private OrderAIM aim;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<AttributeValue> values;

    @JsonDeserialize(keyUsing = Attribute.AttributeDeserializer.class)
    @Transient
    @JsonIgnore
    private Map<UUID, AttributeValue> params;


    public Order() {
        params = new HashMap<>();
    }



    public Order(UUID id){
        this.id = id;
        status = OrderStatus.ENTERING;
        params = new HashMap<>();
    }


    public Order(UUID id, String name, Service serviceId, Specification specification, Customer customer, OrderStatus status, OrderAIM aim) {
        this.id = id;
        this.name = name;
        this.service = serviceId;
        this.specification = specification;
        this.customer = customer;
        this.status = status;
        this.aim = aim;
        params = new HashMap<>();
    }

    public Order(UUID id, String name, String description, Service service, Specification specification, Customer customer, OrderStatus status, OrderAIM aim) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.service = service;
        this.specification = specification;
        this.customer = customer;
        this.status = status;
        this.aim = aim;
        params = new HashMap<>();
    }

    public Order(UUID id, String name, String prescription, Service serviceId,
                 Specification specification, Customer customer, OrderStatus status, OrderAIM aim, Map<UUID, AttributeValue> params) {
        this.id = id;
        this.name = name;
        this.description = prescription;
        this.service = serviceId;
        this.specification = specification;
        this.customer = customer;
        this.status = status;
        this.aim = aim;
        this.params=params;
    }

    @JsonIgnore
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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


    public OrderStatus getStatus() {
        return status;
    }

    @JsonGetter
    public List<AttributeValue> getAttributeValues(){
        return values;
    }
    @JsonSetter
    public void setAttributeValues(List<AttributeValue> values){
        this.values = values;
    }

    public void startOrder(){
        if (status==OrderStatus.ENTERING){
            setStatus(OrderStatus.IN_PROGRESS);
        }
        else{
            throw new OrderException("Can not start order, status is not entering");
        }

    }

    public void completeOrder(){
        if (status==OrderStatus.IN_PROGRESS){
            setStatus(OrderStatus.COMPLETED);
        }
        else{
            throw new OrderException("Can not start order, status is not in progress");
        }

    }

    public void setOrderStatus(OrderStatus status){
        this.status = status;
    }

    private void setStatus(OrderStatus status) {
        if (status == OrderStatus.COMPLETED&&aim==OrderAIM.NEW){
            service.setStatus(Service.ServiceStatus.ACTIVE);
        }
        if (status == OrderStatus.COMPLETED&&aim==OrderAIM.DISCONNECT){
            service.setStatus(Service.ServiceStatus.DISCONNECTED);
        }
        this.status = status;
    }


    public OrderAIM getAim() {
        return aim;
    }


    public void setAim(OrderAIM aim) {
        this.aim = aim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(name, order.name) && Objects.equals(description, order.description) && Objects.equals(service, order.service) && Objects.equals(specification, order.specification) && Objects.equals(customer, order.customer) && status == order.status && aim == order.aim && Objects.equals(values, order.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, service, specification, customer, status, aim, values);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                //", service=" + service.getId() +
                ", specification=" + specification.getId() +
                ", customer=" + customer.getId() +
                ", status=" + status +
                ", aim=" + aim +
                ", values=" + values +
                '}';
    }

    public String serialize() throws JsonProcessingException{
        return new ObjectMapper().writeValueAsString(this);
    }

    public static Order deserialize(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(str, Order.class);
        return order;
    }

    public void addValue(AttributeValue attributeValue){
        values.add(attributeValue);
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    public UUID getSpecificationID(){
        return specification.getId();
    }

    @JsonSetter
    public void setSpecificationID(UUID specID){
        specification=new Specification(specID);
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    public UUID getServiceID(){
        return service.getId();
    }

    @JsonSetter
    public void setServiceID(UUID specID){
        service=new Service(specID);
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithCustomerID.class)
    public UUID getCustomerID(){
        return customer.getId();
    }

    @JsonSetter
    @JsonView(OrderAndServiceViews.WithCustomerID.class)
    public void setCustomerID(UUID specID){
        customer=new Customer(specID);
    }

    public boolean isEditable(){
        if (status==OrderStatus.ENTERING && aim != OrderAIM.DISCONNECT){
            return true;
        }
        return false;
    }

}
