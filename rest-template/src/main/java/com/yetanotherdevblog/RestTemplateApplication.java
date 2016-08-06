package com.yetanotherdevblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@SpringBootApplication
public class RestTemplateApplication {

    @GetMapping("/api")
    public HttpEntity<String> simpleGet(
            @RequestParam(required = false, value = "applicationName") String applicationName) {
        return new HttpEntity<>("Hello");
    }

    @GetMapping("/api/list")
    public HttpEntity<List<String>> simpleGetListOfThings(
            @RequestParam(required = false, value = "applicationName") String applicationName) {
        return new HttpEntity<>(Arrays.asList("Hello", "World"));
    }

    @PostMapping("/api")
    public HttpEntity<String> simplePost(@RequestBody(required = false) String message) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://example.com"));
        return new HttpEntity<>("Hello World", headers);
    }

    @PutMapping("/api")
    public HttpEntity<String> simplePut(@RequestBody String message) {
        return new HttpEntity<>("Hello World");
    }

    @DeleteMapping("/api")
    public HttpEntity<String> simpleDelete() {
        return new HttpEntity<>("Hello World");
    }

    @RequestMapping(value = "/api", method = RequestMethod.OPTIONS)
    public HttpEntity<String> simpleOptions() {
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
        SpringApplication.run(RestTemplateApplication.class, args);
    }

}
