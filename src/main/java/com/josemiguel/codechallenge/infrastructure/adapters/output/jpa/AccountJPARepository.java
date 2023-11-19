package com.josemiguel.codechallenge.infrastructure.adapters.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.josemiguel.codechallenge.application.ports.output.AccountRepository;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;

public interface AccountJPARepository extends AccountRepository, JpaRepository<Account, Long> {

}
