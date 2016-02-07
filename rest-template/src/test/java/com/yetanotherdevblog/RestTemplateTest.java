package com.yetanotherdevblog;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
 */
public class RestTemplateTest {

    private RestTemplate restTemplate = new RestTemplate(
                                new HttpComponentsClientHttpRequestFactory());

    public void test_GetStatus() {
        
        ResponseEntity<String> response
                = restTemplate.getForEntity("http://example.com", String.class);
        HttpStatus status = response.getStatusCode();
        Assert.isTrue(status == HttpStatus.OK);
    }

    public void test_WorkWithListOfThings() {
        ResponseEntity<List<String>> response
                = restTemplate.exchange(
                        "http://example.com",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<String>>() {
                });
        HttpStatus status = response.getStatusCode();
        Assert.isTrue(status == HttpStatus.OK);
    }
    
    // exchange POST
    // exchange PUT
    // exchange blah

    // getForEntity
    // putForEntity
    // blah blah
    
    public void test_SendSpecificHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "Header-Value");
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<List<String>> response
                = restTemplate.exchange(
                        "http://example.com",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<List<String>>() {
                });
        HttpStatus status = response.getStatusCode();
        Assert.isTrue(status == HttpStatus.OK);
    }

    public void test_GetHeaders() {
        ResponseEntity<List<String>> response
                = restTemplate.exchange(
                        "http://example.com",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<String>>() {
                });
        HttpStatus status = response.getStatusCode();
        Assert.isTrue(status == HttpStatus.OK);

        Assert.notNull(response.getHeaders());
        Assert.notNull(response.getHeaders().getOrigin());
        Assert.notNull(response.getHeaders().getOrDefault("someKey", Arrays.asList("test")));
    }

    public void test_SendMultipart() throws UnsupportedEncodingException {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("name 1", "value 1");
        parts.add("name 2", "value 2+1");
        parts.add("name 2", "value 2+2");
//        Resource logo = new ClassPathResource("/org/springframework/http/converter/logo.jpg");
//        parts.add("logo", logo);

        restTemplate.postForLocation("http://example.com/multipart", parts);
    }

    public void test_sendFormEncodedform() throws UnsupportedEncodingException {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name 1", "value 1");
        form.add("name 2", "value 2+1");
        form.add("name 2", "value 2+2");

        restTemplate.postForLocation("http://example.com/form", form);
    }

    public void test_jsonObjectPost() {
//        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
//            @Override
//            public ClientHttpResponse intercept(HttpRequest hr, byte[] bytes, ClientHttpRequestExecution chre) throws IOException {
//                
//                
//                
//                return chre.execute(hr, bytes);
//            }
//        });
    }
    
    public void test_jsonViews() {
        
    }
    
    // DefaultResponseErrorHandler

    public void test_ConfigureTimeoutsOnDefaultFactory() {
        HttpComponentsClientHttpRequestFactory rf =
    (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(1 * 1000);
        rf.setConnectTimeout(1 * 1000);
        rf.setConnectionRequestTimeout(1 * 1000);
    }
    
    
    public void test_addQueryOrPathParams() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        String url = "http://example.com?applicationName={applicationName}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("applicationName", "appName");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
      
        restTemplate.exchange(url, HttpMethod.GET, entity, String.class, "appName");
    }
    
}
