package org.applebase.jira.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.google.common.collect.Maps;
import com.opensymphony.workflow.loader.AbstractDescriptor;

import java.util.Map;

public class GapBalanceValidatorFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory {
      protected void getVelocityParamsForInput(Map velocityParams) {
      }

      protected void getVelocityParamsForEdit(Map velocityParams, AbstractDescriptor descriptor) {
      }

      protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor) {
      }

      public Map getDescriptorParams(Map validatorParams) {
          return Maps.newHashMap();
      }
}
