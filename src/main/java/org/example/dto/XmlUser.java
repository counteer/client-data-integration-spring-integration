package org.example.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlUser {

    @XmlElement(name = "fullName", required = true)
    @NotBlank
    private String fullName;

    @XmlElement(name = "email", required = true)
    @NotBlank
    @Email
    private String email;

    @XmlElement(name = "active", required = true)
    @NotNull
    private Boolean active;

    public XmlUser() {}

    public XmlUser(String fullName, String email, Boolean active) {
        this.fullName = fullName;
        this.email = email;
        this.active = active;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}