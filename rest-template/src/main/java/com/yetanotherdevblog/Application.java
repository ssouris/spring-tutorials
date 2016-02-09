package com.yetanotherdevblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@RestController
@SpringBootApplication
public class Application {

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public HttpEntity<String> simpleGet() {
        return new HttpEntity<>("Hello World");
    }

    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public HttpEntity<String> simplePost(@RequestBody String message) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://example.com"));
        return new HttpEntity<>("Hello World", headers);
    }

    @RequestMapping(value = "/api", method = RequestMethod.PUT)
    public HttpEntity<String> simplePut(@RequestBody String message) {
        return new HttpEntity<>("Hello World");
    }

    @RequestMapping(value = "/api", method = RequestMethod.DELETE)
    public HttpEntity<String> simpleDelete(@RequestBody String message) {
        return new HttpEntity<>("Hello World");
    }

    @RequestMapping(value = "/api", method = RequestMethod.PATCH)
    public HttpEntity<String> simplePatch(@RequestBody String message) {
        return new HttpEntity<>("Hello World");
    }

    @RequestMapping(value = "/api", method = RequestMethod.OPTIONS)
    public HttpEntity<String> simpleOptions(@RequestBody String message) {
        HttpHeaders headers = new HttpHeaders();

        Set<HttpMethod> allowedOps = new HashSet<>();
        allowedOps.add(HttpMethod.GET);
        allowedOps.add(HttpMethod.POST);
        allowedOps.add(HttpMethod.PUT);
        allowedOps.add(HttpMethod.DELETE);
        allowedOps.add(HttpMethod.PATCH);
        allowedOps.add(HttpMethod.OPTIONS);
        headers.setAllow(allowedOps);
        return new HttpEntity<>("Hello World", headers);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
