package spring.jsample.mvc.service;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.exceptions.ApplicationNotFoundException;
import spring.jsample.mvc.model.Application;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class AppServiceTest {

    @Autowired
    private AppDao appDao;

    @Autowired
    private AppService service;

    @BeforeEach
    public void initTest() {
        appDao.deleteAll();
        appDao.insert(new Application("Application-1", true));
        appDao.insert(new Application("Application-2", false));
        appDao.insert(new Application("Application-3", true));
    }

    @Test
    public void getAppsTest1() {
        AssertionsForInterfaceTypes.assertThat(service.getApps()).containsExactlyElementsOf(appDao.findAll());
    }

    @Test
    public void getAppTest1() {
        Application app = appDao.findAll().get(0);
        AssertionsForInterfaceTypes.assertThat(service.getApp(app.getId())).isEqualTo(app);
    }

    @Test
    public void getAppTest2() {
        AssertionsForInterfaceTypes.assertThatExceptionOfType(ApplicationNotFoundException.class).isThrownBy(() -> service.getApp("4"));
    }

    @Test
    public void getAppsPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        AssertionsForInterfaceTypes.assertThat(service.getAppsPageWise(pageNumber, pageSize))
                                   .containsExactlyElementsOf(appDao.findAll(pageable));
    }

    @Test
    public void getAppsByNamesPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 10;
        List<String> appNames = Arrays.asList(appDao.findAll().get(0).getName(), appDao.findAll().get(1).getName());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        AssertionsForInterfaceTypes.assertThat(service.getAppsByNamesPageWise(appNames, pageNumber, pageSize))
                                   .containsExactlyElementsOf(appDao.findByNameIn(appNames, pageable));
    }

    @Test
    public void getRunningAppsPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        AssertionsForInterfaceTypes.assertThat(service.getRunningAppsPageWise(pageNumber, pageSize))
                                   .containsExactlyElementsOf(appDao.findAll(pageable).filter(Application::getRunning).toList());
    }

    @Test
    public void getRunningAppsByNamesPageWiseTest1() {
        int pageNumber = 0;
        int pageSize = 10;
        List<String> appNames = Arrays.asList(appDao.findAll().get(0).getName(), appDao.findAll().get(1).getName());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        AssertionsForInterfaceTypes.assertThat(service.getRunningAppsByNamesPageWise(appNames, pageNumber, pageSize))
                                   .containsExactlyElementsOf(appDao.findByNameInAndRunning(appNames, true, pageable));
    }

    @Test
    public void addAppTest1() {
        Application app = new Application("Application-4", true);
        AssertionsForInterfaceTypes.assertThat(service.addApp(app))
                                   .isEqualToComparingOnlyGivenFields(app, "name", "running")
                                   .extracting(Application::getCreatedDateTime, Application::getLastModifiedDateTime,
                                               Application::getVersion, Application::getId, Application::getName, Application::getRunning)
                                   .doesNotContainNull();
    }

    @Test
    public void deleteAppTest1() {
        Application app = appDao.findAll().get(0);
        service.deleteApp(app.getId());
        AssertionsForInterfaceTypes.assertThat(appDao.findById(app.getId())).isEmpty();
    }

    @Test
    public void deleteAppTest2() {
        AssertionsForInterfaceTypes.assertThatExceptionOfType(ApplicationNotFoundException.class).isThrownBy(() -> service.deleteApp("4"));
    }

    @Test
    public void updateAppTest1() {
        Application app = appDao.findAll().get(0);
        app.setName("Application-4");
        AssertionsForInterfaceTypes.assertThat(service.updateApp(app, app.getId()))
                                   .isEqualToIgnoringGivenFields(app, "lastModifiedDateTime", "version")
                                   .extracting(Application::getLastModifiedDateTime, Application::getVersion)
                                   .doesNotContainNull().doesNotContain(app.getVersion(), app.getLastModifiedDateTime());

    }

    @Test
    public void updateAppTest2() {
        Application app = appDao.findAll().get(0);
        AssertionsForInterfaceTypes.assertThatExceptionOfType(ApplicationNotFoundException.class)
                                   .isThrownBy(() -> service.updateApp(app, "5"));
    }
}