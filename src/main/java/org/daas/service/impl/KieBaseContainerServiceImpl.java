package org.daas.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.daas.bean.KieBaseContainerBean;
import org.daas.service.KieBaseContainerService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class KieBaseContainerServiceImpl implements KieBaseContainerService {


  private Map<String, KieBaseContainerBean> kieBaseMap = new HashMap<>();


  public Set<String> getKieBaseIdSet() {
    Assert.notNull(kieBaseMap, "KieBaseContainer");

    Set<String> keySet = kieBaseMap.keySet();

    return keySet;
  }

  @Override
  public Optional<KieBaseContainerBean> getKieBase(String ruleSetId) {
    Assert.notNull(kieBaseMap, "KieBaseContainer");

    Optional<KieBaseContainerBean> result = null;

    if (!kieBaseMap.containsKey(ruleSetId)) {
      result = Optional.empty();
    } else {
      result = Optional.of(kieBaseMap.get(ruleSetId));
    }

    return result;
  }

  @Override
  public void addKieBase(String ruleSetId, KieBaseContainerBean KieBase) throws Exception {
    Assert.notNull(kieBaseMap, "KieBaseContainer");
    Assert.notNull(ruleSetId, "ruleSetId");
    Assert.notNull(KieBase, "KieBase");

    if (kieBaseMap.containsKey(ruleSetId)) {
      throw new Exception(ruleSetId + " already exist ");
    } else {
      kieBaseMap.put(ruleSetId, KieBase);
    }

  }

  @Override
  public void updateKieBase(String ruleSetId, KieBaseContainerBean KieBase) throws Exception {
    Assert.notNull(kieBaseMap, "KieBaseContainer");
    Assert.notNull(ruleSetId, "ruleSetId");
    Assert.notNull(KieBase, "KieBase");

    if (!kieBaseMap.containsKey(ruleSetId)) {
      throw new Exception(ruleSetId + " not exist ");
    } else {
      kieBaseMap.put(ruleSetId, KieBase);
    }

  }

  @Override
  public void deleteAllKieBase() throws Exception {
    Assert.notNull(kieBaseMap, "KieBaseContainer");

    kieBaseMap.clear();
  }

  @Override
  public void deleteKieBase(String ruleSetId) throws Exception {
    Assert.notNull(kieBaseMap, "KieBaseContainer");
    Assert.notNull(ruleSetId, "ruleSetId");

    if (!kieBaseMap.containsKey(ruleSetId)) {
      throw new Exception(ruleSetId + " not exist ");
    } else {
      kieBaseMap.remove(ruleSetId);
    }

  }

}
