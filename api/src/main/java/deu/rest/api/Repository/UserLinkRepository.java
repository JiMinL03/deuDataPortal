package deu.rest.api.Repository;

import deu.rest.api.Entity.UserLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLinkRepository extends JpaRepository<UserLink, Long> {
    boolean existsByServiceName(String serviceName); //단대 이름 중복 방지용
}
