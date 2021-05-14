package spring.jsample.webflux.controller;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import spring.jsample.webflux.model.Application;
import spring.jsample.webflux.util.AppConstants;

@SpringBootTest
@AutoConfigureWebTestClient
public class AppEventControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void subscribeStatusEventTest() {
        int appId = 1;
        Flux<Application> applicationFlux = webTestClient.get()
                                                         .uri(AppConstants.URI.SUBSCRIBE_STATUS_EVENT, appId)
                                                         .exchange()
                                                         .expectStatus()
                                                         .isOk()
                                                         .returnResult(Application.class)
                                                         .getResponseBody();
        StepVerifier.create(applicationFlux).expectSubscription().consumeNextWith(application -> {
            AssertionsForInterfaceTypes.assertThat(application).isNotNull();
            AssertionsForInterfaceTypes.assertThat(application).extracting(Application::getId).isEqualTo(appId);
        }).thenCancel().verify();
    }
}