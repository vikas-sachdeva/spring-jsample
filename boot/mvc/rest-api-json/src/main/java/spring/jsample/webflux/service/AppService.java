package spring.jsample.webflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.model.Application;

import java.util.List;

@Service
public class AppService {

    @Autowired
    private AppDao dao;

    public List<Application> getApps() {
        return dao.getApps();
    }

    public Application addApp(Application app) {
        return dao.addApp(app);
    }

    public void deleteApp(int id) {
        dao.deleteApp(id);
    }

    public Application updateApp(Application app) {
        return dao.updateApp(app);
    }

    public Application getApp(int id) {
        return dao.getAppById(id);
    }
}