package com.example.demo.basicClasses.entity;

import javax.persistence.*;
import java.util.UUID;


public interface OrderService {

    public UUID getId();
    public void setId(UUID id);
}
