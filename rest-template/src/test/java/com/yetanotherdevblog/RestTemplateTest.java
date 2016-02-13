package com.yetanotherdevblog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.util.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RestTemplateTest {

    @Value("http://localhost:${local.server.port}/api")
    private String baseUrl;

    @Test
    public void test_GET() throws URISyntaxException {
        // tag::GET[]
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        ResponseEntity<String> responseExchange = restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class);

        URI uri = new URI(baseUrl);
        ResponseEntity<String> responseURI = restTemplate.getForEntity(uri, String.class);
        ResponseEntity<String> responseExchangeURI = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        // end::GET[]

        isTrue(response.getStatusCode() == HttpStatus.OK);
        isTrue(responseExchange.getStatusCode() == HttpStatus.OK);

        isTrue(responseURI.getStatusCode() == HttpStatus.OK);
        isTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void test_POST() throws URISyntaxException {
        // tag::POST[]
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl);

        String body = "The Body";

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, body, String.class);

        HttpEntity<String> request = new HttpEntity<>(body);
        ResponseEntity<String> responseExchange = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);

        ResponseEntity<String> responseURI = restTemplate.postForEntity(uri, body, String.class);
        ResponseEntity<String> responseExchangeURI = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        // end::POST[]

        isTrue(response.getStatusCode() == HttpStatus.OK);
        isTrue(responseURI.getStatusCode() == HttpStatus.OK);
        isTrue(responseExchange.getStatusCode() == HttpStatus.OK);
        isTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void test_PUT() throws URISyntaxException {
        // tag::PUT[]
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl);

        String body = "The Body";

        restTemplate.put(baseUrl, body);
        restTemplate.put(uri, body);
        // end::PUT[]
    }

    @Test
    public void test_DELETE() throws URISyntaxException {
        // tag::DELETE[]
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl);

        restTemplate.delete(baseUrl);
        restTemplate.delete(uri);
        // end::DELETE[]
    }

    @Test
    public void test_OPTIONS() throws URISyntaxException {
        // tag::OPTIONS_API_CALL[]
        RestTemplate restTemplate = new RestTemplate();
        Set<HttpMethod> allowHeaders = restTemplate.optionsForAllow(baseUrl);

        URI uri = new URI(baseUrl);
        Set<HttpMethod> allowHeadersURI = restTemplate.optionsForAllow(uri);
        // end::OPTIONS_API_CALL[]

        isTrue(!allowHeaders.isEmpty());
        isTrue(!allowHeadersURI.isEmpty());
    }

    @Test
    public void test_postForLocation() throws URISyntaxException {
        // tag::getLocation[]
        RestTemplate restTemplate = new RestTemplate();
        URI location = restTemplate.postForLocation(baseUrl, null);
        // end::getLocation[]

        isTrue(location.equals(new URI("http://example.com")));
    }

    @Test
    public void test_GetHeaders() {
        baseUrl = baseUrl + "/list";

        // tag::getHeaders[]
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<String>> listOfString = new ParameterizedTypeReference<List<String>>() {
        };
        ResponseEntity<List<String>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null, listOfString);
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        long date = headers.getDate();
        List<String> getOrDefault = headers.getOrDefault("X-Forwarded", Collections.singletonList("Does not exists"));
        // end::getHeaders[]

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
        baseUrl = baseUrl + "/list";

        // tag::WorkWithListOfThings[]
        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<List<String>> listOfStrings = new ParameterizedTypeReference<List<String>>() {
        };

        ResponseEntity<List<String>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null, listOfStrings);
        // end::WorkWithListOfThings[]

        isTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void test_addQueryOrPathParams() {
        baseUrl = baseUrl + "?applicationName={applicationName}";

        // tag::addQueryOrPathParams[]
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("applicationName", "appName");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // first way using UriComponentsBuilder
        ResponseEntity<String> response = // <1>
                restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

        // second way using exchange api
        response = // <2>
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class, "appName");

        // end::addQueryOrPathParams[]

        isTrue(response.getStatusCode() == HttpStatus.OK);
        isTrue(response.getStatusCode() == HttpStatus.OK);
    }

    // How to get Status
    @Test
    public void test_GetStatus() {
        // tag::getStatus[]
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        HttpStatus status = response.getStatusCode();
        // end::getStatus[]

        isTrue(status == HttpStatus.OK);
    }

    // 2nd blog post
    // Timeouts
    // Get Headers
    // follow redirects
    // unfollow redirects

    @Ignore
    public void test_ConfigureTimeoutsOnDefaultRequestFactory_SimpleClientHttpRequestFactory() {
        // tag::settingTimeouts[]
        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(1000);
        rf.setConnectTimeout(1000);
        // end::settingTimeouts[]
    }

    @Test
    public void test_SendSpecificHeaders() {
        // tag::sendCustomHeader[]
        baseUrl = baseUrl + "/list";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "Header-Value");
        HttpEntity request = new HttpEntity(headers);

        ParameterizedTypeReference<List<String>> listOfString = new ParameterizedTypeReference<List<String>>() {};
        ResponseEntity<List<String>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, request, listOfString);
        HttpStatus status = response.getStatusCode();
        // end::sendCustomHeader[]

        isTrue(status == HttpStatus.OK);
    }


    @Test
    public void test_followRedirects() {
        // tag::followRedirects[]
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("http://bit.ly/1NZCQPn", HttpMethod.GET, null, String.class);
        isTrue(response.getStatusCode() == HttpStatus.OK);
        // end::followRedirects[]
    }

    @Test
    public void test_doNotfollowRedirects() {
        // tag::test_doNotfollowRedirects[]
        /**
         * Override behaviour of {@linkplain SimpleClientHttpRequestFactory} by overriding #setInstanceFollowRedirects
         */
        class SimpleClientHttpReqFactoryWithoutRedirects extends SimpleClientHttpRequestFactory {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        }

        RestTemplate restTemplate1 = new RestTemplate(new SimpleClientHttpReqFactoryWithoutRedirects());
        ResponseEntity<String> response = restTemplate1.exchange("http://bit.ly/1NZCQPn", HttpMethod.GET, null, String.class);
        isTrue(response.getStatusCode() == HttpStatus.MOVED_PERMANENTLY);
        // end::test_doNotfollowRedirects[]
    }


//    private void addAuthentication(String username, String password) {
//        if (username == null) {
//            return;
//        }
//        List<ClientHttpRequestInterceptor> interceptors = Collections
//                .<ClientHttpRequestInterceptor>singletonList(
//                        new BasicAuthorizationInterceptor(username, password));
//        setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(),
//                interceptors));
//    }

    @Ignore
    public void test_SendMultipart() throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("name 1", "value 1");
        parts.add("name 2", "value 2+1");
        parts.add("name 2", "value 2+2");
//        Resource logo = new ClassPathResource("/org/springframework/http/converter/logo.jpg");
//        parts.add("logo", logo);

        restTemplate.postForLocation("http://example.com/multipart", parts);
    }

    @Ignore
    public void test_sendFormEncodedform() throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name 1", "value 1");
        form.add("name 2", "value 2+1");
        form.add("name 2", "value 2+2");

        restTemplate.postForLocation("http://example.com/form", form);
    }

    @Ignore
    public void test_jsonObjectPost() {
//        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
//            @Override
//            public ClientHttpResponse intercept(HttpRequest hr, byte[] bytes, ClientHttpRequestExecution chre) throws IOException {
//                return chre.execute(hr, bytes);
//            }
//        });
    }

    @Ignore
    public void test_jsonViews() {
        // do your thingy
    }

    public interface PublicView {
    }

    public static class BigObject {

        @JsonView(PublicView.class)
        private String field1;

        private String field2;

        private String field3;

        @JsonView(PublicView.class)
        private String field4;

        public String getField4() {
            return field4;
        }

        public void setField4(String field4) {
            this.field4 = field4;
        }

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }
    }

    // DefaultResponseErrorHandler

    /**
     * Basic Authorization interceptor
     */
    public static class BasicAuthorizationInterceptor
            implements ClientHttpRequestInterceptor {

        private final String username;
        private final String password;

        BasicAuthorizationInterceptor(String username, String password) {
            this.username = username;
            this.password = (password == null ? "" : password);
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            String token = Base64Utils.encodeToString(
                    (this.username + ":" + this.password).getBytes("UTF-8"));
            request.getHeaders().add("Authorization", "Basic " + token);
            return execution.execute(request, body);
        }

    }

}
