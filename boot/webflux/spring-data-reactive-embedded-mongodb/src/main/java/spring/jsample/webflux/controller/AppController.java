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
import reactor.core.publisher.Mono;
import spring.jsample.webflux.exceptions.ApplicationNotFoundException;
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
    public Mono<ResponseEntity<Application>> getApp(@PathVariable String id) {
        return service.getApp(id)
                      .map(ResponseEntity::ok)
                      .onErrorReturn(ApplicationNotFoundException.class, ResponseEntity.notFound().build());
    }

    @PostMapping(value = {AppConstants.URI.ADD_APP})
    @ResponseBody
    public ResponseEntity<?> addApp(@RequestBody Application app) {
        return new ResponseEntity<>(service.addApp(app), HttpStatus.CREATED);
    }

    @PutMapping(value = {AppConstants.URI.UPDATE_APP})
    @ResponseBody
    public Mono<ResponseEntity<Application>> updateApp(@RequestBody Application app, @PathVariable String id) {
        return service.updateApp(app, id)
                      .map(ResponseEntity::ok)
                      .onErrorReturn(ApplicationNotFoundException.class, ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = {AppConstants.URI.DELETE_APP})
    @ResponseBody
    public Mono<ResponseEntity<Void>> deleteApp(@PathVariable String id) {
        return service.deleteApp(id)
                      .thenReturn(ResponseEntity.noContent().<Void>build())
                      .onErrorReturn(ApplicationNotFoundException.class, ResponseEntity.notFound().build());
    }
}