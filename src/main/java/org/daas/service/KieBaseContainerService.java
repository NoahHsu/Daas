package org.daas.service;

import java.util.Optional;
import java.util.Set;
import org.daas.bean.KieBaseContainerBean;

public interface KieBaseContainerService {
  /**
   * get a Set of all LbpsKieBase's ruleSetId
   * 
   * @return
   * @throws CommonException
   */
  public Set<String> getKieBaseIdSet();

  /**
   * get LbpsKieBase using ruleSetId
   * 
   * @param ruleSetId
   * @return
   * @throws CommonException
   */
  public Optional<KieBaseContainerBean> getKieBase(String ruleSetId);

  /**
   * add LbpsKieBase
   * 
   * @param ruleSetId
   * @param lbpsKieBase
   * @throws CommonException
   */
  void addKieBase(String ruleSetId, KieBaseContainerBean lbpsKieBase) throws Exception;

  /**
   * update LbpsKieBase using ruleSetId
   * 
   * @param ruleSetId
   * @param lbpsKieBase
   * @throws CommonException
   */
  void updateKieBase(String ruleSetId, KieBaseContainerBean lbpsKieBase) throws Exception;

  /**
   * delete all LbpsKieBase
   * 
   * @param ruleSetId
   * @throws CommonException
   */
  void deleteAllKieBase() throws Exception;

  /**
   * delete LbpsKieBase using ruleSetId
   * 
   * @param ruleSetId
   * @throws CommonException
   */
  void deleteKieBase(String ruleSetId) throws Exception;

}
