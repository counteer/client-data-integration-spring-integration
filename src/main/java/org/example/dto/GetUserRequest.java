package org.example.dto;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "GetUserRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUserRequest {

    @XmlElement(name = "id", required = true)
    private Long id;

    public GetUserRequest() {}

    public GetUserRequest(Long id) { this.id = id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
