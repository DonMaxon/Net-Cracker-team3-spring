package com.example.demo.basicClasses.entity;

import com.example.demo.basicClasses.deserializers.AttributeValueDeserializer;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;


//@JsonSerialize(using = AttributeValue.AttributeValueSerializer.class)
@Entity
@Table(name = "attribute_value")
@AccessType(AccessType.Type.PROPERTY)
@JsonDeserialize(using = AttributeValueDeserializer.class)
public class AttributeValue {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = true)
    private Service service;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;
    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @JsonIgnore
    private LocalDate date;
    @JsonIgnore
    private Integer integer;
    @JsonIgnore
    private String string;

    public AttributeValue(){

    }

    public AttributeValue(UUID id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }


    @JsonGetter
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    public UUID getAttributeId() {
        return attribute.getId();
    }



    @JsonGetter
    @JsonView(AttributeValue.ValueViews.ValueWithOrderService.class)
    public UUID getServiceId(){
        if (service!=null) {
            return service.getId();
        }
        return null;
    }

    @JsonSetter
    public void setServiceId(UUID id){
        setService(new Service(id));
    }

    @JsonGetter
    @JsonView(AttributeValue.ValueViews.ValueWithOrderService.class)
    public UUID getOrderId(){
        if (getOrder()!=null) {
            return getOrder().getId();
        }
        return null;
    }

    @JsonSetter
    public void setOrderId(UUID id){
        setOrder(new Order(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValue that = (AttributeValue) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(integer, that.integer) &&
                Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, integer, string);
    }

    @JsonGetter
    @Column(name = "value")
    public String getValue(){
        switch (getAttribute().getType()){
            case DATE: return date.toString();
            case NUMBER: return integer.toString();
            case STRING:
            default: return string;
        }
    }


    @JsonSetter
    public void setValue(String str){
        switch (getAttribute().getType()){
            case DATE:
                date = LocalDate.parse(str);
                return;
            case NUMBER:
                integer = Integer.parseInt(str);
                return;
            case STRING:
            default: string =str;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public static class ValueViews {

        public static class ValueWithoutOrderService extends OrderAndServiceViews.WithCustomerID{

        }

        public static class ValueWithOrderService extends ValueWithoutOrderService {

        }

    }

}
