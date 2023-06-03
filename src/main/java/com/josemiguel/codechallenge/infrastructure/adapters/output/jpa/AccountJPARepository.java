package com.josemiguel.codechallenge.infrastructure.adapters.output.jpa;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.josemiguel.codechallenge.application.ports.output.AccountRepositoryOutputPort;

@RepositoryRestResource(path = "accounts")
public interface AccountJPARepository extends AccountRepositoryOutputPort {

}
