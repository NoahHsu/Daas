# DaaS ( Drools as a service)

We try to build a lightweight RESTful API server that can contain configurable logic and does NOT need coding, rebuilding, compilation for the application server. So we create DaaS, which uses Drools as the core engine to dynamic compile the logic (use .drl, Drools Rule Language ). Then the Application server accepts the request, matches with the logic, and returns the answer as the response. All the logic changes can be done with only writing the .drl file and setting the config.

## Feature List

1. External application.yaml for API setting
2. Read External drl
3. Serve group of drl as an API

## Quick reference
1. [GitHub repository](https://github.com/NoahHsu/Daas)
2. [Docker Hub](https://hub.docker.com/repository/docker/noahhsu/daas)

## Best Practice

We hope that this project can be treated as a micro-service server that only contains logic for your business. You should have a device contain all the .drl files and use this server to take some of them( like mount to the container with container runtime), and building a rule-service server.

In addition, if run Daas as a service in the cloud or container orchestration like K8s( Kubernetes). You should add some sidecar to it to be more reliable or traceable (like Nginx for log, Istio for service mesh...etc).

In the rule-service aspect, we should design the rules as a pure function, the output is calculated by the input data only. so that if the input is the same, then the output is also the same. That makes the rule-service easier to understand and debug.

## Prepare

Prepare file:

1. .drl directory : contains all the .drl that might be used.

   We put the .drl file in the folder as the package hierarchy. In .drl, we can define (see the [Drools documents](https://docs.jboss.org/drools/release/7.55.0.Final/drools-docs/html_single/index.html) for more details).

   1. **Fact**: like class in java, For the input/output of API.
   2. **Rules**: a group(step) of Rules, represents the logic.

2. application.yaml : setting for server and define the API how to use the .drl

   - Server properties
     1. `server.port`: Define the port listened to by the server
     2. `drl-repo.file-paths`: Arrays, Define the directory application will find the .drl files
   - Rule-Service preperties
     1. `dass-service.services`: Arrays of Rule-Services definition as below
        1. `code`: Code for the Rule-Service. Effects the url as `{ip}:{port}/rule/fire/{code}`
        2. `version`: Version number for the Rule-Service.
        3. `effetiveDateTime`: Start effective date-time, formating in `YYYY-MM-DD HH:MM:SS`.
           Will be compared to the `info.caseCreatedDate` in request body.
        4. `ineffetiveDateTime`: End effective date-time, formating in `YYYY-MM-DD HH:MM:SS`.
           Will be compared to the `info.caseCreatedDate` in the request body.
        5. `rules`: Arrays for used **Rules** in the version of Rule-Service
           1. `package-name`: Define the path where can find the .drl file in one of `drl-repo.file-paths`
           2. `code`: Define the file name of the .drl file
           3. `version`: Define the folder name that contains the target .drl file
           4. `priority`: Define the Priority of the Rules. active rules orders From 100 to 0, passive( be launched by ( `setFocus` in drools terms) other rules when some condition match)
        6. `inputs`: Arrays for used request-**Fact** in the version of Rule-Service
           1. `package-name`: same as in rules
           2. `code`: same as in rules
           3. `version`: same as in rules
        7. `outputs`: Arrays for used response-**Fact** in the version of Rule-Service
           1. `package-name`: same as in rules
           2. `code`: same as in rules
           3. `version`: same as in rules
           4. `default-num`: number of instances that the fact will be in the response
        8. `temps` : Arrays for used temp- **Fact** in the version of Rule-Service (in neither request nor response
           1. `package-name`: same as in rules
           2. `code`: same as in rules
           3. `version`: same as in rules
           4. `default-num`: same as in rules

   Example yaml:

```yaml
server:
  port: 8080
drl-repo:
  file-paths:
  - /mydir/drl
daas-service:
  services:
  - code: demo
    version: 1.0.0
    rules:
    - package-name: org.daas.demo.rule
      code: price-rule
      version: 1.0
      priority: 100
    - package-name: org.daas.demo.rule
      code: vip-rule
      version: 2.0
      priority: 80
    inputs:
    - package-name: org.daas.demo.fact
      code: Person
      version: 1.0
    outputs:
    - package-name: org.daas.demo.fact
      code: AnswerInfo
      version: 1.0
      default-num: 1
```

In this YAML file, we defined a couple of things of a rule-service API 

1. Build an API with `{ip}:{port}/rule/fire/demo` as end-point.
2. Find file in 
   1. `/mydir/drl/org/daas/demo/rule/price-rule/1.0/price-rule.drl ` as a rules.
   2. `/mydir/drl/org/daas/demo/rule/vip-rule/2.0/vip-rule.drl` as a rules.
   3. `/mydir/drl/org/daas/demo/fact/Person/1.0/Person.drl` as an input fact.
   4. `/mydir/drl/org/daas/demo/fact/AnswerInfo/1.0/AnswerInfo.drl` as an output fact.
3. when api recieve an request, will execute rules in `price-rule.drl` first,  then execute the rules in `vip-rule.drl`.

## Dockerize

In the Dockerfile, we perform a two-stage image building, so that everyone can build it to an image with docker installed only. Besides, it can reduce the image size.

1. Build image

`docker build -t daas:1.0.0-RELEASE .`

2. Run image (demo version), use demo `application.yaml` and .drl files in `src/main/resource` .

`docker run -d -p ${port}:8080 daas:1.0.0-RELEASE` 

3. Run image (client version), use your own `application.yaml` and .drl files.

```bash
docker run -d \
 --mount type=bind,source={local-drls-repo-path},target={drls-repo-path-define-in-yaml},readonly \
 --mount type=bind,source={local-application.yaml-path},target=/application.yaml,readonly \
 -p {port}:{port-set-in-yaml} \
 daas:1.0.0-RELEASE 
```

In this command we need to define several things:

1. `{local-drls-repo-path}` : the local directory(path) that contains all your needed .drl file.
2. `{drls-repo-path-define-in-yaml}`: the directory that setting in application.yaml, that tells Application to find the needed .drl file.
3. `{local-application.yaml-path}`: the local application.yaml directory
4. `{port}`: the listening port of the container
5. `{port-set-in-yaml}`: the listening port of the application inside the container

After running the image as a container, the container read the .drl file as the yaml define. Then build the KieBase, contain it in memory and serve as an API end-point.

## Runtime

API endpoint provided:

1. `/rules/info`: get all rule-service info
   We can check out the rule-service through `{ip}:{port}/rule/info` to see the `{code}-{version}` and the setting about the rule service, such as execute orders, number of answer Fact... and so on.
2. `/rules/{code}/{version}/info`: get the specific rule-service info
   We can check out the rule-service through `{ip}:{port}/rule/info` to see the `{code}-{version}` and the setting about the rule service, such as  execute orders, number of answer Fact... and so on.
3. `/rules/fire/{code}`: execute the rule service
   We can post with body contains `info` and `data`. In the `info` part, we should add the `ruleFireDateTime`. So that the application can find a suitable version of the rule service to execute. (Find the version that `ruleFireDateTime` is after `effetiveDateTime` and before `ineffetiveDateTime` )
4. `/health`: an easy end-point for any health probe to call

API calling sample (for rule fire):

giving ip= localhost, port=8081, and run the demo rule-service inside this repository (`demo` rule-service).

Fetching url `http://localhost:8081/rule/fire/demo`, with request body as below:

```json
{
  "data": {
   "Person":{"age":19, "vipLevel":8}
},
  "info": {
    "ruleFireDateTime": "2021-07-01 20:21:01"
  }
}
```

We can put all the input data in `data` then we will get a response body as below:

```json
{
  "info": {
    "returnCode": null,
    "returnMessage": null,
    "apiLogId": null,
  },
  "data": {
    "AnswerInfo": {
      "isIllegal": false,
      "price": 210
    }
  }
}
```

The server runs the rule (set the value of the response-Fact) and then return it in the `data` of the response body.