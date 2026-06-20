package edu.ucsb.cs.scaffold.model;

import edu.ucsb.cs.scaffold.entity.User;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CurrentUser {
  private User user;
  private Collection<? extends GrantedAuthority> roles;
}
