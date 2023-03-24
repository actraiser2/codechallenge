package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.context.annotation.Configuration;

import io.dekorate.docker.annotation.DockerBuild;
import io.dekorate.kubernetes.annotation.Env;
import io.dekorate.kubernetes.annotation.ImagePullPolicy;
import io.dekorate.kubernetes.annotation.KubernetesApplication;
import io.dekorate.kubernetes.annotation.Label;
import io.dekorate.kubernetes.annotation.Port;
import io.dekorate.kubernetes.annotation.RollingUpdate;
import io.dekorate.kubernetes.annotation.ServiceType;
import io.dekorate.kubernetes.config.DeploymentStrategy;

@Configuration
@KubernetesApplication(
	imagePullPolicy = ImagePullPolicy.Always, 
	name = "codechallenge", 
	serviceType = ServiceType.ClusterIP,
	envVars = @Env(configmap = "codechallenge-config", name = "codechallenge-env"),
	ports = @Port(containerPort = 8080,name = "http", hostPort = 8081 ),
	version="v1",
	deploymentStrategy = DeploymentStrategy.RollingUpdate
	)

@DockerBuild(registry="ghcr.io", group = "actraiser2")
public class KubernetesConfig {

}
