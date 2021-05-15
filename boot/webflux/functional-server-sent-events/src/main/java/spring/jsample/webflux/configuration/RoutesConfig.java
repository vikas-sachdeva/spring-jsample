package spring.jsample.webflux.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import spring.jsample.webflux.handler.AppEventHandler;
import spring.jsample.webflux.util.AppConstants;

@Configuration
public class RoutesConfig {

    @Bean
    RouterFunction<ServerResponse> routes(AppEventHandler appHandler) {
        return RouterFunctions.route(RequestPredicates.GET(AppConstants.URI.SUBSCRIBE_STATUS_EVENT), appHandler::subscribeStatusEvent);
    }
}
