# flowable
Flowable是一个使用Java编写的轻量级业务流程引擎。Flowable流程引擎可用于部署BPMN 2.0流程定义（用于定义流程的行业XML标准）， 创建这些流程定义的流程实例，进行查询，访问运行中或历史的流程实例与相关数据，等等。

> 先启动好此项目，然后创建一个流程：

访问：http://localhost:8080/expense/add?userId=123&money=12345

返回：提交成功.流程Id为：f86edf08-edc0-11e9-93f0-005056c00001

 

> 查询待办列表:

访问：http://localhost:8080/expense/list?userId=123

输出：Task[id=f86edf08-edc0-11e9-93f0-005056c00001, name=出差报销]

 

> 同意：

访问：http://localhost:8080/expense/apply?taskId=f86edf08-edc0-11e9-93f0-005056c00001

返回：processed ok!

 

> 生成流程图：

访问：http://localhost:8080/expense/processDiagram?processId=f86edf08-edc0-11e9-93f0-005056c00001