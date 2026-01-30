package org.example.config;

import org.example.dto.GetUserRequest;
import org.example.dto.UserResponse;
import org.example.dto.XmlUserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.*;
import org.springframework.integration.http.dsl.Http;

@Configuration
public class GetUserFlow {

    private static final Logger log = LoggerFactory.getLogger(GetUserFlow.class);

    @Bean
    public IntegrationFlow getUserByIdFlow() {
        return IntegrationFlow.from("xmlGetUserInboundChannel")

                // Optional: log inbound request
                .log(m -> "INBOUND GetUserRequest: " + m.getPayload())

                // Extract id from payload to headers for URI template
                .enrichHeaders(h -> h.headerFunction("userId", m -> ((GetUserRequest) m.getPayload()).getId()))

                // Call downstream: GET http://localhost:8080/users/{userId}

                .handle(Http.outboundGateway("http://localhost:8080/users/{userId}")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(UserResponse.class)
                        .uriVariable("userId", "headers.userId")
                )


                // Optional: log JSON response POJO
                .log(m -> "DOWNSTREAM RESP (UserResponse): " + m.getPayload())

                // Map to XML response for caller
                .transform((UserResponse r) -> new XmlUserResult(
                        r.getId(), r.getFirstName(), r.getLastName(), r.getEmail(), r.getActive()
                ))

                // Optional: log final XML DTO
                .log(m -> "REPLY XmlUserResult: " + m.getPayload())

                // Send to the reply channel bound to the inbound gateway
                .channel("xmlGetUserReplyChannel")
                .get();
    }
}