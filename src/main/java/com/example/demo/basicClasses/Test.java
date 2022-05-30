package com.example.demo.basicClasses;

import com.example.demo.basicClasses.api.CustomerAPI;
import com.example.demo.basicClasses.api.LocationAPI;
import com.example.demo.basicClasses.api.exceptions.WrongCountryException;
import com.example.demo.basicClasses.deserializers.SpecificationDeserializer;
import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.serializing.*;
import com.example.demo.basicClasses.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.basicClasses.entity.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@org.springframework.stereotype.Service
public class Test {

    @Autowired
    CustomerSerializer customerSerializer;
    @Autowired
    AttributeSerializer attributeSerializer;
    @Autowired
    LocationSerializer locationSerializer;
    @Autowired
    SpecificationSerializer specificationSerializer;
    @Autowired
    ServiceSerializer serviceSerializer;
    @Autowired
    OrderSerializer orderSerializer;
    @Autowired
    AttributeValueSerializer attributeValueSerializer;
    @Autowired
    UserService userService;

    public void test() throws WrongCountryException {
        CustomerAPI customerAPI = new CustomerAPI();
        Customer obj = customerAPI.createCustomer("max", "td",
                new ContactData("+78005553535", "my@mail.ru"),
                new Location("aa", Location.Types.ADDRESS), 0);

        Attribute attribute = new Attribute(UUID.randomUUID(),
                "speed", true, Attribute.AttributeTypes.NUMBER, null);

        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);
        Location loc1 = new Location("ls", Location.Types.CITY);
        Location loc2 = new Location("gs", Location.Types.ADDRESS, loc1);
        ArrayList locs = new ArrayList<Location>();
        locs.add(loc2);
        Specification spec = new Specification(UUID.randomUUID(), "spec1", "test_desc",
                attributes, locs);
        attribute.setSpecification(spec);
        Service service = new Service(UUID.randomUUID(),
                "service1", "descServ", Service.ServiceStatus.ACTIVE, spec, obj);
        Order order = new Order(UUID.randomUUID(), "order1", "descOrd",
                service, spec, obj, Order.OrderStatus.ENTERING, Order.OrderAIM.MODIFY);
        obj.getOrders().add(order);
        obj.getServices().add(service);
        AttributeValue attributeValue = new AttributeValue(new AttributeValueId(order, attribute));
        attributeValue.setValue("5");
        AttributeValue attributeValue1 = new AttributeValue(new AttributeValueId(service, attribute));
        attributeValue1.setValue("4");
        order.getParams().put(attributeValue.getAttributeId(), attributeValue);
        service.getParams().put(attributeValue.getAttributeId(), attributeValue1);
        User user = new User(UUID.randomUUID(), false, "user1", "user");
        User admin = new User(UUID.randomUUID(), true, "admin1", "admin");
        obj.setUser(user);
        try {
            String str = customerSerializer.serialize(obj);
            System.out.println(str);
            //str = "{\"name\":\"ls\",\"type\":\"CITY\",\"children\":[{\"name\":\"gs\",\"type\":\"ADDRESS\",\"children\":[]}]}}\n";
            Customer object = customerSerializer.deserialize(str);
            System.out.println("end");
			/*String str = obj3.simpleSerialize();
			System.out.println(str);
			obj3 = Location.simpleDeserialize(str);*/
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
