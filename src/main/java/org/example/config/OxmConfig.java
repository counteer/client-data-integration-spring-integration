package org.example.config;

import org.example.dto.UserResponse;
import org.example.dto.XmlUser;
import org.example.dto.XmlUserResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import static org.springframework.integration.dsl.IntegrationFlow.from;
import static org.springframework.integration.http.dsl.Http.outboundGateway;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class OxmConfig {


}