package edu.ucsb.cs.scaffold.controller;

import edu.ucsb.cs.scaffold.model.CurrentUser;
import edu.ucsb.cs.scaffold.services.CurrentUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class ApiController {

  @Autowired private CurrentUserService currentUserService;

  protected CurrentUser getCurrentUser() {
    return currentUserService.getCurrentUser();
  }
}
