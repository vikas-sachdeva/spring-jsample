package spring.jsample.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.model.Application;
import spring.jsample.mvc.service.AppService;
import spring.jsample.mvc.util.AppConstants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringJUnitWebConfig
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureDataMongo
public class AppControllerTest {

    @Autowired
    private AppService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppDao appDao;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void initTest() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        appDao.save(new Application(LocalDateTime.now(), LocalDateTime.now(), "1", "Application-1", true));
        appDao.save(new Application(LocalDateTime.now(), LocalDateTime.now(), "2", "Application-2", false));
        appDao.save(new Application(LocalDateTime.now(), LocalDateTime.now(), "3", "Application-3", true));
    }

    @Test
    public void getAppsTest1() throws Exception {
        String expectedResponse = mapper.writeValueAsString(appDao.findAll());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(AppConstants.URI.GET_APPS);
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.content().json(expectedResponse, true));

    }

    @Test
    public void getAppsPageWiseTest1() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        String expectedResponse = mapper.writeValueAsString(appDao.findAll(PageRequest.of(pageNumber, pageSize)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(AppConstants.URI.GET_APPS_PAGE_WISE)
                                                              .param(AppConstants.REQ_PARAM.PAGE_NUMBER, String.valueOf(pageNumber))
                                                              .param(AppConstants.REQ_PARAM.PAGE_SIZE, String.valueOf(pageSize));
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.content().json(expectedResponse, true));
    }

    @Test
    public void getAppsByNamesPageWiseTest1() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        List<String> appNames = Arrays.asList(appDao.findAll().get(0).getName(), appDao.findAll().get(1).getName());
        String expectedResponse = mapper.writeValueAsString(appDao.findByNameIn(appNames, PageRequest.of(pageNumber, pageSize)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(AppConstants.URI.GET_APPS_HAVING_NAMES_PAGE_WISE)
                                                              .param(AppConstants.REQ_PARAM.PAGE_NUMBER, String.valueOf(pageNumber))
                                                              .param(AppConstants.REQ_PARAM.PAGE_SIZE, String.valueOf(pageSize))
                                                              .param(AppConstants.REQ_PARAM.NAMES_LIST, String.join(",", appNames));
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.content().json(expectedResponse, true));
    }

    @Test
    public void getRunningAppsPageWiseTest1() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Application application = new Application();
        application.setRunning(true);
        Example<Application> example = Example.of(application);
        String expectedResponse = mapper.writeValueAsString(appDao.findAll(example, pageable));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(AppConstants.URI.GET_RUNNING_APPS_PAGE_WISE)
                                                              .param(AppConstants.REQ_PARAM.PAGE_NUMBER, String.valueOf(pageNumber))
                                                              .param(AppConstants.REQ_PARAM.PAGE_SIZE, String.valueOf(pageSize));
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.content().json(expectedResponse, true));
    }

    @Test
    public void getRunningAppsByNamesPageWiseTest1() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        List<String> appNames = Arrays.asList(appDao.findAll().get(0).getName(), appDao.findAll().get(1).getName());
        String expectedResponse =
                mapper.writeValueAsString(appDao.findByNameInAndRunning(appNames, true, PageRequest.of(pageNumber, pageSize)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(AppConstants.URI.GET_RUNNING_APPS_HAVING_NAMES_PAGE_WISE)
                                                              .param(AppConstants.REQ_PARAM.PAGE_NUMBER, String.valueOf(pageNumber))
                                                              .param(AppConstants.REQ_PARAM.PAGE_SIZE, String.valueOf(pageSize))
                                                              .param(AppConstants.REQ_PARAM.NAMES_LIST, String.join(",", appNames));
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.content().json(expectedResponse, true));
    }

    @Test
    public void addAppTest1() throws Exception {
        Application app4 = new Application("Application-4", true);
        String jsonApp = mapper.writeValueAsString(app4);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(AppConstants.URI.ADD_APP)
                                                              .content(jsonApp)
                                                              .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(Matchers.notNullValue())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(app4.getName())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.running", Matchers.is(app4.getRunning())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.lastModifiedDateTime", Matchers.is(Matchers.notNullValue())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.createdDateTime", Matchers.is(Matchers.notNullValue())));
    }

    @Test
    public void updateAppTest1() throws Exception {
        Application app3 = appDao.findById("3").orElseThrow();
        app3.setName("Application-3-new");
        String jsonApp = mapper.writeValueAsString(app3);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(AppConstants.URI.UPDATE_APP, app3.getId())
                                                              .content(jsonApp)
                                                              .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(app3.getId())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(app3.getName())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.running", Matchers.is(app3.getRunning())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.lastModifiedDateTime", Matchers.is(Matchers.notNullValue())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.createdDateTime", Matchers.is(app3.getCreatedDateTime().toString())));
    }

    @Test
    public void deleteAppTest1() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(AppConstants.URI.DELETE_APP, appDao.findAll().get(0).getId());
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
