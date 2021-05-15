package spring.jsample.webflux.handler;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jsample.webflux.model.Application;
import spring.jsample.webflux.service.AppService;
import spring.jsample.webflux.util.AppConstants;

import java.time.Duration;

@RestController
public class AppEventHandler {

    private AppService appService;

    public AppEventHandler(AppService appService) {
        this.appService = appService;
    }

    public Mono<ServerResponse> subscribeStatusEvent(ServerRequest serverRequest) {
        return ServerResponse.ok()
                             .contentType(MediaType.TEXT_EVENT_STREAM)
                             .body(Flux.interval(Duration.ofSeconds(1)).map(i ->
                                                                                    appService.getAppStatus(Integer.parseInt(
                                                                                            serverRequest.pathVariable(
                                                                                                    AppConstants.REQ_PARAM.ID)))),
                                   Application.class);

    }
}