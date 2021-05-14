package spring.jsample.webflux.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import spring.jsample.webflux.handler.AppHandler;
import spring.jsample.webflux.util.AppConstants;

@Configuration
public class RoutesConfig {

    @Bean
    RouterFunction<ServerResponse> routes(AppHandler appHandler) {
        return RouterFunctions.route(RequestPredicates.GET(AppConstants.URI.GET_APPS), appHandler::getApps)
                              .andRoute(RequestPredicates.GET(AppConstants.URI.GET_APP_BY_ID), appHandler::getApp)
                              .andRoute(RequestPredicates.POST(AppConstants.URI.ADD_APP), appHandler::addApp)
                              .andRoute(RequestPredicates.PUT(AppConstants.URI.UPDATE_APP), appHandler::updateApp)
                              .andRoute(RequestPredicates.DELETE(AppConstants.URI.DELETE_APP), appHandler::deleteApp);
    }
}
