package org.example.config;

import org.example.dto.GetUserRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.http.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class HttpInboundGetConfig {

    @Bean
    public MessageChannel xmlGetUserInboundChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel xmlGetUserReplyChannel() { return new DirectChannel(); }

    @Bean
    public RequestMapping getUserMapping() {
        RequestMapping m = new RequestMapping();
        m.setPathPatterns("/xml/user/get");
        m.setMethods(HttpMethod.POST); // as per your curl
        m.setConsumes(MediaType.APPLICATION_XML_VALUE);
        m.setProduces(MediaType.APPLICATION_XML_VALUE);
        return m;
    }

    @Bean
    public HeaderMapper<HttpHeaders> getInboundHeaderMapper() {
        DefaultHttpHeaderMapper mapper = DefaultHttpHeaderMapper.inboundMapper();
        mapper.setInboundHeaderNames(new String[]{"*"});
        return mapper;
    }

    @Bean
    public HttpRequestHandlingMessagingGateway getUserInboundGateway(
            RequestMapping getUserMapping,
            MessageChannel xmlGetUserInboundChannel,
            MessageChannel xmlGetUserReplyChannel,
            Jaxb2Marshaller jaxb2Marshaller,
            HeaderMapper<HttpHeaders> getInboundHeaderMapper) {

        HttpRequestHandlingMessagingGateway gw = new HttpRequestHandlingMessagingGateway(true);
        gw.setRequestMapping(getUserMapping);
        gw.setRequestPayloadTypeClass(GetUserRequest.class);
        gw.setRequestChannel(xmlGetUserInboundChannel);
        gw.setReplyChannel(xmlGetUserReplyChannel);
        gw.setStatusCodeExpression(new LiteralExpression(HttpStatus.OK.name()));
//        gw.setCharset(StandardCharsets.UTF_8.name());

        MarshallingHttpMessageConverter xmlConv =
            new MarshallingHttpMessageConverter(jaxb2Marshaller, jaxb2Marshaller);
        xmlConv.setSupportedMediaTypes(List.of(MediaType.APPLICATION_XML));
        gw.setMessageConverters(List.<HttpMessageConverter<?>>of(xmlConv));

        gw.setHeaderMapper(getInboundHeaderMapper);
        return gw;
    }
}