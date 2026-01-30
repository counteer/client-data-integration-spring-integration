package org.example.mapping;

import org.example.dto.UserCreateRequest;
import org.example.dto.XmlUser;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("xmlUserToJsonUser")
public class XmlUserToJsonUser implements Function<XmlUser, UserCreateRequest> {

    @Override
    public UserCreateRequest apply(XmlUser xml) {
        String[] names = NameSplitter.split(xml.getFullName());
        return new UserCreateRequest(
            xml.getActive(),
            xml.getEmail(),
            names[0],
            names[1]
        );
    }
}