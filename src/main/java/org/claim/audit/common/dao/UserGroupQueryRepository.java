package org.claim.audit.common.dao;

import java.util.List;

import org.claim.audit.common.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserGroupQueryRepository extends JpaRepository<UserGroup, Long>  {


	 
	 List<UserGroup> findByStatus(String status);
	 List<UserGroup> findById(String id);

}
