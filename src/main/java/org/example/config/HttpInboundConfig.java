package org.example.config;

import org.example.dto.GetUserRequest;
import org.example.dto.UserResponse;
import org.example.dto.XmlUser;
import org.example.dto.XmlUserResult;
import org.example.mapping.XmlUserToJsonUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.integration.dsl.IntegrationFlow.from;
import static org.springframework.integration.http.dsl.Http.outboundGateway;

@Configuration
public class HttpInboundConfig {

    @Bean
    public MessageChannel xmlInboundChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel xmlReplyChannel() { return new DirectChannel(); }

    @Bean
    public RequestMapping xmlUsersMapping() {
        RequestMapping mapping = new RequestMapping();
        mapping.setPathPatterns("/xml/users");
        mapping.setMethods(HttpMethod.POST);
        mapping.setConsumes(MediaType.APPLICATION_XML_VALUE);
        mapping.setProduces(MediaType.APPLICATION_XML_VALUE);
        return mapping;
    }

    // ✅ Strongly-typed HeaderMapper bean
    @Bean
    public HeaderMapper<HttpHeaders> inboundHeaderMapper() {
        DefaultHttpHeaderMapper mapper = DefaultHttpHeaderMapper.inboundMapper();
        mapper.setInboundHeaderNames(new String[] { "*" });
        return mapper;
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller m = new Jaxb2Marshaller();
        m.setClassesToBeBound(XmlUser.class, XmlUserResult.class, GetUserRequest.class);
        return m;
    }


    @Bean
    public IntegrationFlow userCreationFlow(Jaxb2Marshaller marshaller, ObjectMapper objectMapper) {
        return from("xmlInboundChannel")
                // XML payload already unmarshalled into XmlUser by Http Inbound Gateway


                .log(msg -> "INBOUND: payload=" + msg.getPayload() + ", headers=" + msg.getHeaders())

                .transform(new XmlUserToJsonUser())  // your class implements Function<XmlUser, UserCreateRequest>


                .transform(Transformers.toJson()) // uses Boot’s MappingJackson2 under the hood
                .enrichHeaders(h -> h.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .headerFilter("content-length", "Content-Length", "transfer-encoding")


                .log(msg -> "OUTBOUND REQ (to REST): payload=" + msg.getPayload() + ", headers=" + msg.getHeaders())

                .handle(outboundGateway("http://localhost:8080/users")
                        .httpMethod(HttpMethod.POST)
                        .expectedResponseType(UserResponse.class))
                // Convert JSON response to XML response POJO
                .transform((UserResponse resp) ->
                        new XmlUserResult(resp.getId(), resp.getFirstName(), resp.getLastName(), resp.getEmail(), resp.getActive()))
                // Reply as XML (the inbound gateway’s message converters will marshal it)
                .channel("xmlReplyChannel")
                .get();
    }

    @Bean
    public HttpRequestHandlingMessagingGateway xmlInboundGateway(
            RequestMapping xmlUsersMapping,
            MessageChannel xmlInboundChannel,
            MessageChannel xmlReplyChannel,
            Jaxb2Marshaller jaxb2Marshaller,
            HeaderMapper<HttpHeaders> inboundHeaderMapper) { // ✅ type matches

        HttpRequestHandlingMessagingGateway gw = new HttpRequestHandlingMessagingGateway(true);
        gw.setRequestMapping(xmlUsersMapping);

        gw.setRequestPayloadTypeClass(XmlUser.class);
        gw.setRequestChannel(xmlInboundChannel);
        gw.setReplyChannel(xmlReplyChannel);

        gw.setStatusCodeExpression(new LiteralExpression(HttpStatus.OK.name()));
//        gw.setCharset(StandardCharsets.UTF_8.name());

        MarshallingHttpMessageConverter xmlConverter =
                new MarshallingHttpMessageConverter(jaxb2Marshaller, jaxb2Marshaller);
        xmlConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_XML));
        gw.setMessageConverters(List.<HttpMessageConverter<?>>of(xmlConverter));

        // ✅ No more generic mismatch
        gw.setHeaderMapper(inboundHeaderMapper);

        return gw;
    }
}