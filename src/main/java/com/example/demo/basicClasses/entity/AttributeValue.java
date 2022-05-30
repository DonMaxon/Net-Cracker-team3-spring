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

    @EmbeddedId
    private AttributeValueId attributeValueId;

    @JsonIgnore
    private LocalDate date;
    @JsonIgnore
    private Integer integer;
    @JsonIgnore
    private String string;

    public AttributeValue(){

    }

    public AttributeValue(AttributeValueId attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    public AttributeValue(AttributeValueId attributeValueId, String string) {
        this.attributeValueId = attributeValueId;
        this.string = string;
    }

    @JsonIgnore
    public AttributeValueId getAttributeValueId() {
        return attributeValueId;
    }

    @JsonIgnore
    public void setAttributeValueId(AttributeValueId attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithoutCustomerID.class)
    public UUID getAttributeId() {
        return attributeValueId.getAttribute().getId();
    }

    @JsonSetter
    public void setAttributeId(UUID attributeId) {
        this.attributeValueId = attributeValueId;
    }

    @JsonGetter
    @JsonView(AttributeValue.ValueViews.ValueWithOrderService.class)
    public UUID getServiceId(){
        if (attributeValueId.getService()!=null) {
            return attributeValueId.getService().getId();
        }
        return null;
    }

    @JsonSetter
    public void setServiceId(UUID id){
        if (attributeValueId.getService()!=null) {
            attributeValueId.getService().setId(id);
        }
        else {
            attributeValueId.setService(new Service(id));
        }
    }

    @JsonGetter
    @JsonView(AttributeValue.ValueViews.ValueWithOrderService.class)
    public UUID getOrderId(){
        if (attributeValueId.getOrder()!=null) {
            return attributeValueId.getOrder().getId();
        }
        return null;
    }

    @JsonSetter
    public void setOrderId(UUID id){
        if (attributeValueId.getOrder()!=null) {
            attributeValueId.getService().setId(id);
        }
        else {
            attributeValueId.setOrder(new Order(id));
        }
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
        if (date!=null){
            return date.toString();
        }
        if (integer!=null){
            return integer.toString();
        }
        return string;
    }


    @JsonSetter
    public void setValue(String str){
        if (attributeValueId.getAttribute().getType()==null){
            this.string =str;
            return;
        }
        switch (attributeValueId.getAttribute().getType()){
            case DATE:
                date = LocalDate.parse(str);
                return;
            case NUMBER:
                integer = Integer.parseInt(str);
                return;

        }
        this.string =str;
    }


    public static class ValueViews {

        public static class ValueWithoutOrderService extends OrderAndServiceViews.WithCustomerID{

        }

        public static class ValueWithOrderService extends ValueWithoutOrderService {

        }

    }

}
