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
        Application app4 = new Application(4, "Application-4", "running");
        int maxId = dao.getApps().stream().max(Comparator.comparingInt(Application::getId)).map(Application::getId).orElseThrow();
        AssertionsForInterfaceTypes.assertThat(dao.addApp(app4)).isEqualTo(maxId + 1);
    }

    @Test
    public void deleteAppTest1() {
        int id = 2;
        dao.deleteApp(id);
        AssertionsForInterfaceTypes.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> dao.getAppById(id));
    }

    @Test
    public void updateAppTest1() {
        int id = 1;
        Application app1 = new Application(id, "Application-14", "running");
        dao.updateApp(app1);
        AssertionsForInterfaceTypes.assertThat(dao.getAppById(id)).isEqualTo(app1);
    }
}