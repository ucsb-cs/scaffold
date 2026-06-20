package edu.ucsb.cs.scaffold.services;

import edu.ucsb.cs.scaffold.entity.User;
import edu.ucsb.cs.scaffold.model.CurrentUser;
import edu.ucsb.cs.scaffold.repository.UserRepository;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrentUserServiceImpl extends CurrentUserService {

  @Autowired private UserRepository userRepository;
  @Autowired private GrantedAuthoritiesService grantedAuthoritiesService;
  @Autowired private RoleHierarchy roleHierarchy;

  @Override
  public CurrentUser getCurrentUser() {
    CurrentUser cu = CurrentUser.builder().user(getUser()).roles(getRoles()).build();
    log.info("getCurrentUser returns {}", cu);
    return cu;
  }

  @Override
  public User getUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication instanceof OAuth2AuthenticationToken) {
      OidcUser oAuthUser = (OidcUser) authentication.getPrincipal();
      return userRepository.findByEmail(oAuthUser.getEmail()).orElse(null);
    }
    return null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getRoles() {
    return roleHierarchy.getReachableGrantedAuthorities(
        grantedAuthoritiesService.getGrantedAuthorities());
  }
}
