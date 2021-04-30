package spring.jsample.webflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.model.Application;

@Service
public class AppService {

    @Autowired
    private AppDao dao;

    public Flux<Application> getApps() {
        return dao.getApps();
    }

    public Mono<Application> getApp(int id) {
        return dao.getAppById(id);
    }

    public Mono<Application> addApp(Application app) {
        return dao.addApp(app);
    }

    public Mono<?> deleteApp(int id) {
        return dao.deleteApp(id);
    }

    public Mono<Application> updateApp(Application app) {
        return dao.updateApp(app);
    }
}