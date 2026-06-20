package edu.ucsb.cs.scaffold.services;

import edu.ucsb.cs.scaffold.entity.User;
import edu.ucsb.cs.scaffold.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class GoogleSignInServiceImpl extends OidcUserService implements GoogleSignInService {

  @Autowired private UserRepository userRepository;

  @Value("${app.admin.emails:phtcon@ucsb.edu}")
  private String adminEmails;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);
    return processSignIn(oidcUser);
  }

  private OidcUser processSignIn(OidcUser oidcUser) {
    Optional<User> existing = userRepository.findByEmail(oidcUser.getEmail());
    Set<GrantedAuthority> authorities = new HashSet<>();

    boolean isAdmin = isAdminEmail(oidcUser.getEmail());
    authorities.add(new SimpleGrantedAuthority(isAdmin ? "ROLE_ADMIN" : "ROLE_USER"));

    if (existing.isPresent()) {
      User user = existing.get();
      boolean changed = false;
      if (!oidcUser.getFullName().equals(user.getFullName())) {
        user.setFullName(oidcUser.getFullName());
        changed = true;
      }
      if (!oidcUser.getGivenName().equals(user.getGivenName())) {
        user.setGivenName(oidcUser.getGivenName());
        changed = true;
      }
      if (!oidcUser.getFamilyName().equals(user.getFamilyName())) {
        user.setFamilyName(oidcUser.getFamilyName());
        changed = true;
      }
      if (!oidcUser.getPicture().equals(user.getPictureUrl())) {
        user.setPictureUrl(oidcUser.getPicture());
        changed = true;
      }
      if (changed) {
        userRepository.save(user);
      }
    } else {
      User newUser =
          User.builder()
              .googleSub(oidcUser.getSubject())
              .email(oidcUser.getEmail())
              .fullName(oidcUser.getFullName())
              .givenName(oidcUser.getGivenName())
              .familyName(oidcUser.getFamilyName())
              .pictureUrl(oidcUser.getPicture())
              .build();
      userRepository.save(newUser);
    }

    authorities.addAll(oidcUser.getAuthorities());
    return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
  }

  private boolean isAdminEmail(String email) {
    if (email == null || adminEmails == null) return false;
    for (String admin : adminEmails.split(",")) {
      if (admin.trim().equalsIgnoreCase(email.trim())) return true;
    }
    return false;
  }
}
