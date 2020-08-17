package spring.jsample.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jsample.mvc.dao.AppDao;
import spring.jsample.mvc.exceptions.ApplicationNotFoundException;
import spring.jsample.mvc.exceptions.ConcurrentModificationException;
import spring.jsample.mvc.model.Application;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppService {

    @Autowired
    private AppDao dao;

    public List<Application> getApps() {
        return dao.findAll();
    }

    public Page<Application> getAppsPageWise(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Application> apps = dao.findAll(pageable);
        return apps;
    }

    public Page<Application> getAppsByNamesPageWise(List<String> list, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Application> apps = dao.findByNameIn(list, pageable);
        return apps;
    }

    public Page<Application> getRunningAppsByNamesPageWise(List<String> list, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Application> apps = dao.findByNameInAndRunning(list, true, pageable);
        return apps;
    }

    public Page<Application> getRunningAppsPageWise(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Application application = new Application();
        application.setRunning(true);
        Example<Application> example = Example.of(application);
        Page<Application> apps = dao.findAll(example, pageable);
        return apps;
    }

    public Application addApp(Application app) {
        Application savedApp = dao.save(app);
        return savedApp;
    }

    public void deleteApp(String id) {
        Application app = dao.findById(id)
                             .orElseThrow(() -> new ApplicationNotFoundException("Application with id " + id + " not found"));
        dao.deleteById(app.getId());
    }

    public Application updateApp(Application app, String id) {
        Optional<Application> dbApp = dao.findById(id);
        return dbApp.map((row) -> {
            if (row.getLastModifiedDateTime().compareTo(app.getLastModifiedDateTime()) != 0) {
                throw new ConcurrentModificationException("Application with id " + id + " is already modified.");
            }
            row.setName(app.getName());
            row.setRunning(app.getRunning());
            return dao.save(row);
        }).orElseThrow(() -> new ApplicationNotFoundException("Application with id " + id + " not found"));
    }
}