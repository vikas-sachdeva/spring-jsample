package spring.jsample.webflux.service;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.exceptions.ApplicationNotFoundException;
import spring.jsample.webflux.model.Application;

@SpringBootTest
public class AppServiceTest {

    @Autowired
    private AppDao dao;

    @Autowired
    private AppService service;

    @BeforeEach
    public void init() {
        dao.insert(new Application("Application-1", true));
        dao.insert(new Application("Application-2", false));
        dao.insert(new Application("Application-3", true));
    }

    @Test
    public void getAppsTest1() {
        StepVerifier.create(service.getApps()).expectSubscription().consumeNextWith(a -> {
            AssertionsForInterfaceTypes.assertThat(a).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getId()).isNotBlank();
            AssertionsForInterfaceTypes.assertThat(a.getCreatedDateTime()).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getLastModifiedDateTime()).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getVersion()).isNotBlank();
            AssertionsForInterfaceTypes.assertThat(a.getName()).isNotBlank();
            AssertionsForInterfaceTypes.assertThat(a.getRunning()).isNotNull();
        });
    }

    @Test
    public void addAppTest1() {
        Application app4 = new Application("Application - 4", true);
        StepVerifier.create(service.addApp(app4)).expectSubscription().consumeNextWith(a -> {
            AssertionsForInterfaceTypes.assertThat(a).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getId()).isNotBlank();
            AssertionsForInterfaceTypes.assertThat(a.getCreatedDateTime()).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getLastModifiedDateTime()).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getVersion()).isNotBlank();
            AssertionsForInterfaceTypes.assertThat(a.getName()).isNotBlank().isEqualTo(app4.getName());
            AssertionsForInterfaceTypes.assertThat(a.getRunning()).isNotNull().isEqualTo(app4.getRunning());
        });
    }

    @Test
    public void deleteAppTest1() {
        String id = dao.findAll().blockFirst().getId();
        StepVerifier.create(service.deleteApp(id)).verifyComplete();

    }

    @Test
    public void deleteAppTest2() {
        StepVerifier.create(service.deleteApp("4")).verifyError(ApplicationNotFoundException.class);
    }

    @Test
    public void updateAppTest1() {
        Application app4 = new Application("Application-4", true);
        String id = dao.findAll().elementAt(0).block().getId();
        StepVerifier.create(service.updateApp(app4, id)).assertNext(x -> Assertions.assertEquals(app4, x))
                    .verifyComplete();

    }

    @Test
    public void updateAppTest3() {
        Application app4 = new Application("Application-4", true);
        StepVerifier.create(service.updateApp(app4, "4")).verifyError(ApplicationNotFoundException.class);
    }
}

