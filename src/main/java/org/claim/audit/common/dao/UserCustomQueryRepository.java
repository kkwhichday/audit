package org.claim.audit.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.claim.audit.common.model.QUser;
import org.claim.audit.common.model.QUserGroup;
import org.claim.audit.common.vo.UserAndGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Component
public class UserCustomQueryRepository {
	@Autowired  
    @PersistenceContext  
    private EntityManager entityManager;  
      
    private JPAQueryFactory queryFactory;  
      
    @PostConstruct  
    public void init() {  
        queryFactory = new JPAQueryFactory(entityManager);  
    }  
    
    /** 
     * Details：多表动态查询 
     */  
    public List<Tuple> findAllUserAndIdCard(){  
        Predicate predicate = (QUserGroup.userGroup.id).eq(QUser.user.userGroup.id);  
        JPAQuery<Tuple> jpaQuery = queryFactory.select(QUserGroup.userGroup.name, QUser.user.userName)  
                .from(QUserGroup.userGroup, QUser.user)  
                .where(predicate); 
       
        return jpaQuery.fetch();  
    }  
      
    /** 
     * Details：将查询结果以DTO的方式输出 
     */  
    public List<UserAndGroup> findByDTO(){    
        List<Tuple> tuples = findAllUserAndIdCard();  
        List<UserAndGroup> dtos = new ArrayList<UserAndGroup>();  
        if(null != tuples && !tuples.isEmpty()){
            for(Tuple tuple:tuples){  
                String userName = tuple.get(QUser.user.userName);  
                String groupName = tuple.get(QUserGroup.userGroup.name);  
 
                UserAndGroup dto = new UserAndGroup();  
                dto.setUserName(userName);
                dto.setName(groupName);
                dtos.add(dto);  
            }  
        }  
        return dtos;  
    }  
    
    public List<UserAndGroup> findByDTOUseBean(){
    	 Predicate predicate = (QUserGroup.userGroup.id).eq(QUser.user.userGroup.id);  
         JPAQuery<UserAndGroup> jpaQuery = queryFactory.select(
        		 Projections.bean(UserAndGroup.class, QUser.user.userName,QUserGroup.userGroup.name))
                 .from(QUserGroup.userGroup, QUser.user)  
                 .where(predicate); 
         return jpaQuery.fetch();
    }
    public List<UserAndGroup> findByDTOUseBean2(){
   	 Predicate predicate = (QUserGroup.userGroup.id).eq(QUser.user.userGroup.id);  
        JPAQuery<UserAndGroup> jpaQuery = queryFactory.select(
       		 Projections.bean(UserAndGroup.class,QUserGroup.userGroup.name, QUser.user.userName))
                .from(QUserGroup.userGroup, QUser.user)  
                .where(predicate); 
        return jpaQuery.fetch();
   }
      
    /** 
     * Details：多表动态查询，并分页 
     */  
    public QueryResults<UserAndGroup> findByDtoAndPager(int offset, int pageSize){  
        Predicate predicate = (QUserGroup.userGroup.id).eq(QUser.user.userGroup.id);  
        return queryFactory.select(Projections.bean(UserAndGroup.class,QUserGroup.userGroup.name, QUser.user.userName))
                .from(QUserGroup.userGroup, QUser.user)
                .where(predicate)  
                .offset(offset)
                .limit(pageSize).fetchResults();
                
    }
    

    
}
