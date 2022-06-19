package com.example.demo.basicClasses.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;


import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonDeserialize(using = AttributeDeserializer.class)
@Table(name = "Attribute")
@Access(AccessType.FIELD)
public class Attribute implements ObjectWithId {

    public Attribute(UUID randomUUID, String name, boolean isMandatory) {
        id = randomUUID;
    }

    public enum AttributeTypes {DATE, NUMBER, STRING}

    @Id
    @JsonView(Attribute.AttributeViews.AttributeWithoutParent.class)
    private UUID id;
    @Column(name = "mandatority")
    @JsonView(Attribute.AttributeViews.AttributeWithoutParent.class)
    private boolean isMandatory;
    @Column(name = "type")
    @JsonView(Attribute.AttributeViews.AttributeWithoutParent.class)
    private AttributeTypes type;
    @Column(name = "name")
    @JsonView(Attribute.AttributeViews.AttributeWithoutParent.class)
    private String name;
    @JoinColumn(name = "specification")
    @ManyToOne
    @JsonIgnore
    //@Transient
    private Specification specification;

    public Attribute(){

    }

    public Attribute(UUID id) {
        this.id = id;
    }

    public Attribute(UUID id, String name, AttributeTypes type, Specification specification) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.specification = specification;
    }

    public Attribute(UUID id, String name, boolean isMandatory, AttributeTypes type) {
        this.id = id;
        this.isMandatory = isMandatory;
        this.type = type;
        this.name = name;
    }

    public Attribute(UUID id, String name,boolean isMandatory, AttributeTypes type,  Specification specification) {
        this.id = id;
        this.isMandatory = isMandatory;
        this.type = type;
        this.name = name;
        this.specification = specification;
    }

    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public boolean getMandatority() {
        return isMandatory;
    }

    @JsonIgnore
    public void setMandatority(boolean mandatory) {
        isMandatory = mandatory;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AttributeTypes getType() {
        return type;
    }

    public void setType(AttributeTypes type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(id, attribute.id) &&
                type == attribute.type &&
                Objects.equals(name, attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, specification.getId());
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", isMandatory=" + isMandatory +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", specification=" + specification.getId() +
                '}';
    }

    /*public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static Attribute deserialize(String str) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Attribute attr = mapper.readValue(str, Attribute.class);
        return attr;
    }
*/
    public static class AttributeDeserializer extends KeyDeserializer {

        @Override
        public Attribute deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            Attribute attribute = new Attribute();
            String strID = s.substring(s.indexOf('=')+1, s.indexOf(','));
            attribute.setId(UUID.fromString(strID));
            String strMandatory = s.substring(s.indexOf("isMandatory")+12, s.indexOf("type")-2);
            attribute.setMandatority(Boolean.parseBoolean(strMandatory));
            String strType = s.substring(s.indexOf("type")+5, s.indexOf("name")-2);
            switch (strType) {
                case "NUMBER":
                    attribute.setType(AttributeTypes.NUMBER);
                    break;
                case "DATE":
                    attribute.setType(AttributeTypes.DATE);
                    break;
                default:
                    attribute.setType(AttributeTypes.STRING);
            }
            String strName = s.substring(s.indexOf("name")+6, s.indexOf("spec")-3);
            attribute.setName(strName);
            String strSpec = s.substring(s.indexOf("spec")+14, s.indexOf("}"));
            attribute.setSpecification(findSpec(UUID.fromString(strSpec)));
            return attribute;
        }

        private static Specification findSpec(UUID id){
            /*Repo repo = Repo.getInstance();
            if (repo!=null&&repo.getSpecs()!=null) {
                for (int i = 0; i < repo.getSpecs().size(); ++i) {
                    if (id.equals(((ArrayList<Specification>)repo.getSpecs()).get(i).getId())) {
                        return repo.getSpecs().get(i);
                    }
                }
            }*/
            return new Specification(id);
        }
    }

    //@Access(AccessType.PROPERTY)
    //@Column(name = "specificationID")
    @JsonGetter
    @JsonView(Attribute.AttributeViews.AttributeWithParent.class)
    private UUID getSpecificationId(){
        return specification.getId();
    }

    @JsonSetter
    private void setSpecificationId(UUID id){
        specification = new Specification(id);
    }

    public static class AttributeViews {
        public static class AttributeWithoutParent {

        }

        public static class AttributeWithParent extends AttributeWithoutParent {

        }
    }

}
