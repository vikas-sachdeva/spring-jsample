package spring.jsample.webflux.service;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.exceptions.ApplicationNotFoundException;
import spring.jsample.webflux.model.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest
public class AppServiceTest {

    @Autowired
    private AppDao dao;

    @Autowired
    private AppService service;

    @BeforeEach
    public void init() {
        dao.deleteAll().block();
        List<Application> apps = new ArrayList<>();
        apps.add(new Application("Application-1", true));
        apps.add(new Application("Application-2", false));
        apps.add(new Application("Application-3", true));
        dao.insert(apps).blockLast();
    }

    @Test
    public void getAppsTest1() {
        List<Application> apps = Objects.requireNonNull(dao.findAll().collect(Collectors.toList()).block());
        StepVerifier.create(service.getApps())
                    .expectSubscription()
                    .recordWith(ArrayList::new)
                    .expectNextCount(apps.size())
                    .consumeRecordedWith(l -> {
                        AssertionsForInterfaceTypes.assertThat(l).isNotNull().hasSize(apps.size());
                        AssertionsForInterfaceTypes.assertThat(l).containsExactlyElementsOf(apps);
                    })
                    .verifyComplete();
    }

    @Test
    public void getAppTest1() {
        Application app = Objects.requireNonNull(dao.findAll().elementAt(0).block());
        StepVerifier.create(service.getApp(app.getId()))
                    .expectSubscription()
                    .consumeNextWith(a -> AssertionsForInterfaceTypes.assertThat(a).isEqualTo(app))
                    .verifyComplete();
    }

    @Test
    public void getAppTest2() {
        StepVerifier.create(service.getApp("4")).verifyError(ApplicationNotFoundException.class);
    }

    @Test
    public void addAppTest1() {
        Application app = new Application("Application - 4", true);
        StepVerifier.create(service.addApp(app)).expectSubscription().consumeNextWith(a -> {
            AssertionsForInterfaceTypes.assertThat(a).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getId()).isNotBlank();
            AssertionsForInterfaceTypes.assertThat(a.getCreatedDateTime()).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getLastModifiedDateTime()).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getVersion()).isEqualTo(0L);
            AssertionsForInterfaceTypes.assertThat(a.getName()).isNotBlank().isEqualTo(app.getName());
            AssertionsForInterfaceTypes.assertThat(a.getRunning()).isNotNull().isEqualTo(app.getRunning());
        }).verifyComplete();
    }

    @Test
    public void deleteAppTest1() {
        String id = Objects.requireNonNull(dao.findAll().blockFirst()).getId();
        StepVerifier.create(service.deleteApp(id)).verifyComplete();
    }

    @Test
    public void deleteAppTest2() {
        StepVerifier.create(service.deleteApp("4")).verifyError(ApplicationNotFoundException.class);
    }

    @Test
    public void updateAppTest1() {
        Application app = Objects.requireNonNull(dao.findAll().elementAt(0).block());
        app.setName("new name");
        app.setRunning(false);
        StepVerifier.create(service.updateApp(app, app.getId())).expectSubscription().consumeNextWith(a -> {
            AssertionsForInterfaceTypes.assertThat(a).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a).isEqualToIgnoringGivenFields(app, "lastModifiedDateTime", "version");
            AssertionsForInterfaceTypes.assertThat(a.getVersion()).isEqualTo(app.getVersion() + 1);
            AssertionsForInterfaceTypes.assertThat(a.getLastModifiedDateTime()).isNotNull().isNotEqualTo(app.getLastModifiedDateTime());
        }).verifyComplete();
    }

    @Test
    public void updateAppTest3() {
        Application app = new Application("Application-4", true);
        StepVerifier.create(service.updateApp(app, "4")).verifyError(ApplicationNotFoundException.class);
    }
}