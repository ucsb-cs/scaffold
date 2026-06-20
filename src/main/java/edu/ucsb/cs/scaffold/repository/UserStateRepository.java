package edu.ucsb.cs.scaffold.repository;

import edu.ucsb.cs.scaffold.model.UserState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStateRepository extends JpaRepository<UserState, UUID> {

  Optional<UserState> findByUserid(Long userid);
}
