# WasteServiceGUI
## Spring Boot 

Spring Boot is an open-source Java-based framework that provides a rapid application development platform for building standalone web applications. It is designed to make it easier to create Spring-powered applications with minimal effort and configuration.

Spring Boot comes with a set of pre-configured dependencies and conventions that eliminate the need for manual configuration, allowing developers to focus on writing application logic instead of spending time on boilerplate code. It also includes an embedded web server, which makes it easy to create and run web applications without having to deploy them to a separate server.

Some of the benefits of using Spring Boot include:

** Spring Boot's pre-configured dependencies and automatic configuration make it easier to develop applications quickly and with minimal setup.
- **Simplified deployment:** With Spring Boot, you can create self-contained executable JAR files that include an embedded web server, which makes it easy to deploy and run your application on any platform.
- **Increased productivity:** Spring Boot provides a range of tools and plugins that make it easier to develop, test, and deploy applications, which can increase developer productivity.
- **Scalability:** Spring Boot provides a scalable architecture that allows you to easily add new features and scale your application as needed.

## Come è suddiviso client-server
There are four layers in Spring Boot are as follows:

**Presentation Layer:** The presentation layer handles the HTTP requests, translates the JSON parameter to object, and authenticates the request and transfer it to the business layer. In short, it consists of views i.e., frontend part.

**usiness Layer:** The business layer handles all the business logic. It consists of service classes and uses services provided by data access layers. It also performs authorization and validation.

**Persistence Layer:** The persistence layer contains all the storage logic and translates business objects from and to database rows.

**Database Layer:** In the database layer, CRUD (create, retrieve, update, delete) operations are performed.

![](https://static.javatpoint.com/springboot/images/spring-boot-architecture.png)

![](https://static.javatpoint.com/springboot/images/spring-boot-architecture2.png)

- Now we have validator classes, view classes, and utility classes.
- Spring Boot uses all the modules of Spring-like Spring MVC, Spring Data, etc. The architecture of Spring Boot is the same as the architecture of Spring MVC, except one thing: there is no need for DAO and DAOImpl classes in Spring boot.
- Creates a data access layer and performs CRUD operation.
- The client makes the HTTP requests (PUT or GET).
- The request goes to the controller, and the controller maps that request and handles it. After that, it calls the service logic if required.
- In the service layer, all the business logic performs. It performs the logic on the data that is mapped to JPA with model classes.
- A JSP page is returned to the user if no error occurred.

## STOMP
STOMP (Simple Text Oriented Messaging Protocol) is a lightweight messaging protocol that defines a simple text-based format for messages exchanged between client and server applications. It is designed to provide a standardized way for different applications to communicate with each other, regardless of the programming language or operating system they are using.

STOMP is based on a publish-subscribe messaging model, in which clients can subscribe to specific topics or channels to receive messages from a server, or publish messages to a server to be delivered to other subscribed clients. The protocol also supports transactional messaging, allowing clients to send and receive messages as part of a single transaction.

## JQuery
jQuery is a fast, small, and feature-rich JavaScript library designed to simplify client-side scripting of HTML. It is one of the most widely used JavaScript libraries, and is used by many websites to enhance the user experience, by adding interactivity and animation to web pages.

jQuery provides a range of functions and methods that make it easier to manipulate HTML elements, handle events, and perform animations and transitions. It simplifies the process of selecting and manipulating DOM elements, and provides a consistent way of working with AJAX requests and other common web development tasks.

Some of the benefits of using jQuery include:

- Cross-browser compatibility: jQuery provides a unified API that works consistently across different browsers, making it easier to develop cross-browser compatible web applications.

- Simplified DOM manipulation: jQuery's selector syntax and methods make it easier to select and manipulate HTML elements, allowing developers to write more concise and readable code.
- AJAX support: jQuery provides a range of functions for working with AJAX requests, making it easier to fetch and update data on a web page without requiring a full page refresh.
- Large community and plugin ecosystem: jQuery has a large and active community of developers, and a wide range of plugins and extensions are available to extend its functionality.

## Thymeleaf
Thymeleaf is a server-side Java-based templating engine that allows developers to create dynamic HTML templates for web applications. It allows developers to write HTML templates with placeholders for dynamic content, which are then filled in by the server-side code before being sent to the client browser. Thymeleaf provides a range of features for template layout, conditionals, loops, and other common web development tasks. It is often used in conjunction with Spring Framework to build web applications.

## Structure

- `WebSocketController.java`: Controller dell'intera infrastruttura. Fa da server e si occupa di inizializzare i valori di default che poi verranno mostrati sul client web.
- `WasteServiceGuiApplication`: il main dell'intera app web. Esegue l'app spring boot e allo stesso tempo, ha il decoratore @EnableScheduling, che abilita l'invio di un certo messaggio ogni tot secondi.
- `WebSocketConfig.java`: definisce un message broker per mandare indietro messaggi al client alle destinazioni annotate con "/topic". Inoltre definisce uno Stomp endpoint.
- `DataGeneratorService`: dummy server che manda ogni 5 secondi un aggiornamento sulla plastica al path /topic/data.
- `application.properties`: file dove vengono definiti i valori di default 
- `app.js`: viene definito il comportamento del client web, in particolare:
    - la connessione al client Stomp
    - la disconnessione da esso
    - ulteriori funzioni utili da definire ancora
- `index.html`: dashboard che illustra statistiche varie, quali capacità contenitori plastica, carta, etc
