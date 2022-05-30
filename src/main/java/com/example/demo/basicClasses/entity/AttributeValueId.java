package com.example.demo.basicClasses.entity;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AttributeValueId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    public AttributeValueId() {
    }

    public AttributeValueId(Order order, Attribute attribute) {
        this.order = order;
        this.attribute = attribute;
    }

    public AttributeValueId(Service service, Attribute attribute) {
        this.service = service;
        this.attribute = attribute;
    }

    public AttributeValueId(Service service, Order order, Attribute attribute) {
        this.service = service;
        this.order = order;
        this.attribute = attribute;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValueId that = (AttributeValueId) o;
        return Objects.equals(service, that.service) &&
                Objects.equals(order, that.order) &&
                Objects.equals(attribute, that.attribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, order, attribute);
    }
}
