package org.daas.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.iterators.ReverseListIterator;
import org.daas.bean.KieBaseContainerBean;
import org.daas.bean.RequestBodyBean;
import org.daas.bean.config.DaasServiceBean;
import org.daas.service.ApiConfigureService;
import org.daas.service.KieBaseBuildService;
import org.daas.service.KieBaseContainerService;
import org.daas.service.RuleSetFireService;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.type.FactType;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.event.rule.DefaultRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class RuleSetFireServiceImpl implements RuleSetFireService {

  private KieBaseContainerService kieBaseContainerService;
  private KieBaseBuildService kieBaseBuildService;
  private ApiConfigureService apiConfigureService;

  public RuleSetFireServiceImpl(KieBaseContainerService kieBaseContainerService,
      KieBaseBuildService kieBaseBuildService, ApiConfigureService apiConfigureService) {
    super();
    this.kieBaseContainerService = kieBaseContainerService;
    this.kieBaseBuildService = kieBaseBuildService;
    this.apiConfigureService = apiConfigureService;
  }

  @Override
  public Map<String, Object> fireRules(String apiCode, RequestBodyBean requestBodyBean)
      throws Exception {
    DaasServiceBean target =
        apiConfigureService.findEffectiveService(apiCode, requestBodyBean.getInfo());
    String key = kieBaseBuildService.kieBaseKey(apiCode, target.getVersion());
    Optional<KieBaseContainerBean> kieBaseOpt = kieBaseContainerService.getKieBase(key);
    KieBaseContainerBean kieBase = kieBaseOpt.orElseThrow();
    Map<String, String> codePackageMap = genCodePackageMap(kieBase);
    List<Object> factList =
        buildInsertFactList(requestBodyBean.getData(), kieBase.getKiebase(), codePackageMap);
    Map<String, FactType> ansTypeMap = addDefaultFact(factList, codePackageMap, kieBase);
    KieSession session = fireRule(kieBase, factList);
    Map<String, Object> ansData = genAnswerDataMap(ansTypeMap, session);
    session.dispose();
    return ansData;
  }

  private List<Object> buildInsertFactList(Map<String, Object> dataMap, KieBase kieBase,
      Map<String, String> codePackageMap) throws Exception {
    List<Object> factList = new ArrayList<>();

    for (String factCode : dataMap.keySet()) {
      FactType factType = getFactType(factCode, codePackageMap.get(factCode), kieBase);
      addInputFact(factList, factCode, dataMap, factType);
    }

    return factList;

  }

  private Map<String, FactType> addDefaultFact(List<Object> factList,
      Map<String, String> codePackageMap, KieBaseContainerBean kieBase) throws Exception {
    Map<String, FactType> ansTypeMap = new HashMap<>();
    for (Entry<String, Integer> en : kieBase.getAnsFactDefaultNum().entrySet()) {
      try {
        FactType answerInfoClass =
            getFactType(en.getKey(), codePackageMap.get(en.getKey()), kieBase.getKiebase());
        for (int i = 0; i < en.getValue(); i++) {
          factList.add(answerInfoClass.newInstance());
        }
        ansTypeMap.put("AnswerInfo", answerInfoClass);
      } catch (Exception e) {
        throw e;
      }
    }
    for (Entry<String, Integer> en : kieBase.getTempFactDefaultNum().entrySet()) {
      try {
        FactType answerInfoClass =
            getFactType(en.getKey(), codePackageMap.get(en.getKey()), kieBase.getKiebase());
        for (int i = 0; i < en.getValue(); i++) {
          factList.add(answerInfoClass.newInstance());
        }
      } catch (Exception e) {
        throw e;
      }
    }

    return ansTypeMap;
  }

  private FactType getFactType(String factCode, String packageName, KieBase kieBase)
      throws Exception {
    FactType factType = kieBase.getFactType(packageName, factCode);
    if (factType == null) {
      throw new Exception(factCode + " not found");
    }
    return factType;
  }

  @SuppressWarnings("unchecked")
  private List<Object> addInputFact(List<Object> factList, String factCode,
      Map<String, Object> dataMap, FactType factType) throws Exception {

    Object factCodeValueObj = dataMap.get(factCode);
    if (factCodeValueObj instanceof List<?>) {
      for (Map<String, Object> factCodeValueMap : (List<Map<String, Object>>) factCodeValueObj) {
        factList.add(buildFactInstance(factCode, factType, factCodeValueMap));
      }
    } else {
      Map<String, Object> factCodeValueMap = (Map<String, Object>) factCodeValueObj;
      factList.add(buildFactInstance(factCode, factType, factCodeValueMap));
    }

    return factList;
  }

  private Object buildFactInstance(String factCode, FactType factType, Map<String, Object> dataMap)
      throws Exception {
    Object factInstance = factType.newInstance();

    for (String fieldCode : dataMap.keySet()) {
      factType.set(factInstance, fieldCode, dataMap.get(fieldCode));
    }

    return factInstance;
  }

  private Map<String, String> genCodePackageMap(KieBaseContainerBean kieBase) {

    Map<String, String> codePackageMap = new HashMap<>();

    Collection<KiePackage> packageCollection = kieBase.getKiebase().getKiePackages();
    for (KiePackage kiePackage : packageCollection) {
      Collection<FactType> factTypeCollection = kiePackage.getFactTypes();
      Map<String, String> tempMap = factTypeCollection.stream()
          .collect(Collectors.toMap(FactType::getSimpleName, FactType::getPackageName));
      codePackageMap.putAll(tempMap);
    }

    return codePackageMap;
  }

  public KieSession fireRule(KieBaseContainerBean kieBase, List<Object> factList) {
    KieSession session = kieBase.getKiebase().newKieSession();
    // addEventListeners(session);

    for (Object fact : factList) {
      session.insert(fact);
    }


    List<String> groupAgendaList = kieBase.getDefaultRuleGroup();
    if (!CollectionUtils.isEmpty(groupAgendaList)) {
      ReverseListIterator<String> reverseListIterator = new ReverseListIterator<>(groupAgendaList);
      while (reverseListIterator.hasNext()) {
        session.getAgenda().getAgendaGroup(reverseListIterator.next()).setFocus();
      }
    }

    session.addEventListener(agendaListener());
    session.addEventListener(runTimeListener());
    int matchRuleNum = session.fireAllRules();
    System.out.println("number of match rule = " + matchRuleNum);

    return session;
  }

  private DefaultAgendaEventListener agendaListener() {
    return new DefaultAgendaEventListener() {
      public void agendaGroupPushed(AgendaGroupPushedEvent event) {
        super.agendaGroupPushed(event);
        System.out.println("Pushed " + event.getAgendaGroup().getName());// prints the rule name
                                                                         // that fires
      }

      public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
        super.agendaGroupPopped(event);
        System.out.println("Pop" + event.getAgendaGroup().getName());// prints the rule name that
                                                                     // fires
        // the event
      }

      // this event will be executed after the rule matches with the model data
      public void afterMatchFired(AfterMatchFiredEvent event) {
        super.afterMatchFired(event);
        System.out.println("Match" + event.getMatch().getRule().getName());// prints the rule name
                                                                           // that fires
        // the event
      }
    };
  }

  private RuleRuntimeEventListener runTimeListener() {
    // TODO Auto-generated method stub
    return new DefaultRuleRuntimeEventListener() {
      public void objectInserted(ObjectInsertedEvent event) {
        super.objectInserted(event);
        System.out.println("Insert " + event.getObject());
      }

      // this event will be executed after the rule matches with the model data
      public void objectUpdated(ObjectUpdatedEvent event) {
        super.objectUpdated(event);
        System.out.println("[" + event.getRule().getName() + "] = " + event.getOldObject() + " => "
            + event.getObject());
      }
    };
  }

  public Map<String, Object> genAnswerDataMap(Map<String, FactType> ansCodeModelMap,
      KieSession session) throws Exception {
    Map<String, Object> dataMap = new HashMap<>();

    for (String factModelCode : ansCodeModelMap.keySet()) {
      List<Object> ansObjList = session.getObjects().stream().filter(item -> {
        String simpleName = item.getClass().getSimpleName();
        return factModelCode.equals(simpleName);
      }).collect(Collectors.toList());
      switch (ansObjList.size()) {
        case 0:
          throw new Exception("no answer setting");
        case 1:
          dataMap.put(factModelCode, ansObjList.get(0));
          break;
        default:
          dataMap.put(factModelCode, ansObjList);
      }
    }

    return dataMap;
  }

}
