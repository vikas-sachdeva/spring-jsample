package spring.jsample.webflux.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import spring.jsample.webflux.exceptions.ApplicationNotFoundException;
import spring.jsample.webflux.model.Application;
import spring.jsample.webflux.service.AppService;
import spring.jsample.webflux.util.AppConstants;

import java.net.URI;

@Component
public class AppHandler {

    @Autowired
    private AppService service;

    public Mono<ServerResponse> getApps(ServerRequest serverRequest) {
        return service.getApps()
                      .collectList()
                      .flatMap(apps -> ServerResponse.ok().body(BodyInserters.fromValue(apps)));

    }

    public Mono<ServerResponse> getApp(ServerRequest serverRequest) {
        return service.getApp(serverRequest.pathVariable(AppConstants.REQ_PARAM.ID))
                      .flatMap(app -> ServerResponse.ok().body(BodyInserters.fromValue(app)))
                      .onErrorResume(ApplicationNotFoundException.class, e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addApp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Application.class)
                            .flatMap(service::addApp)
                            .flatMap(app -> ServerResponse.created(URI.create(AppConstants.URI.GET_APP + app.getId()))
                                                          .body(BodyInserters.fromValue(app)));
    }

    public Mono<ServerResponse> updateApp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Application.class).flatMap(app -> service.updateApp(app, serverRequest.pathVariable(
                AppConstants.REQ_PARAM.ID)))
                            .flatMap(app -> ServerResponse.ok().body(BodyInserters.fromValue(app)))
                            .onErrorResume(ApplicationNotFoundException.class, e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteApp(ServerRequest serverRequest) {
        return service.deleteApp(serverRequest.pathVariable(AppConstants.REQ_PARAM.ID))
                      .then(ServerResponse.noContent().build())
                      .onErrorResume(ApplicationNotFoundException.class, e -> ServerResponse.notFound().build());
    }
}