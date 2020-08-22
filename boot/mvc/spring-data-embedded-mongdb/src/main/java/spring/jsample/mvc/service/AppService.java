package spring.jsample.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.exceptions.ApplicationNotFoundException;
import spring.jsample.mvc.model.Application;

import java.util.List;

@Service
public class AppService {

    @Autowired
    private AppDao dao;

    public List<Application> getApps() {
        return dao.findAll();
    }

    public Page<Application> getAppsPageWise(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return dao.findAll(pageable);
    }

    public Page<Application> getAppsByNamesPageWise(List<String> list, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return dao.findByNameIn(list, pageable);
    }

    public Page<Application> getRunningAppsByNamesPageWise(List<String> list, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return dao.findByNameInAndRunning(list, true, pageable);
    }

    public Page<Application> getRunningAppsPageWise(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Application application = new Application();
        application.setRunning(true);
        Example<Application> example = Example.of(application);
        return dao.findAll(example, pageable);
    }

    public Application addApp(Application app) {
        return dao.save(app);
    }

    public void deleteApp(String id) {
        Application app = dao.findById(id)
                             .orElseThrow(() -> new ApplicationNotFoundException("Application with id " + id + " not found"));
        dao.deleteById(app.getId());
    }

    public Application updateApp(Application app, String id) {
        return dao.findById(id).map(a -> {
            a.setName(app.getName());
            a.setRunning(app.getRunning());
            return dao.save(a);
        }).orElseThrow(() -> new ApplicationNotFoundException("Application with id " + id + " not found"));
    }
}