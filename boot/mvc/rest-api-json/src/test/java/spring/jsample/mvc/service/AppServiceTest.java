package spring.jsample.mvc.service;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.model.Application;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAppsTest1() {
        Mockito.when(dao.getApps()).thenReturn(applicationList);
        AssertionsForInterfaceTypes.assertThat(service.getApps()).containsExactlyElementsOf(applicationList);
    }

    @Test
    public void getAppTest1() {
        Mockito.when(dao.getAppById(Mockito.eq(1))).thenReturn(applicationList.get(0));
        AssertionsForInterfaceTypes.assertThat(service.getApp(1)).isEqualTo(applicationList.get(0));
    }

    @Test
    public void addAppTest1() {
        Application app4 = new Application(4, "Application-4", "running");
        Mockito.when(dao.addApp(app4)).thenReturn(app4);
        AssertionsForInterfaceTypes.assertThat(service.addApp(app4)).isEqualTo(app4);
    }

    @Test
    public void deleteAppTest1() {
        Mockito.doNothing().when(dao).deleteApp(4);
        service.deleteApp(4);
    }

    @Test
    public void updateAppTest1() {
        Application app4 = new Application(4, "Application-4", "running");
        Mockito.when(dao.updateApp(app4)).thenReturn(app4);
        AssertionsForInterfaceTypes.assertThat(service.updateApp(app4)).isEqualTo(app4);
    }
}