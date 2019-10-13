package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @program: flow
 * @description: 实现JavaDelegate
 * @author: yang-xiansen
 * @create: 2019-10-13 13:31
 **/
public class CallExternalSystemDelegate implements JavaDelegate {

    //审批过后自动调逻辑
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Calling the external system for employee "
            + delegateExecution.getVariable("employee"));
    }
}
