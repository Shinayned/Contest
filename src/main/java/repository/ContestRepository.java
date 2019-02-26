package repository;

import model.Contest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ContestRepository extends PagingAndSortingRepository<Contest, Long>{
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Contest c WHERE c.name = :contestName")
    boolean existsByContestName(@Param("contestName") String contestName);
}
