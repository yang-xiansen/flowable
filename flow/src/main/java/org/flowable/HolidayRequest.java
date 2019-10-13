package org.flowable;




import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @program: flow
 * @description: 创建流程引擎
 * @author: yang-xiansen
 * @create: 2019-10-13 10:12
 **/
public class HolidayRequest {

    public static void main(String[] args) {

        //创建的数据库会随着jvm的重启而消失
//        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
//            .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
//            .setJdbcUsername("sa")
//            .setJdbcPassword("")
//            .setJdbcDriver("org.h2.Driver")
//            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE); //设置了true，确保在JDBC参数连接的数据库中，数据库表结构不存在时，会创建相应的表结构。

        //创建流程引擎
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
            .setJdbcUrl("jdbc:mysql://localhost:3306/flowable")
            .setJdbcUsername("root")
            .setJdbcPassword("root")
            .setJdbcDriver("com.mysql.jdbc.Driver")
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE); //设置了true，确保在JDBC参数连接的数据库中，数据库表结构不存在时，会创建相应的表结构。


        //创建processEngine对象
        ProcessEngine processEngine = cfg.buildProcessEngine();

        //部署流程定义
        //流程定义部署至Flowable引擎，需要使用RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
            .addClasspathResource("holiday-request.bpmn20.xml")
            .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .deploymentId(deployment.getId())
            .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());


        //启动流程
        Scanner scanner= new Scanner(System.in);

        System.out.println("Who are you?");
        String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        System.out.println("Why do you need them?");
        String description = scanner.nextLine();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        ProcessInstance processInstance =
            runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        //获得实际的任务列表
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i=0; i<tasks.size(); i++) {
            System.out.println((i+1) + ") " + tasks.get(i).getName());
        }

        //获得具体的任务
        System.out.println("Which task would you like to complete?");
        int taskIndex = Integer.valueOf(scanner.nextLine());
        Task task = tasks.get(taskIndex - 1);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        System.out.println(processVariables.get("employee") + " wants " +
            processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

        //审批任务 完成任务时传递带有’approved’变量（这个名字很重要，因为之后会在顺序流的条件中使用！）的map来模拟
        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        variables = new HashMap<String, Object>();
        variables.put("approved", approved);
        taskService.complete(task.getId(), variables);

        //实现javaDelegate    服务任务（service task）
        // callExternal...


        // 使用历史数据 自动存储所有流程实例的审计数据或历史数据
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
            historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(activity.getActivityId() + " took "
                + activity.getDurationInMillis() + " milliseconds");
        }

    }


}
