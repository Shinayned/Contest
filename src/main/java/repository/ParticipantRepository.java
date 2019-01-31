package repository;

import model.Participant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends PagingAndSortingRepository<Participant, Long>{
    @Query("SELECT u FROM Participant u where u.email = :email")
    Participant findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Participant u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
