package spring.jsample.webflux.dao;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jsample.webflux.model.Application;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class AppDao {

    private List<Application> applications;

    @PostConstruct
    public void init() {
        Application app1 = new Application(1, "Application-1", "running");
        Application app2 = new Application(2, "Application-2", "stopped");
        Application app3 = new Application(3, "Application-3", "running");
        applications = new ArrayList<>(Arrays.asList(app1, app2, app3));
    }

    public Flux<Application> getApps() {
        return Flux.fromIterable(applications);
    }

    public Mono<Application> getAppById(int id) {
        return applications.stream().filter(a -> a.getId() == id).findFirst().map(Mono::just).orElse(error(id));
    }

    private Mono<Application> error(int id) {
        return Mono.error(() -> new NoSuchElementException("Element with id " + id + " not found"));
    }

    public Mono<?> deleteApp(int id) {
        return applications.stream().filter(a -> a.getId() == id).findFirst().map(a -> {
            applications.remove(a);
            return Mono.empty();
        }).orElse(Mono.error(() -> new NoSuchElementException("Element with id " + id + " not found")));
    }

    public Mono<Application> addApp(Application app) {
        int maxId = applications.stream().max(Comparator.comparingInt(Application::getId)).map(Application::getId).orElseThrow();
        app.setId(maxId + 1);
        applications.add(app);
        return Mono.just(app);
    }

    public Mono<Application> updateApp(Application app) {
        return applications.stream().filter(a -> a.getId() == app.getId()).findFirst().map(a -> {
            a.setStatus(app.getStatus());
            a.setName(app.getName());
            return Mono.just(a);
        }).orElse(error(app.getId()));
    }
}