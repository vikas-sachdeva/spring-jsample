package spring.jsample.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import spring.jsample.webflux.model.Application;
import spring.jsample.webflux.service.AppService;
import spring.jsample.webflux.util.AppConstants;

@RestController
public class AppController {

    @Autowired
    private AppService service;

    @GetMapping(value = {AppConstants.URI.GET_APPS})
    @ResponseBody
    public ResponseEntity<?> getApps() {
        return new ResponseEntity<>(service.getApps(), HttpStatus.OK);
    }

    @GetMapping(value = {AppConstants.URI.GET_APP})
    @ResponseBody
    public ResponseEntity<?> getApp(@PathVariable int id) {
        return new ResponseEntity<>(service.getApp(id), HttpStatus.OK);
    }

    @PostMapping(value = {AppConstants.URI.ADD_APP})
    @ResponseBody
    public ResponseEntity<?> addApp(@RequestBody Application app) {
        return new ResponseEntity<>(service.addApp(app), HttpStatus.OK);
    }

    @PutMapping(value = {AppConstants.URI.UPDATE_APP})
    @ResponseBody
    public ResponseEntity<?> updateApp(@RequestBody Application app, @PathVariable int id) {
        app.setId(id);
        return new ResponseEntity<>(service.updateApp(app), HttpStatus.OK);
    }

    @DeleteMapping(value = {AppConstants.URI.DELETE_APP})
    @ResponseBody
    public ResponseEntity<?> deleteApp(@PathVariable int id) {
        service.deleteApp(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
