package spring.jsample.webflux.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import spring.jsample.webflux.model.Application;

@Repository
public interface AppDao extends ReactiveMongoRepository<Application, String> {

}
