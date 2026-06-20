package edu.ucsb.cs.scaffold.services;

import edu.ucsb.cs.scaffold.entity.User;
import edu.ucsb.cs.scaffold.model.CurrentUser;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public abstract class CurrentUserService {

  public abstract User getUser();

  public abstract CurrentUser getCurrentUser();

  public abstract Collection<? extends GrantedAuthority> getRoles();

  public final boolean isLoggedIn() {
    return getUser() != null;
  }
}
