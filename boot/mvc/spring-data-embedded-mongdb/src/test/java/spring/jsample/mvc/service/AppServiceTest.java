package spring.jsample.mvc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.exceptions.ApplicationNotFoundException;
import spring.jsample.mvc.exceptions.ConcurrentModificationException;
import spring.jsample.mvc.model.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AppServiceTest {

    @MockBean
    private AppDao dao;

    @InjectMocks
    private AppService service;

    private static List<Application> applicationList;

    @BeforeAll
    private static void init() {
        Application app1 = new Application("1", "Application-1", true);
        Application app2 = new Application("2", "Application-2", false);
        Application app3 = new Application("3", "Application-3", true);
        applicationList = new ArrayList<>(Arrays.asList(app1, app2, app3));
    }

    @BeforeEach
    private void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAppsTest1() {
        Mockito.when(dao.findAll()).thenReturn(applicationList);
        List<Application> appList = service.getApps();
        Assertions.assertArrayEquals(applicationList.toArray(), appList.toArray());
    }

    @Test
    public void getAppsPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Application> emptyPage = Page.empty();
        Mockito.when(dao.findAll(pageable)).thenReturn(emptyPage);
        Page<Application> page = service.getAppsPageWise(pageNumber, pageSize);
        Assertions.assertEquals(emptyPage, page);
    }

    @Test
    public void getAppsByNamesPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 10;
        List<String> appNames = Arrays.asList("app-1", "app-2");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Application> emptyPage = Page.empty();
        Mockito.when(dao.findByNameIn(appNames, pageable)).thenReturn(emptyPage);
        Page<Application> page = service.getAppsByNamesPageWise(appNames, pageNumber, pageSize);
        Assertions.assertEquals(emptyPage, page);
    }

    @Test
    public void getRunningAppsPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 10;
        Page<Application> emptyPage = Page.empty();
        Mockito.when(dao.findAll(Mockito.any(Example.class), Mockito.any(Pageable.class))).thenReturn(emptyPage);
        Page<Application> page = service.getRunningAppsPageWise(pageNumber, pageSize);
        Assertions.assertEquals(emptyPage, page);
    }

    @Test
    public void getRuningAppsByNamesPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 10;
        List<String> appNames = Arrays.asList("app-1", "app-2");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Application> emptyPage = Page.empty();
        Mockito.when(dao.findByNameInAndRunning(appNames, true, pageable)).thenReturn(emptyPage);
        Page<Application> page = service.getRunningAppsByNamesPageWise(appNames, pageNumber, pageSize);
        Assertions.assertEquals(emptyPage, page);
    }

    @Test
    public void addAppTest1() {
        Application app4 = new Application("4", "Application-4", true);
        Mockito.when(dao.save(app4)).thenReturn(app4);
        Application acutalApp = service.addApp(app4);
        Assertions.assertEquals(app4, acutalApp);
    }

    @Test
    public void deleteAppTest1() {
        Application app4 = new Application("4", "Application-4", true);
        Mockito.when(dao.findById("4")).thenReturn(Optional.of(app4));
        Mockito.doNothing().when(dao).deleteById("4");
        service.deleteApp("4");

    }

    @Test
    public void deleteAppTest2() {
        Mockito.when(dao.findById("4")).thenReturn(Optional.empty());
        Assertions.assertThrows(ApplicationNotFoundException.class, () -> service.deleteApp("4"));

    }

    @Test
    public void updateAppTest1() {
        Application app4 = new Application("4", "Application-4", true);
        app4.setLastModifiedDate(new Date());
        Mockito.when(dao.save(app4)).thenReturn(app4);
        Mockito.when(dao.findById("4")).thenReturn(Optional.of(app4));
        Application acutalApp = service.updateApp(app4, "4");
        Assertions.assertEquals(app4, acutalApp);

    }

    @Test
    public void updateAppTest2() {
        Application app4 = new Application("4", "Application-4", true);
        app4.setLastModifiedDate(new Date());
        Mockito.when(dao.save(app4)).thenReturn(app4);
        Application dbApp = new Application("5", "Application-4", true);
        dbApp.setLastModifiedDate(new Date(39393939L));
        Mockito.when(dao.save(dbApp)).thenReturn(dbApp);
        Optional<Application> dbOptional = Optional.of(dbApp);
        Mockito.when(dao.findById("4")).thenReturn(dbOptional);
        Assertions.assertThrows(ConcurrentModificationException.class, () -> service.updateApp(app4, "4"));
    }

    @Test
    public void updateAppTest3() {
        Application app4 = new Application("4", "Application-4", true);
        app4.setLastModifiedDate(new Date());
        Mockito.when(dao.save(app4)).thenReturn(app4);
        Mockito.when(dao.findById("4")).thenReturn(Optional.empty());
        Assertions.assertThrows(ApplicationNotFoundException.class, () -> service.updateApp(app4, "4"));
    }

}