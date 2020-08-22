package spring.jsample.mvc.dao;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.jsample.mvc.model.Application;

import java.util.Comparator;
import java.util.NoSuchElementException;

@SpringBootTest
public class AppDaoTest {

    @Autowired
    private AppDao dao;

    @Test
    public void addAppTest1() {
        Application app = new Application(1, "Application-4", "running");
        int maxId = dao.getApps().stream().max(Comparator.comparingInt(Application::getId)).map(Application::getId).orElseThrow();
        AssertionsForInterfaceTypes.assertThat(dao.addApp(app).getId()).isEqualTo(maxId + 1);
        AssertionsForInterfaceTypes.assertThat(dao.addApp(app).getStatus()).isEqualTo(app.getStatus());
        AssertionsForInterfaceTypes.assertThat(dao.addApp(app).getName()).isEqualTo(app.getName());
    }

    @Test
    public void deleteAppTest1() {
        int id = 2;
        dao.deleteApp(id);
        AssertionsForInterfaceTypes.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> dao.getAppById(id));
    }

    @Test
    public void updateAppTest1() {
        Application app = new Application(1, "Application-14", "running");
        AssertionsForInterfaceTypes.assertThat(dao.updateApp(app)).isEqualTo(app);
    }

    @Test
    public void updateAppTest2() {
        Application app = new Application(5, "Application-14", "running");
        AssertionsForInterfaceTypes.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> dao.updateApp(app));
    }
}