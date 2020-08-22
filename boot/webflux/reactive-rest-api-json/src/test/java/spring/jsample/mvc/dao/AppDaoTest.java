package spring.jsample.mvc.dao;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import spring.jsample.mvc.model.Application;

@SpringBootTest
public class AppDaoTest {

    @Autowired
    private AppDao dao;

    @Test
    public void addAppTest1() {
        Application app4 = new Application(-1, "Application-4", "running");
        StepVerifier.create(dao.addApp(app4)).expectSubscription().consumeNextWith(a -> {
            AssertionsForInterfaceTypes.assertThat(a.getName()).isEqualTo(app4.getName());
            AssertionsForInterfaceTypes.assertThat(a.getStatus()).isEqualTo(app4.getStatus());
            AssertionsForInterfaceTypes.assertThat(a.getId()).isPositive();
        }).verifyComplete();
    }

    @Test
    public void deleteAppTest1() {
        StepVerifier.create(dao.deleteApp(2)).expectSubscription()
                    .verifyComplete();
    }

    @Test
    public void updateAppTest1() {
        Application app = new Application(1, "Application-14", "running");
        StepVerifier.create(dao.updateApp(app))
                    .expectSubscription()
                    .consumeNextWith(a -> AssertionsForInterfaceTypes.assertThat(a).isEqualTo(app)).verifyComplete();
    }
}