package com.app.utilities;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

public class UseIdOrGenerate extends IdentityGenerator {

	 @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
        if(id == null) {
        	return Math.abs(UUID.randomUUID().getMostSignificantBits());
        }
        else { 
        	return id; 
        }
    }
}