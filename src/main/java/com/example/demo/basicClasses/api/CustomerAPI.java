package com.example.demo.basicClasses.api;


import com.example.demo.basicClasses.api.exceptions.CreatingException;
import com.example.demo.basicClasses.entity.ContactData;
import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Location;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerAPI {
    private static CustomerAPI instance;


    public Customer createCustomer(String firstName, String lastName, ContactData contactData,
                                   Location location, int balance){
        if (location.getType()!= Location.Types.ADDRESS){
            throw  new CreatingException("Error during creating customer, location is not address");
        }
        if (!isValidContactData(contactData)){
            throw  new CreatingException("Error during creating customer, contact data is not valid");
        }
        return new Customer(UUID.randomUUID(), firstName, lastName, contactData, location, balance);
    }
    private boolean isValidContactData(ContactData data){
        if (isValidPhoneNum(data.getPhoneNumber())&& isValidEmail(data.getEmail())){
            return true;
        }
        return false;
    }

    public boolean isValidEmail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public boolean isValidPhoneNum(String phoneNum) {
        Pattern VALID_PHONE_NUM_REGEX =
                Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$");
        Matcher matcher = VALID_PHONE_NUM_REGEX.matcher(phoneNum);
        return matcher.find();
    }


}
