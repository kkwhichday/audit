package org.claim.audit.common.dao;

import java.util.List;

import org.claim.audit.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface UserQueryRepository extends JpaRepository<User, Long>,QueryDslPredicateExecutor<User>  {

	 @Query(nativeQuery = true,value = "select * from user b where b.name=?1 and b.groupId<>null") 
	 List<User> findByNameNat(String name);// 此处用了原生sql，因此sql中的表名必须为真实表名而不能是java对象
	 
	 List<User> findByUserName(String name);
	 
	 List<User> findByGroupId(String groupId);
	 


}
