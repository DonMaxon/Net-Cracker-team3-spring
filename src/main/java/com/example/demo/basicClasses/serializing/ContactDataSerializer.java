package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.ContactData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class ContactDataSerializer {

    public String serialize(ContactData contactData) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(contactData);
    }

    public ContactData deserialize(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ContactData.class);
    }
}
