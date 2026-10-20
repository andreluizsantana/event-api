package com.project.EventAPI.audit;

import static org.assertj.core.api.Assertions.assertThat;
import java.security.Principal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class AuditContextTest {

  @AfterEach
  void limparContextoDaRequisicao() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void usaUsuarioDoPrincipalHttpQuandoDisponivel() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    Principal principal = () -> "andre";
    request.setUserPrincipal(principal);
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    assertThat(AuditContext.currentUsername()).isEqualTo("andre");
  }

  @Test
  void usaUsuarioSystemQuandoNaoHaRequisicaoAutenticada() {
    assertThat(AuditContext.currentUsername()).isEqualTo("system");
  }
}
