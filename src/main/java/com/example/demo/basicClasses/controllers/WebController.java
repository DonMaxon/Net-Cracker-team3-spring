package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.api.OrderServiceAPI;
import com.example.demo.basicClasses.controllers.dto.FieldDTO;
import com.example.demo.basicClasses.controllers.dto.FormDTO;
import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.entity.exceptions.OrderException;
import com.example.demo.basicClasses.services.*;
import com.example.demo.basicClasses.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;


import java.util.*;

@Controller
public class WebController {

    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;
    @Autowired
    ServiceService serviceService;
    @Autowired
    SpecificationService specificationService;
    @Autowired
    UserService userService;
    @Autowired
    LocationService locationService;
    @Autowired
    AttributeService attributeService;
    @Autowired
    AttributeValueService attributeValueService;
    @Autowired
    ContactDataService contactDataService;
    @Autowired
    OrderServiceAPI orderServiceAPI;

    private boolean checkOnAdmin(){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAdmin()){
            return true;
        }
        return false;
    }

    @GetMapping(value = "/afterLogin")
    public String afterLogin(Model model) {
        if (checkOnAdmin()){
            return "redirect:/admin_customer";
        }
        return "hello";
    }

    @GetMapping(value = "/admin_customer")
    public String adminCustomer(Model model) {
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("customers", customerService.getAll());
        return "admin_customer";
    }

    @GetMapping(value = "/admin_orders")
    public String adminOrders(Model model) {
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("orders", orderService.getAll());
        model.addAttribute("customer", new Customer(UUID.randomUUID()));
        return "admin_orders";
    }

    @GetMapping(value = "/admin_services")
    public String adminServices(Model model) {
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("services", serviceService.getAll());
        model.addAttribute("customer", new Customer(UUID.randomUUID()));
        return "admin_services";
    }

    @GetMapping(value = "/admin_specifications")
    public String adminSpecifications(Model model) {
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("specifications", specificationService.getAll());
        return "admin_specifications";
    }

    @GetMapping(value = "/admin_users")
    public String adminUsers(Model model) {
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("users", userService.getAll());
        return "admin_users";
    }

    @GetMapping(value = "/admin_locations")
    public String adminLocations(Model model) {
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        List<Location> locations = locationService.findByType(Location.Types.COUNTRY);
        if (locations.size()==0){
            locations = locationService.findByType(Location.Types.STATE);
            if (locations.size()==0){
                locations = locationService.findByType(Location.Types.CITY);
                if (locations.size()==0){
                    locations= locationService.findByType(Location.Types.ADDRESS);
                }
            }
        }
        model.addAttribute("locations", locations);
        return "admin_locations";
    }

    @GetMapping("/one_location")
    public String oneLocation(@RequestParam("location") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Location location = locationService.findById(uuid);
        //model.addAttribute("location", location);
        model.addAttribute("parent", location);
        model.addAttribute("locations", location.getChildren());
        return "admin_locations";
    }
    
    @GetMapping("/del_location")
    public String delLocation(@RequestParam("location") UUID uuid, Model model){
        locationService.delete(uuid);
        return "redirect:/admin_locations";
    }

    @GetMapping("/admin_one_customer")
    public String adminOneCustomer(@RequestParam("customer") UUID uuid, Model model) {
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Customer client = customerService.findById(uuid);
        List<Service> services = client.getServices();
        List<Order> orders = client.getOrders();
        model.addAttribute("customers", client);
        model.addAttribute("services", services);
        model.addAttribute("orders", orders);
        return "admin_one_customer";
    }

    @GetMapping("/del_spec")
    public String delSpec(@RequestParam("spec") UUID uuid, Model model){
        specificationService.delete(uuid);
        return "admin_specifications";
    }

    @GetMapping("/admin_one_spec")
    public String oneSpec(@RequestParam("spec") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Specification spec = specificationService.findById(uuid);
        List<Location> locations = spec.getAvailableLocations();
        List<Attribute> attributes = spec.getAttributes();
        model.addAttribute("specifications", spec);
        model.addAttribute("locations", locations);
        model.addAttribute("attributes", attributes);
        return "admin_one_spec";
    }

    @GetMapping("/admin_one_service")
    public String oneService(@RequestParam("service") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Service service = serviceService.findById(uuid);
        List<AttributeValue> values = service.getAttributeValues();
        List<Location> locations = service.getSpecification().getAvailableLocations();
        List<FieldDTO> fields = new ArrayList<>();
        for (Attribute attribute: service.getSpecification().getAttributes()){
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setId(attribute.getId().toString());
            fieldDTO.setRequired(attribute.getMandatority());
            fieldDTO.setName(attribute.getName());
            switch (attribute.getType()){
                case DATE: fieldDTO.setType("date"); break;
                case NUMBER:  fieldDTO.setType("number"); break;
                case STRING:
                default:    fieldDTO.setType("text");
            }
            if(service.getParams().containsKey(attribute.getId())){
                fieldDTO.setValue(service.getParams().get(attribute.getId()).getValue());
            }
            fields.add(fieldDTO);
        }
        FormDTO form = new FormDTO();
        form.setFields(fields);
        System.out.println("OUT form = " + form);
        model.addAttribute("locations", locations);
        model.addAttribute("services", service);
        model.addAttribute("values", values);
        model.addAttribute("orders", service.getOrders());
        model.addAttribute("form", form);
        return "admin_one_service";
    }

    @GetMapping("/admin_one_order")
    public String oneOrder(@RequestParam("order") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Order order = orderService.findById(uuid);
        List<FieldDTO> fields = new ArrayList<>();
        for (Attribute attribute: order.getSpecification().getAttributes()){
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setId(attribute.getId().toString());
            fieldDTO.setRequired(attribute.getMandatority());
            fieldDTO.setName(attribute.getName());
            switch (attribute.getType()){
                case DATE: fieldDTO.setType("date"); break;
                case NUMBER:  fieldDTO.setType("number"); break;
                case STRING:
                default:    fieldDTO.setType("text");
            }
            if(order.getParams().containsKey(attribute.getId())){
                fieldDTO.setValue(order.getParams().get(attribute.getId()).getValue());
            }
            fields.add(fieldDTO);
        }
        FormDTO form = new FormDTO();
        form.setFields(fields);
        System.out.println("OUT form = " + form);
        List<Location> locations = order.getSpecification().getAvailableLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("orders", order);
        model.addAttribute("fields", fields);
        model.addAttribute("form", form);
        return "admin_one_order";
    }

    @GetMapping("/new_location")
    public String newLocation(@RequestParam(value = "creator", required = false) UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        if (uuid!=null) {
            model.addAttribute("parent", locationService.findById(uuid));
        }
        else {
            model.addAttribute("parent", new Location(new UUID(0, 0)));
        }
        model.addAttribute("location", new Location());
        return "new_location";
    }

    @PostMapping("/new_location")
    public String newLocationPost(@RequestParam(value = "creator", required = false) UUID uuid, Model model, @ModelAttribute Location location){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        if (!uuid.equals(new UUID(0, 0))){
            Location parent = locationService.findById(uuid);
            location.setParent(parent);
            if (parent.getType()== Location.Types.ADDRESS){
                return "redirect:/one_location?location="+uuid.toString();
            }
            location.setType(Location.Types.returnType(parent.getType().getVal()+1));
        }
        location.setId(UUID.randomUUID());
        if (location.getType()==null){
            location.setType(Location.Types.COUNTRY);
        }
        locationService.save(location);
        return "redirect:/one_location?location="+location.getId().toString();
    }

    @GetMapping("/new_spec")
    public String newSpec(Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("spec", new Specification());
        return "new_spec";
    }

    @PostMapping("/new_spec")
    public String newSpecPost(Model model, @ModelAttribute Specification specification){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        specification.setId(UUID.randomUUID());
        specificationService.save(specification);
        return "redirect:/admin_one_spec?spec="+specification.getId().toString();
    }

    @GetMapping("/update_location")
    public String updLocation(@RequestParam(value = "location") UUID uuid, Model model){
        model.addAttribute("location", locationService.findById(uuid));
        return "update_location";
    }

    @PostMapping("/update_location")
    public String updLocationPost(@RequestParam(value = "location") UUID uuid, Model model, @ModelAttribute Location location){
        Location location1 = locationService.findById(uuid);
        location1.setName(location.getName());
        locationService.save(location1);
        return "redirect:/one_location?location="+location.getId().toString();
    }

    @GetMapping("/update_spec")
    public String updSpec(@RequestParam(value = "spec") UUID uuid, Model model){
        model.addAttribute("spec", specificationService.findById(uuid));
        return "update_spec";
    }

    @PostMapping("/update_spec")
    public String updSpecPost(@RequestParam(value = "spec") UUID uuid, Model model, @ModelAttribute Specification specification){
        Specification spec = specificationService.findById(uuid);
        spec.setName(specification.getName());
        spec.setDescription(specification.getDescription());
        specificationService.save(spec);
        return "redirect:/admin_one_spec?spec="+spec.getId().toString();
    }

    @GetMapping("/change_user_status")
    public String changeUserStatus(@RequestParam(value = "user") UUID uuid, Model model){
        User user = userService.findById(uuid);
        user.changeStatus();
        userService.save(user);
        return "redirect:/admin_users";
    }

    @GetMapping("/new_attribute")
    public String newAttribute(@RequestParam(value = "spec") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("attribute", new Attribute());
        model.addAttribute("spec", specificationService.findById(uuid));
        return "new_attribute";
    }

    @PostMapping("/new_attribute")
    public String newAttributePost(@RequestParam(value = "spec") UUID uuid, Model model, @ModelAttribute Attribute attribute){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        attribute.setId(UUID.randomUUID());
        Specification specification = specificationService.findById(uuid);
        attribute.setSpecification(specification);
        try {
            specification.addAttribute(attribute);
        }
        catch (InstantiationException e){
            return "redirect:/admin_one_spec?spec="+specificationService.findById(uuid).getId().toString();
        }
        specificationService.save(specification);
        return "redirect:/admin_one_spec?spec="+specificationService.findById(uuid).getId().toString();
    }

    @GetMapping("/add_location")
    public String addLocation(@RequestParam(value = "spec") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("spec", specificationService.findById(uuid));
        model.addAttribute("locations", locationService.getAll());
        model.addAttribute("newLocation", new Location());
        return "add_location";
    }

    @PostMapping("/add_location")
    public String addLocationPost(@RequestParam(value = "spec") UUID uuid, Model model, @ModelAttribute Location location){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Specification specification = specificationService.findById(uuid);
        specification.addLocation(location);
        specificationService.save(specification);
        return "redirect:/admin_one_spec?spec="+specificationService.findById(uuid).getId().toString();
    }

    @GetMapping("/del_location_from_spec")
    public String delLocationFromSpec(@RequestParam(value = "spec") UUID spec_uuid, @RequestParam("location") UUID location_uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Specification specification = specificationService.findById(spec_uuid);
        specification.deleteLocation(locationService.findById(location_uuid));
        specificationService.save(specification);
        return "redirect:/admin_one_spec?spec="+spec_uuid.toString();
    }

    @GetMapping("/change_status")
    public String newValuePost(@RequestParam(value = "order") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Order order = orderService.findById(uuid);

        try {
            if (order.getStatus() == Order.OrderStatus.ENTERING) {
                orderServiceAPI.startOrder(order);
            } else {
                orderServiceAPI.completeOrder(order);
            }
        }
        catch (OrderException e){

        }
        return "redirect:/admin_one_order?order="+uuid.toString();
    }

    @GetMapping("/new_order")
    public String newOrder(@RequestParam(value = "customer") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("order", new Order());
        model.addAttribute("specs", specificationService.getAll());
        Customer customer = customerService.findById(uuid);
        model.addAttribute("customer", customer);
        return "new_order";
    }

    @PostMapping("/new_order")
    public String newOrderPost(@RequestParam(value = "customer") UUID uuid, Model model, @ModelAttribute Order orderForm){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        System.out.println(orderForm);
        Order order = orderServiceAPI.createOrderNew(orderForm.getSpecification(), orderForm.getCustomer(), new ArrayList<>());
        order.setDescription(orderForm.getDescription());
        orderService.save(order);
        return "redirect:/admin_one_order?order="+order.getId().toString();
    }

    @GetMapping("new_customer")
    public String newCustomer (Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("customer", new Customer(new ContactData()));
        model.addAttribute("locations", locationService.findByType(Location.Types.ADDRESS));
        model.addAttribute("users", userService.getAll());
        return "new_customer";
    }

    @PostMapping("new_customer")
    public String newCustomerPost (Model model, @ModelAttribute Customer customer){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        customer.setId(UUID.randomUUID());
        customer.getContactData().setId(UUID.randomUUID());
        contactDataService.save(customer.getContactData());
        customerService.save(customer);
        if (customer.getUser()!=null) {
            customer.getUser().setCustomer(customer);
            userService.save(customer.getUser());
        }
        return "redirect:/admin_one_customer?customer="+customer.getId().toString();
    }

    @GetMapping("new_user")
    public String newUser (Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("user", new User());
        model.addAttribute("customers", customerService.getAll());

        return "new_user";
    }

    @PostMapping("new_user")
    public String newUserPost (Model model, @ModelAttribute User user){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        user.setId(UUID.randomUUID());
        user.setActive(false);
        userService.save(user);
        if (user.getCustomer()!=null) {
            user.getCustomer().setUser(user);
            customerService.save(user.getCustomer());
        }
        return "redirect:/admin_users";
    }

    @GetMapping("/del_customer")
    public String delCustomer(@RequestParam("customer") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        customerService.delete(uuid);
        return "redirect:/admin_customer";
    }

    @GetMapping("/del_user")
    public String delUser(@RequestParam("user") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        userService.delete(uuid);
        return "redirect:/admin_users";
    }

    @GetMapping("/update_customer")
    public String updateCustomer(@RequestParam("customer") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("customer", customerService.findById(uuid));
        model.addAttribute("locations", locationService.findByType(Location.Types.ADDRESS));
        model.addAttribute("users", userService.getAll());
        return "update_customer";
    }

    @PostMapping("/update_customer")
    public String updateCustomerPost(@RequestParam("customer") UUID uuid, Model model, @ModelAttribute Customer newCustomer){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        newCustomer.setId(uuid);
        if (newCustomer.getUser()!=null){
            User user = newCustomer.getUser();
            user.setCustomer(newCustomer);
            userService.save(user);
        }
        customerService.save(newCustomer);
        return "redirect:/admin_one_customer?customer="+uuid.toString();
    }

    @GetMapping("/update_user")
    public String updateUser(@RequestParam("user") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("user", userService.findById(uuid));
        model.addAttribute("customers", customerService.getAll());
        return "update_user";
    }

    @PostMapping("/update_user")
    public String updateUserPost(@RequestParam("user") UUID uuid, Model model, @ModelAttribute User newUser){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        newUser.setId(uuid);
        if (newUser.getCustomer()!=null){
            Customer customer = newUser.getCustomer();
            customer.setUser(newUser);
            customerService.save(customer);
        }
        userService.save(newUser);
        return "redirect:/admin_users";
    }

    @GetMapping("/disconnect_service")
    public String disconnectService(@RequestParam("service") UUID uuid, Model model){
        Service service = serviceService.findById(uuid);
        Order order = orderServiceAPI.createOrderDisconnect(service);
        return "redirect:/admin_one_order?order="+order.getId().toString();
    }

    @GetMapping("/modify_service")
    public String modifyService(@RequestParam("service") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        model.addAttribute("service", serviceService.findById(uuid));
        model.addAttribute("order", new Order());
        return "modify_service";
    }

    @PostMapping("/modify_service")
    public String modifyServicePost(@RequestParam("service") UUID uuid, Model model, @ModelAttribute Order orderForm){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Service service = serviceService.findById(uuid);
        Order order = orderServiceAPI.createOrderModify(service);
        order.setDescription(orderForm.getDescription());
        orderService.save(order);
        return "redirect:/admin_one_order?order="+order.getId().toString();
    }

    @PostMapping("/new_params")
    public String newParams(@RequestParam("order") UUID uuid, Model model, @ModelAttribute FormDTO form){
        Order order = orderService.findById(uuid);
        if(order.getStatus() != Order.OrderStatus.ENTERING)
            throw new RuntimeException("You do not able to change parameters");
        System.out.println("form = " + form);
        for(FieldDTO field : form.getFields()){
            Attribute attribute = attributeService.findById(UUID.fromString(field.getId()));
            AttributeValue value = order.getParams().containsKey(attribute.getId()) ?
                    order.getParams().get(attribute.getId()) :
                    new AttributeValue(UUID.randomUUID());
            value.setOrder(order);
            value.setAttribute(attribute);
            value.setValue(field.getValue());
            attributeValueService.save(value);
        }
        orderService.save(order);
        return "redirect:/admin_one_order?order="+uuid.toString();
    }

}
