Before you do anythng, you have to start the client-data-repository project

After start, you can call it with the next curl command to insert a new User to the database:

`curl -v -X POST "http://localhost:9191/xml/users" \
  -H "Content-Type: application/xml" \
    -d '<User> \
        <fullName>Alice Xerxes</fullName> \
        <email>alice4@example.com</email> \
        <active>true</active> \
      </User>'`

After the first user was inserted, you can call the next curl command:


`curl -v -X POST "http://localhost:9191/xml/user/get"  
-H "Content-Type: application/xml" 
-d '<GetUserRequest><id>1</id></GetUserRequest>'`
