package spring.jsample.mvc.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import spring.jsample.mvc.model.Application;

import java.util.List;

@Repository
public interface AppDao extends MongoRepository<Application, String> {

    Page<Application> findByNameIn(List<String> names, Pageable pageable);

    Page<Application> findByNameInAndRunning(List<String> names, boolean running, Pageable pageable);

}
