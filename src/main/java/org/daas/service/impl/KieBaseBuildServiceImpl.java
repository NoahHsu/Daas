package org.daas.service.impl;

import static org.daas.utils.FilePathNameUtils.parsePackage;
import static org.daas.utils.FilePathNameUtils.toFielPath;
import static org.daas.utils.FileStringReader.readFileBytes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.daas.bean.KieBaseContainerBean;
import org.daas.bean.config.DaasServiceBean;
import org.daas.bean.config.DefatulFactModelBean;
import org.daas.bean.config.InputFcatModelBean;
import org.daas.bean.config.RuleGroupBean;
import org.daas.bean.config.abs.DaasResourceBean;
import org.daas.config.FileConfig;
import org.daas.service.KieBaseBuildService;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.springframework.stereotype.Service;

@Service
public class KieBaseBuildServiceImpl implements KieBaseBuildService {

  private KieServices kieServices = KieServices.Factory.get();
  private static final String ACCPEPT_EXTENSION = ".drl";
  private static final Predicate<RuleGroupBean> isDefaultRuleGroup = b -> b.getPriority() != null;
  private static final Comparator<RuleGroupBean> priortySort =
      (lb, rb) -> -Integer.compare(lb.getPriority(), rb.getPriority());

  private FileConfig fileConfig;

  public KieBaseBuildServiceImpl(FileConfig fileConfig) {
    this.fileConfig = fileConfig;
  }

  @Override
  public KieBase buildKieBase(DaasServiceBean serviceDef)
      throws FileNotFoundException, IOException {
    List<DaasResourceBean> resources =
        Stream.of(serviceDef.getInputs(), serviceDef.getRules(), serviceDef.getOutputs(),
            serviceDef.getTemps()).flatMap(List::stream).collect(Collectors.toList());

    KieFileSystem kieFileSystem = loadDrl(resources);
    String key = kieBaseKey(serviceDef.getCode(), serviceDef.getVersion());
    KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
    KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel(key).setDefault(true)
        .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
        .setEventProcessingMode(EventProcessingOption.STREAM);
    resources.stream().map(DaasResourceBean::getPackageName).distinct()
        .forEach(kieBaseModel1::addPackage);

    kieBaseModel1.newKieSessionModel("Session1").setDefault(true)
        .setType(KieSessionModel.KieSessionType.STATEFUL)
        .setClockType(ClockTypeOption.get("realtime"));

    System.out.println("XML = " + kieModuleModel.toXML());
    kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
    KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
    kieBuilder.buildAll();
    KieModule kmodule = kieBuilder.getKieModule();
    KieContainer kieContainer = kieServices.newKieContainer(kmodule.getReleaseId());
    return kieContainer.getKieBase();
  }

  @Override
  public KieBaseContainerBean extractRuntimeInfo(DaasServiceBean serviceDef, KieBase kieBase) {
    KieBaseContainerBean bean = new KieBaseContainerBean();

    bean.setKiebase(kieBase);
    bean.setNotNullInputFields(serviceDef.getInputs().stream().collect(
        Collectors.toMap(DaasResourceBean::getCode, InputFcatModelBean::getNotNullFields)));
    bean.setAnsFactDefaultNum(serviceDef.getOutputs().stream()
        .collect(Collectors.toMap(DaasResourceBean::getCode, DefatulFactModelBean::getDefaultNum)));
    bean.setTempFactDefaultNum(serviceDef.getTemps().stream()
        .collect(Collectors.toMap(DaasResourceBean::getCode, DefatulFactModelBean::getDefaultNum)));
    bean.setDefaultRuleGroup(serviceDef.getRules().stream().filter(isDefaultRuleGroup)
        .sorted(priortySort).map(DaasResourceBean::getCode).collect(Collectors.toList()));
    return bean;
  }

  @Override
  public String kieBaseKey(String apiCode, String version) {
    return apiCode + "-" + version;
  }

  private KieFileSystem loadDrl(List<DaasResourceBean> resources)
      throws FileNotFoundException, IOException {
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    for (DaasResourceBean resource : resources) {
      writeToKieFileSystem(kieFileSystem, resource);
    }
    return kieFileSystem;
  }

  private void writeToKieFileSystem(KieFileSystem kieFileSystem, DaasResourceBean drl)
      throws FileNotFoundException, IOException {
    String filePath = toFielPath("src/main/resources", parsePackage(drl.getPackageName()),
        drl.getCode(), drl.getVersion(), drl.getCode() + ACCPEPT_EXTENSION);
    System.out.println(drl.toString());
    kieFileSystem.write(filePath,
        kieServices.getResources().newByteArrayResource(readFileBytes(deviceFilePath(drl))));
  }

  private String deviceFilePath(DaasResourceBean drl) throws FileNotFoundException {
    String filePath = toFielPath(parsePackage(drl.getPackageName()), drl.getCode(),
        drl.getVersion(), drl.getCode() + ACCPEPT_EXTENSION);
    for (String repoPath : fileConfig.getFilePaths()) { // find in define path
      String fullFilePath = toFielPath(repoPath, filePath);
      System.out.println("find file at : " + fullFilePath);
      if (new File(fullFilePath).isFile()) {
        return fullFilePath;
      }
    }
    for (String repoPath : fileConfig.getFilePaths()) { // find in classpath
      String fullFilePath = toFielPath(repoPath, filePath);
      try {
        System.out.println("in classpath");
        Path a = Paths.get(ClassLoader.getSystemResource(fullFilePath).toURI());
        System.out.println("path" + a);
        return a.toString();
      } catch (URISyntaxException e) {
        throw new FileNotFoundException();
      }
    }
    throw new FileNotFoundException();
  }

}
