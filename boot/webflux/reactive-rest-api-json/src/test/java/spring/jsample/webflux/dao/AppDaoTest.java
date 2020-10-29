package spring.jsample.webflux.dao;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import spring.jsample.webflux.model.Application;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@SpringBootTest
public class AppDaoTest {

    @Autowired
    private AppDao dao;

    @Test
    public void getAppsTest1() {
        StepVerifier.create(dao.getApps()).expectSubscription().recordWith(ArrayList::new).expectNextCount(3).consumeRecordedWith(l -> {
            AssertionsForInterfaceTypes.assertThat(l).isNotEmpty();
            l.forEach(a -> {
                AssertionsForInterfaceTypes.assertThat(a.getStatus()).isNotBlank();
                AssertionsForInterfaceTypes.assertThat(a.getName()).isNotBlank();
                AssertionsForInterfaceTypes.assertThat(a.getId()).isPositive();
            });
        }).verifyComplete();
    }

    @Test
    public void addAppTest1() {
        Application app = new Application(-1, "Application-4", "running");
        StepVerifier.create(dao.addApp(app)).expectSubscription().consumeNextWith(a -> {
            AssertionsForInterfaceTypes.assertThat(a.getName()).isEqualTo(app.getName());
            AssertionsForInterfaceTypes.assertThat(a.getStatus()).isEqualTo(app.getStatus());
            AssertionsForInterfaceTypes.assertThat(a.getId()).isPositive();
        }).verifyComplete();
    }

    @Test
    public void getAppTest1() {
        int id = 2;
        StepVerifier.create(dao.getAppById(id)).expectSubscription().consumeNextWith(a -> {
            AssertionsForInterfaceTypes.assertThat(a).isNotNull();
            AssertionsForInterfaceTypes.assertThat(a.getId()).isEqualTo(id);
            AssertionsForInterfaceTypes.assertThat(a.getName()).isNotBlank();
            AssertionsForInterfaceTypes.assertThat(a.getStatus()).isNotBlank();
        }).verifyComplete();
    }

    @Test
    public void getAppTest2() {
        StepVerifier.create(dao.getAppById(5)).expectSubscription().verifyError(NoSuchElementException.class);
    }

    @Test
    public void deleteAppTest1() {
        StepVerifier.create(dao.deleteApp(2)).expectSubscription()
                    .verifyComplete();
    }

    @Test
    public void deleteAppTest2() {
        StepVerifier.create(dao.deleteApp(4))
                    .verifyError(NoSuchElementException.class);
    }

    @Test
    public void updateAppTest1() {
        Application app = new Application(1, "Application-14", "running");
        StepVerifier.create(dao.updateApp(app))
                    .expectSubscription()
                    .consumeNextWith(a -> AssertionsForInterfaceTypes.assertThat(a).isEqualTo(app)).verifyComplete();
    }

    @Test
    public void updateAppTest2() {
        Application app = new Application(5, "Application-14", "running");
        StepVerifier.create(dao.updateApp(app))
                    .expectSubscription()
                    .verifyError(NoSuchElementException.class);
    }
}