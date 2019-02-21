package repository;

import model.Application;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends PagingAndSortingRepository<Application, Long> {
}
