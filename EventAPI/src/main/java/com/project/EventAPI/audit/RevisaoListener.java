package com.project.EventAPI.audit;

import org.hibernate.envers.RevisionListener;

public class RevisaoListener implements RevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {
    RevisaoEvento revisao = (RevisaoEvento) revisionEntity;
    revisao.setUsuario("system");
  }
}
