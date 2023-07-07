package com.josemiguel.codechallenge.infrastructure.naming;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CodechallengeNamingStrategy extends PhysicalNamingStrategyStandardImpl  {

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		// TODO Auto-generated method stub
		return new Identifier("TB_" + name.getText(), name.isQuoted());
	}

	
}
