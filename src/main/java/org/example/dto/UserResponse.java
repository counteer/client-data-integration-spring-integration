package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse {

    private Long id;
    private Boolean active;
    private String email;

    @JsonProperty("firstName")
    private String firstName;

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @JsonProperty("lastName")
    private String lastName;

    public UserResponse() {}

    public UserResponse(Long id, Boolean active, String email, String firstName, String lastName) {
        this.id = id;
        this.active = active;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
