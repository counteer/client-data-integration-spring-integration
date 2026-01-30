package org.example.dto;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "UserResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlUserResult {

    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "firstName")
    private String firstName;

    @XmlElement(name = "lastName")
    private String lastName;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "active")
    private Boolean active;

    public XmlUserResult() {}

    public XmlUserResult(Long id, String firstName, String lastName, String email, Boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}