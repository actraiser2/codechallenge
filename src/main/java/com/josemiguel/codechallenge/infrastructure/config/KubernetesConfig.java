package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.context.annotation.Configuration;

import io.dekorate.kubernetes.annotation.Env;
import io.dekorate.kubernetes.annotation.ImagePullPolicy;
import io.dekorate.kubernetes.annotation.KubernetesApplication;
import io.dekorate.kubernetes.annotation.Port;
import io.dekorate.kubernetes.annotation.ResourceRequirements;
import io.dekorate.kubernetes.annotation.ServiceType;

@Configuration
@KubernetesApplication(
	imagePullPolicy = ImagePullPolicy.Always, 
	name = "codechallenge", 
	serviceType = ServiceType.ClusterIP,
	envVars = @Env(configmap = "codechallenge-config", name = "codechallenge-env"),
	ports = @Port(containerPort = 8080,name = "http", hostPort = 8081 )
	
	)
public class KubernetesConfig {

}