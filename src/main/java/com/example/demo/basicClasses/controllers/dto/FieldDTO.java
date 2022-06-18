package com.example.demo.basicClasses.controllers.dto;

public class FieldDTO {
   private String id;
   private String name;
   private String value;
   private boolean isRequired;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public boolean isRequired() {
      return isRequired;
   }

   public void setRequired(boolean required) {
      isRequired = required;
   }

   @Override
   public String toString() {
      return "FieldDTO{" +
              "id='" + id + '\'' +
              ", name='" + name + '\'' +
              ", value='" + value + '\'' +
              ", isRequired=" + isRequired +
              '}';
   }
}
