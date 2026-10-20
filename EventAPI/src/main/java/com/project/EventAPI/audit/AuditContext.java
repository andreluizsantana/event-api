package com.project.EventAPI.audit;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component("auditContext")
public class AuditContext implements AuditorAware<String> {

  private static final String SYSTEM_USER = "system";

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(currentUsername());
  }

  public static String currentUsername() {
    if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
      HttpServletRequest request = attributes.getRequest();
      if (request.getUserPrincipal() != null && !request.getUserPrincipal().getName().isBlank()) {
        return request.getUserPrincipal().getName();
      }
    }
    return SYSTEM_USER;
  }
}
