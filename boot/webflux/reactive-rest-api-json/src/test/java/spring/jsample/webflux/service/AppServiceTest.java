package spring.jsample.webflux.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.model.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class AppServiceTest {

    @MockBean
    private AppDao dao;

    @InjectMocks
    private AppService service;

    private static List<Application> applicationList;

    @BeforeAll
    private static void init() {
        Application app1 = new Application(1, "Application-1", "running");
        Application app2 = new Application(2, "Application-2", "stopped");
        Application app3 = new Application(3, "Application-3", "running");
        applicationList = new ArrayList<>(Arrays.asList(app1, app2, app3));
    }

    @BeforeEach
    private void initTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAppsTest1() {
        Mockito.when(dao.getApps()).thenReturn(Flux.fromIterable(applicationList));
        StepVerifier.create(service.getApps())
                    .expectNext(applicationList.get(0))
                    .expectNext(applicationList.get(1))
                    .expectNext(applicationList.get(2))
                    .verifyComplete();
    }

    @Test
    public void getAppTest1() {
        int id = 2;
        Mockito.when(dao.getAppById(id)).thenReturn(Mono.just(applicationList.get(id - 1)));
        StepVerifier.create(service.getApp(id))
                    .expectNext(applicationList.get(id - 1))
                    .verifyComplete();
    }

    @Test
    public void addAppTest1() {
        Application app = new Application(4, "Application-4", "running");
        Mockito.when(dao.addApp(app)).thenReturn(Mono.just(app));
        StepVerifier.create(service.addApp(app)).expectNext(app).verifyComplete();
    }

    @Test
    public void deleteAppTest1() {
        int id = 2;
        Mockito.when(dao.deleteApp(id)).thenReturn(Mono.empty());
        StepVerifier.create(service.deleteApp(id)).verifyComplete();
    }

    @Test
    public void updateAppTest1() {
        Application app = new Application(4, "Application-4", "running");
        Mockito.when(dao.updateApp(app)).thenReturn(Mono.just(app));
        StepVerifier.create(service.updateApp(app)).expectNext(app).verifyComplete();
    }
}