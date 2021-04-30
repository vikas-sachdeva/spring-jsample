package spring.jsample.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import spring.jsample.webflux.model.Application;
import spring.jsample.webflux.service.AppService;

import java.time.Duration;

@RestController
public class AppEventController {

    private AppService appService;

    public AppEventController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping(value = "/app/status/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Application> subscribeNotifications(@PathVariable int id) {
        return Flux.interval(Duration.ofSeconds(1)).map(a -> appService.getAppStatus(id));
    }
}