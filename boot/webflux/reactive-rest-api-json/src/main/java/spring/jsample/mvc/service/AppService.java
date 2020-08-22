package spring.jsample.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.model.Application;

@Service
public class AppService {

    @Autowired
    private AppDao dao;

    public Flux<Application> getApps() {
        return dao.getApps();
    }

    public Mono<Application> addApp(Application app) {
        return dao.addApp(app);
    }

    public Mono<Void> deleteApp(int id) {
        return dao.deleteApp(id);
    }

    public Mono<Application> updateApp(Application app) {
        return dao.updateApp(app);
    }
}