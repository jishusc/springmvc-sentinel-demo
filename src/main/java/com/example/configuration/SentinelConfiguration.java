package com.example.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.log.LogBase;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

@Configuration
public class SentinelConfiguration {
	public static final String APP_NAME = "springmvc-sentinel-demo";

	@Value("${spring.cloud.sentinel.transport.dashboard}")
	private String sentinelDashboardUrl;
	@Value("${spring.cloud.sentinel.log.dir}")
	private String sentinelLogDir;
	@Value("${spring.cloud.sentinel.log.switch-pid}")
	private String logWithPid;
	@Value("${" + APP_NAME + "-flow-rules:[]}")
	private String flowRules;

	@Bean
	public Object getObject() {
		System.setProperty(AppNameUtil.APP_NAME, APP_NAME);
		System.setProperty(LogBase.LOG_DIR, sentinelLogDir);
		System.setProperty(LogBase.LOG_NAME_USE_PID, logWithPid);

		SentinelConfig.setConfig(TransportConfig.CONSOLE_SERVER, sentinelDashboardUrl);
		List<FlowRule> flowRuleList = JSON.parseObject(flowRules, new TypeReference<List<FlowRule>>() {
		});
		FlowRuleManager.loadRules(flowRuleList);
		return new Object();
	}

	@ApolloConfigChangeListener
	private void configChangeListter(ConfigChangeEvent changeEvent) {
		for (String changedKey : changeEvent.changedKeys()) {
			//动态更新sentinel限流规则
			if (changedKey.equals(APP_NAME + "-flow-rules")) {
				ConfigChange configChange = changeEvent.getChange(changedKey);
				String oldValue = configChange.getOldValue();
				String newValue = configChange.getNewValue();
				System.out.println("newValue:" + newValue);
				List<FlowRule> flowRuleList = JSON.parseObject(newValue, new TypeReference<List<FlowRule>>() {
				});
				FlowRuleManager.loadRules(flowRuleList);
			}
			//动态更新sentinel降级规则
			if (changedKey.equals(APP_NAME + "-degrade-rules")) {
				ConfigChange configChange = changeEvent.getChange(changedKey);
				String oldValue = configChange.getOldValue();
				String newValue = configChange.getNewValue();
				System.out.println("newValue:" + newValue);
				List<DegradeRule> ruleList = JSON.parseObject(newValue, new TypeReference<List<DegradeRule>>() {
				});
				DegradeRuleManager.loadRules(ruleList);
			}
		}
	}

}
