package com.yxs.flow.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @program: sprongboot-flow
 * @description: 老板审批
 * @author: yang-xiansen
 * @create: 2019-10-13 21:35
 **/
public class BossTaskHandle implements TaskListener {

    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("经理");
    }

}
