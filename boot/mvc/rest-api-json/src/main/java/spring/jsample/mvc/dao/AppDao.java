package spring.jsample.mvc.dao;

import org.springframework.stereotype.Repository;
import spring.jsample.mvc.model.Application;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Repository
public class AppDao {

    private List<Application> applicationList;

    @PostConstruct
    public void init() {
        Application app1 = new Application(1, "Application-1", "running");
        Application app2 = new Application(2, "Application-2", "stopped");
        Application app3 = new Application(3, "Application-3", "running");
        applicationList = new ArrayList<>(Arrays.asList(app1, app2, app3));
    }

    public List<Application> getApps() {
        return applicationList;
    }

    public Application getAppById(int id) {
        return applicationList.stream().filter(a -> a.getId() == id).findFirst().orElseThrow();
    }

    public void deleteApp(int id) {
        applicationList.stream().filter(a -> a.getId() == id).findFirst().map(a -> applicationList.remove(a)).orElseThrow();
    }

    public Application addApp(Application app) {
        int maxId = applicationList.stream().max(Comparator.comparingInt(Application::getId)).map(Application::getId).orElseThrow();
        app.setId(maxId + 1);
        applicationList.add(app);
        return app;
    }

    public Application updateApp(Application app) {
        return applicationList.stream().filter(a -> a.getId() == app.getId()).findFirst().map(a -> {
            a.setName(app.getName());
            a.setStatus(app.getStatus());
            return a;
        }).orElseThrow();
    }
}
