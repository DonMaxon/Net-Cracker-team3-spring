package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.services.*;
import com.example.demo.basicClasses.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.AccessDeniedException;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private boolean checkOnAdmin(){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isAdmin()){
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
        model.addAttribute("locations", locations);
        model.addAttribute("services", service);
        model.addAttribute("values", values);
        return "admin_one_service";
    }

    @GetMapping("/admin_one_order")
    public String oneOrder(@RequestParam("order") UUID uuid, Model model){
        if (!checkOnAdmin()){
            throw new AccessDeniedException("403 returned");
        }
        Order order = orderService.findById(uuid);
        List<AttributeValue> values = order.getAttributeValues();
        List<Location> locations = order.getSpecification().getAvailableLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("orders", order);
        model.addAttribute("values", values);
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

}
