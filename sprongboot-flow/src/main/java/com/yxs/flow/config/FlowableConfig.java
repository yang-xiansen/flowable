package com.yxs.flow.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * @program: sprongboot-flow
 * @description: 传入流程ID生成当前流程的流程图给前端, 如果流程中使用到中文且生成的图片是乱码的，则需要进配置下字体
 * @author: yang-xiansen
 * @create: 2019-10-13 20:48
 **/

@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {


//    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
    }
}
