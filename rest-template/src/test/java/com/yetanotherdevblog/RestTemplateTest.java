package com.yetanotherdevblog;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.util.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestTemplateApplication.class)
@WebAppConfiguration
@IntegrationTest
public class RestTemplateTest {

    @Value("http://localhost:${local.server.port}/api")
    private String baseUrl;

    @Test
    public void test_GET() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        ResponseEntity<String> responseExchange = restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class);

        URI uri = new URI(baseUrl);
        ResponseEntity<String> responseURI = restTemplate.getForEntity(uri, String.class);
        ResponseEntity<String> responseExchangeURI = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

        isTrue(response.getStatusCode()==HttpStatus.OK);
        isTrue(responseExchange.getStatusCode()==HttpStatus.OK);

        isTrue(responseURI.getStatusCode()==HttpStatus.OK);
        isTrue(responseExchangeURI.getStatusCode()==HttpStatus.OK);
    }

    @Test
    public void test_POST() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl);

        String body = "The Body";

        ResponseEntity<String> response=restTemplate.postForEntity(baseUrl, body, String.class);

        HttpEntity<String> request = new HttpEntity<>(body);
        ResponseEntity<String> responseExchange=restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);

        ResponseEntity<String> responseURI=restTemplate.postForEntity(uri, body, String.class);
        ResponseEntity<String> responseExchangeURI=restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

        isTrue(response.getStatusCode()==HttpStatus.OK);
        isTrue(responseURI.getStatusCode()==HttpStatus.OK);
        isTrue(responseExchange.getStatusCode()==HttpStatus.OK);
        isTrue(responseExchangeURI.getStatusCode()==HttpStatus.OK);
    }

    @Test
    public void test_PUT() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl);

        String body = "The Body";

        restTemplate.put(baseUrl, body);
        restTemplate.put(uri, body);
    }

    @Test
    public void test_DELETE() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl);

        restTemplate.delete(baseUrl);
        restTemplate.delete(uri);
    }

    @Test
    public void test_OPTIONS() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        Set<HttpMethod> allowHeaders = restTemplate.optionsForAllow(baseUrl);

        URI uri = new URI(baseUrl);
        Set<HttpMethod> allowHeadersURI = restTemplate.optionsForAllow(uri);

        isTrue(!allowHeaders.isEmpty());
        isTrue(!allowHeadersURI.isEmpty());
    }

    @Test
    public void test_postForLocation() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI location = restTemplate.postForLocation(baseUrl, null);

        isTrue(location.equals(new URI("http://example.com")));
    }

    @Test
    public void test_GetHeaders() {
        baseUrl=baseUrl+"/list";

        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<String>> listOfString = new ParameterizedTypeReference<List<String>>() {};
        ResponseEntity<List<String>> response= restTemplate.exchange(baseUrl,HttpMethod.GET,null, listOfString);
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        long date = headers.getDate();
        List<String> getOrDefault = headers.getOrDefault("X-Forwarded", Collections.singletonList("Does not exists"));

        HttpStatus status = response.getStatusCode();
        notNull(headers);
        notNull(contentType);
        notNull(date);
        notNull(getOrDefault);
        isTrue(status == HttpStatus.OK);
    }

    // How to get Json Array (List of things on first level)
    // How to get Headers
    @Test
    public void test_WorkWithListOfThings() {
        baseUrl=baseUrl+"/list";

        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<List<String>> listOfStrings = new ParameterizedTypeReference<List<String>>() {};

        ResponseEntity<List<String>> response = restTemplate.exchange(baseUrl,HttpMethod.GET,null,listOfStrings);

        isTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void test_addQueryOrPathParams() {
        baseUrl = baseUrl+"?applicationName={applicationName}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("applicationName", "appName");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // first way using UriComponentsBuilder
        ResponseEntity<String> response = 
                restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

        isTrue(response.getStatusCode() == HttpStatus.OK);
        
        // second way using exchange api
        response = 
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class, "appName");

        isTrue(response.getStatusCode() == HttpStatus.OK);
    }

    // How to get Status
    @Test
    public void test_GetStatus() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        HttpStatus status = response.getStatusCode();

        isTrue(status == HttpStatus.OK);
    }
    
}
