package com.yetanotherdev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetanotherdev.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoDbApplication.class)
public class PersonControllerTests {


    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
    }

    @Test
    public void testAll() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/people")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().is2xxSuccessful());

        Person person = new Person();
        person.setId(UUID.randomUUID().toString());
        person.setFirstname("FirstName");
        person.setLastname("LastName");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/people")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(person))
        ).andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/people/{id}", person.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(person))
        ).andExpect(status().is2xxSuccessful());


    }


}
