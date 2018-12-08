package org.claim.audit.common.controller;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployController {
	@Resource
	private RepositoryService repositoryService;

	@GetMapping(value = "/deploy")
	public void deploy() {

		Deployment dep = repositoryService.createDeployment()
				.addClasspathResource("vacation.bpmn").deploy();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.deploymentId(dep.getId()).singleResult();
		repositoryService.addCandidateStarterGroup(pd.getId(), "empGroup");

	}

}
