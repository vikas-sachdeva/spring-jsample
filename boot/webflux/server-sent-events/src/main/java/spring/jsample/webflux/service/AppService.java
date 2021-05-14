package spring.jsample.webflux.service;

import org.springframework.stereotype.Service;
import spring.jsample.webflux.model.Application;

import java.util.Random;

@Service
public class AppService {

    private Random random = new Random();

    public Application getAppStatus(int id) {
        return new Application(id, "app-1", getStatus());
    }

    private String getStatus() {
        int randomNumber = random.nextInt();
        if (randomNumber % 2 == 0) {
            return "running";
        } else {
            return "stopped";
        }
    }
}
