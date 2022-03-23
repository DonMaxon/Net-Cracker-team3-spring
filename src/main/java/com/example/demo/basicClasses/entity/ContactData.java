package com.example.demo.basicClasses.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "Contact_data")
public class ContactData {

    @Id
    @Column(name = "id")
    @JsonIgnore
    private UUID id;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;

    public ContactData(){
    }


    public ContactData(UUID id, String num, String mail){
        this.id =id;
        phoneNumber=num;
        email=mail;
    }

    public ContactData(String num, String mail){
        this.id =UUID.randomUUID();
        phoneNumber=num;
        email=mail;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phoneNumber, email);
    }

    @Override
    public String toString() {
        return "ContactData{" +
                "uniqueId=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }



    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    public static ContactData deserialize(String str)throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ContactData contactData = mapper.readValue(str, ContactData.class);
        return contactData;
    }
}
