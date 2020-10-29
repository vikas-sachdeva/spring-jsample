package spring.jsample.webflux;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import spring.jsample.webflux.dao.AppDao;
import spring.jsample.webflux.model.Application;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
public class ContractBaseClass {

    @Autowired
    private AppDao appDao;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void init() {
        RestAssuredWebTestClient.webTestClient(webTestClient);
        appDao.deleteAll().block();
        List<Application> apps = new ArrayList<>();
        apps.add(new Application("1", "Application-1", true));
        apps.add(new Application("2", "Application-2", false));
        apps.add(new Application("3", "Application-3", true));
        appDao.insert(apps).blockLast();
    }
}