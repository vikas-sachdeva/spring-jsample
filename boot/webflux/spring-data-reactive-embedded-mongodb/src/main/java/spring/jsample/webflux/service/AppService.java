package spring.jsample.webflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.exceptions.ApplicationNotFoundException;
import spring.jsample.webflux.model.Application;

@Service
public class AppService {

    @Autowired
    private AppDao dao;

    public Flux<Application> getApps() {
        return dao.findAll();
    }

    public Mono<Application> addApp(Application app) {
        Mono<Application> savedApp = dao.save(app);
        return savedApp;
    }

    public Mono<Void> deleteApp(String id) {
        return dao.findById(id)
                  .switchIfEmpty(Mono.error(new ApplicationNotFoundException("Application with id " + id + " not found")))
                  .flatMap(x -> dao.deleteById(id));

    }

    public Mono<Application> updateApp(Application app, String id) {
        return dao.findById(id).flatMap(a -> {
            a.setName(app.getName());
            a.setRunning(app.getRunning());
            return dao.save(a);

        }).switchIfEmpty(Mono.error((new ApplicationNotFoundException("Application with id " + id + " not found"))));
    }
}