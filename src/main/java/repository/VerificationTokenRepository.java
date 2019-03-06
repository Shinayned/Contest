package repository;

import model.Participant;
import model.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface VerificationTokenRepository extends PagingAndSortingRepository<Token, Long> {
    @Query("SELECT u FROM Token u where u.token = :token")
    Token findByToken(@Param("token") String token);

    @Query("SELECT u FROM Token u where u.participant = :participant")
    Token findByParticipant(@Param("participant") Participant participant);
}
