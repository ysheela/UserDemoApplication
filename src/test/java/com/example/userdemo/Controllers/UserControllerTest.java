package com.example.userdemo.Controllers;

import com.example.userdemo.UserdemoApplicationTests;
import com.example.userdemo.Util.TestUtil;
import com.example.userdemo.domain.User;
import com.example.userdemo.repository.UserRepository;
import com.mongodb.DuplicateKeyException;
import com.mongodb.ServerAddress;
import org.bson.BsonDocument;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
@EnableSpringDataWebSupport
@DataMongoTest
public class UserControllerTest {

    @Mock
    UserRepository userRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private Pageable pageRequest;

    private Page<User> pageUsers;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        List<User> users = new ArrayList<>();
        users.add(new User( "sheela", "yedlapalli", "sy@gmail.com"));
        users.add(new User( "raj", "eti", "ry@gmail.com"));

        Sort sort = new Sort(Sort.Direction.ASC, "LastName");
        pageRequest = new PageRequest(1, 5, sort);
        pageUsers = new PageImpl<>(users, pageRequest, 15);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setHandlerExceptionResolvers(TestUtil.WithExceptionControllerAdvice())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                })
                .build();
    }

    @Test
    public void testFindAll() throws Exception{
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(pageUsers);
        mockMvc.perform(get("/user/all"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].firstName", is("sheela")))
                .andExpect(jsonPath("$.content[0].lastName", is("yedlapalli")))
                .andExpect(jsonPath("$.content[0].email", is("sy@gmail.com")))
                .andExpect(jsonPath("$.content[1].firstName", is("raj")))
                .andExpect(jsonPath("$.content[1].lastName", is("eti")))
                .andExpect(jsonPath("$.content[1].email", is("ry@gmail.com")))
                .andExpect(jsonPath("$.numberOfElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.totalElements", is(15)));


        verify(userRepository, times(1)).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void test_insert_Fail_Duplicates() throws Exception {
        User user = new User( "sheela", "yedlapalli", "sy@gmail.com");
        when(userRepository.insert(user)).thenThrow(new com.mongodb.DuplicateKeyException(new BsonDocument(),
                new ServerAddress(), null));
        mockMvc.perform(put("/user/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andDo(print())
                .andExpect(content().string("Cannot add user with same first and last name"));
    }

    @Test
    public void test_update_Fail_Duplicates() throws Exception {
        User user = new User( "sheela", "yedlapalli", "sy@gmail.com");
        when(userRepository.save(user)).thenThrow(new com.mongodb.DuplicateKeyException(new BsonDocument(),
                new ServerAddress(), null));
        mockMvc.perform(post("/user/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andDo(print())
                .andExpect(content().string("Cannot update user with same first and last name"));
    }

    @Test
    public void test_retrieve_user_empty() throws Exception {
        when(userRepository.findOneById("1")).thenReturn(null);
        mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().is(500));
    }

    @Test
    public void test_retrieve_valid_user() throws Exception {
        User user = new User( "sheela", "yedlapalli", "sy@gmail.com");
        when(userRepository.findOneById("1")).thenReturn(user);
        mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void test_perform_delete_user() throws Exception {
        User user = new User( "sheela", "yedlapalli", "sy@gmail.com");
        mockMvc.perform(delete("/user/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userRepository, times(1)).delete("1");
        verifyNoMoreInteractions(userRepository);
    }

}
