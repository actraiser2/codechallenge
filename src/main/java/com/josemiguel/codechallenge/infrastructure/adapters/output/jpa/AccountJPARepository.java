package com.josemiguel.codechallenge.infrastructure.adapters.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.josemiguel.codechallenge.application.ports.output.AccountRepositoryRepository;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;

public interface AccountJPARepository extends AccountRepositoryRepository, JpaRepository<Account, Long> {

}
