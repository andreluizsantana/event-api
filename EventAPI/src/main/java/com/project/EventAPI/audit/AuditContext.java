package com.project.EventAPI.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("auditContext")
public class AuditContext implements AuditorAware<String> {

  private static final String SYSTEM_USER = "system";

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(currentUsername());
  }

  public static String currentUsername() {
    return SYSTEM_USER;
  }
}
