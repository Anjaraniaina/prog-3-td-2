package app.foot.repository;

import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerEntityRepository extends JpaRepository<PlayerEntity, Integer> {
}
