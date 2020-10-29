package spring.jsample.webflux.controller;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.model.Application;
import spring.jsample.webflux.util.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureWebTestClient
public class AppControllerTest {

    @Autowired
    private AppDao appDao;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void init() {
        appDao.deleteAll().block();
        List<Application> apps = new ArrayList<>();
        apps.add(new Application("Application-1", true));
        apps.add(new Application("Application-2", false));
        apps.add(new Application("Application-3", true));
        appDao.insert(apps).blockLast();
    }

    @Test
    public void getAppsTest1() {
        List<Application> apps = Objects.requireNonNull(appDao.findAll().collect(Collectors.toList()).block());
        webTestClient.get().uri(AppConstants.URI.GET_APPS).exchange().expectStatus().isOk()
                     .expectBodyList(Application.class).isEqualTo(apps);
    }

    @Test
    public void getAppTest1() {
        Application app = Objects.requireNonNull(appDao.findAll().blockLast());
        webTestClient.get().uri(AppConstants.URI.GET_APP, app.getId()).exchange().expectStatus().isOk()
                     .expectBody(Application.class).isEqualTo(app);
    }

    @Test
    public void addAppTest1() {
        Application app = new Application("Application-4", true);
        webTestClient.post().uri(AppConstants.URI.ADD_APP).bodyValue(app).exchange().expectStatus().isOk()
                     .expectBody(Application.class).consumeWith(r -> {
            Application application = r.getResponseBody();
            AssertionsForInterfaceTypes.assertThat(application).isNotNull();
            AssertionsForInterfaceTypes.assertThat(application.getName()).isEqualTo(app.getName());
            AssertionsForInterfaceTypes.assertThat(application.getRunning()).isEqualTo(app.getRunning());
            AssertionsForInterfaceTypes.assertThat(application.getVersion()).isEqualTo(0L);
            AssertionsForInterfaceTypes.assertThat(application)
                                       .extracting(Application::getId, Application::getLastModifiedDateTime,
                                                   Application::getCreatedDateTime)
                                       .doesNotContainNull();
        });
    }

    @Test
    public void updateAppTest1() {
        Application app = Objects.requireNonNull(appDao.findAll().blockFirst());
        app.setName("new app new");
        app.setRunning(false);
        webTestClient.put().uri(AppConstants.URI.UPDATE_APP, app.getId()).bodyValue(app).exchange().expectStatus().isOk()
                     .expectBody(Application.class).consumeWith(r -> {
            Application application = r.getResponseBody();
            AssertionsForInterfaceTypes.assertThat(application)
                                       .isNotNull()
                                       .isEqualToIgnoringGivenFields(app, "lastModifiedDateTime", "version");
            AssertionsForInterfaceTypes.assertThat(application.getVersion()).isEqualTo(1L);
            AssertionsForInterfaceTypes.assertThat(application.getLastModifiedDateTime())
                                       .isNotNull()
                                       .isNotEqualTo(app.getLastModifiedDateTime());
        });
    }

    @Test
    public void deleteAppTest1() {
        String id = Objects.requireNonNull(appDao.findAll().blockFirst()).getId();
        webTestClient.delete().uri(AppConstants.URI.DELETE_APP, id).exchange().expectStatus().isNoContent();
    }
}