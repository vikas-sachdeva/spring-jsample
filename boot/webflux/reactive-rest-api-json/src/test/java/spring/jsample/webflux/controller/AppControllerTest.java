package spring.jsample.webflux.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jsample.webflux.model.Application;
import spring.jsample.webflux.service.AppService;
import spring.jsample.webflux.util.AppConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
public class AppControllerTest {

    @MockBean
    private AppService service;

    @Autowired
    private WebTestClient webTestClient;

    private static List<Application> applicationList;

    @BeforeAll
    private static void init() {
        Application app1 = new Application(1, "Application-1", "running");
        Application app2 = new Application(2, "Application-2", "stopped");
        Application app3 = new Application(3, "Application-3", "running");
        applicationList = new ArrayList<>(Arrays.asList(app1, app2, app3));
    }

    @Test
    public void getAppsTest1() throws Exception {
        Mockito.when(service.getApps()).thenReturn(Flux.fromIterable(applicationList));
        webTestClient.get()
                     .uri(AppConstants.URI.GET_APPS)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(Application.class)
                     .hasSize(applicationList.size())
                     .isEqualTo(applicationList);
    }

    @Test
    public void getAppTest1() throws Exception {
        Application app = applicationList.get(0);
        Mockito.when(service.getApp(app.getId())).thenReturn(Mono.just(app));
        webTestClient.get()
                     .uri(AppConstants.URI.GET_APP, app.getId())
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Application.class)
                     .isEqualTo(app);
    }

    @Test
    public void addAppTest1() throws Exception {
        Application app = new Application(4, "Application-4", "running");
        Mockito.when(service.addApp(Mockito.any(Application.class))).thenReturn(Mono.just(app));
        webTestClient.post()
                     .uri(AppConstants.URI.ADD_APP)
                     .bodyValue(app)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Application.class)
                     .isEqualTo(app);
    }

    @Test
    public void updateAppTest1() throws Exception {
        Application app = new Application(2, "Application-new", "running");
        Mockito.when(service.updateApp(Mockito.any(Application.class))).thenReturn(Mono.just(app));
        webTestClient.put()
                     .uri(AppConstants.URI.UPDATE_APP, app.getId())
                     .bodyValue(app)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Application.class)
                     .isEqualTo(app);
    }

    @Test
    public void deleteAppTest1() throws Exception {
        Mockito.when(service.deleteApp(Mockito.anyInt())).thenReturn(Mono.empty());
        webTestClient.delete().uri(AppConstants.URI.DELETE_APP, 2).exchange().expectStatus().isNoContent();
    }
}
