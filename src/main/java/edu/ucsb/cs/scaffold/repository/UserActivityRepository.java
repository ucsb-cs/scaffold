package edu.ucsb.cs.scaffold.repository;

import edu.ucsb.cs.scaffold.model.UserActivity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {}
