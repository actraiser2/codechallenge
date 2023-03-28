package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.context.annotation.Configuration;

import io.dekorate.docker.annotation.DockerBuild;
import io.dekorate.kubernetes.annotation.Container;
import io.dekorate.kubernetes.annotation.EmptyDirVolume;
import io.dekorate.kubernetes.annotation.Env;
import io.dekorate.kubernetes.annotation.ImagePullPolicy;
import io.dekorate.kubernetes.annotation.KubernetesApplication;
import io.dekorate.kubernetes.annotation.Mount;
import io.dekorate.kubernetes.annotation.Port;
import io.dekorate.kubernetes.annotation.ServiceType;

@Configuration
@KubernetesApplication(
	sidecars = @Container(name="curl", image = "ubuntu:latest", command = {"bash", "-c"}, 
		arguments = {"apt-get update; apt-get install -y curl; sleep 1000000"}),
	initContainers = @Container(name = "git", image = "bitnami/git", 
		envVars = @Env(configmap = "codechallenge-config", name = "codechallenge-env"), 
		command = {"bash", "-c"},
		arguments = {"cd /git; git clone https://github.com/actraiser2/codechallenge.git"},
		mounts = @Mount(name = "git-config", path = "/git")),
	imagePullPolicy = ImagePullPolicy.Always, 
	name = "codechallenge", 
	serviceType = ServiceType.ClusterIP,
	envVars = @Env(configmap = "codechallenge-config", name = "codechallenge-env"),
	version="v1",
	//deploymentStrategy = DeploymentStrategy.RollingUpdate,
	emptyDirVolumes = @EmptyDirVolume(volumeName = "git-config"),
	mounts = @Mount(name = "git-config", path = "/git")
	)

@DockerBuild(registry="ghcr.io", group = "actraiser2")
public class KubernetesConfig {

}
