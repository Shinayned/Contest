package repository;

import model.Participant;
import model.VerificationToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface VerificationTokenRepository extends PagingAndSortingRepository<VerificationToken, Long> {
    @Query("SELECT u FROM VerificationToken u where u.token = :token")
    VerificationToken findByToken(@Param("token") String token);

    @Query("SELECT u FROM VerificationToken u where u.participant = :participant")
    VerificationToken findByParticipant(@Param("participant") Participant participant);
}
