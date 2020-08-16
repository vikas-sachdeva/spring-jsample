package spring.jsample.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.model.Application;

import java.util.List;

@Service
public class AppService {

    @Autowired
    private AppDao dao;

    public List<Application> getApps() {
        return dao.getApps();
    }

    public Application addApp(Application app) {
        int id = dao.addApp(app);
        return dao.getAppById(id);
    }

    public void deleteApp(int id) {
        dao.deleteApp(id);
    }

    public Application updateApp(Application app) {
        dao.updateApp(app);
        return dao.getAppById(app.getId());
    }
}