package com.example.demo.basicClasses.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;


//@JsonSerialize(using = AttributeValue.AttributeValueSerializer.class)
public class AttributeValue {
    @JsonIgnore
    private LocalDate date;
    @JsonIgnore
    private Integer integer;
    @JsonIgnore
    private String str;
    @JsonIgnore
    private Attribute.AttributeTypes type;

    public AttributeValue(){

    }

    public void setType(Attribute.AttributeTypes type){
        this.type=type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValue that = (AttributeValue) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(integer, that.integer) &&
                Objects.equals(str, that.str) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, integer, str, type);
    }

    @JsonGetter
    public String getValue(){
        try {
            switch (type) {
                case DATE:
                    return date.toString();
                case NUMBER:
                    return integer.toString();
            }
        }
        catch (NullPointerException e){

        }
        return str;
    }


    @JsonSetter
    public void setValue(String str){
        if (type==null){
            this.str=str;
            return;
        }
        switch (type){
            case DATE:
                date = LocalDate.parse(str);
                return;
            case NUMBER:
                integer = Integer.parseInt(str);
                return;

        }
        this.str=str;
    }

    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    @JsonDeserialize
    public static AttributeValue deserialize(String str) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AttributeValue attributeValue = mapper.readValue(str, AttributeValue.class);
        return attributeValue;
    }



}
