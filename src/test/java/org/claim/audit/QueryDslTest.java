package org.claim.audit;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;

import org.claim.audit.common.dao.UserCustomQueryRepository;
import org.claim.audit.common.dao.UserGroupQueryRepository;
import org.claim.audit.common.model.QUser;
import org.claim.audit.common.model.QUserGroup;
import org.claim.audit.common.model.User;
import org.claim.audit.common.model.UserGroup;
import org.claim.audit.common.service.facade.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QueryDslTest {

	@Autowired
	UserService userService;
	@Autowired  
    @PersistenceContext  
    private EntityManager entityManager;  
	
	private JPAQueryFactory queryFactory; 
	
	@Autowired
	UserGroupQueryRepository userGroupQueryRepository;
	
	@Autowired
	UserCustomQueryRepository userCustomQueryRepository;
	
	@Before
	public void setUp() {
		queryFactory = new JPAQueryFactory(entityManager);
	}
	
//	@Test
	@Transactional
	public void testSimpleQuery() throws JsonParseException, JsonMappingException, IOException{
		User user = userService.findUserByUserNameQDSL("dira");
		QUserGroup quserGroup = QUserGroup.userGroup;
		UserGroup userGroup =queryFactory.selectFrom(quserGroup)  
        .where(quserGroup.id.eq("dir"))  
        .fetchOne();
		ObjectMapper mapper= new ObjectMapper();
//		String content = null;
//		mapper.readValue(content, User.class);
//		System.out.println(user);
		System.out.println(mapper.writeValueAsString(userGroup));
		
	}
	
//	@Test
	@Transactional
	public void testSimpleQueryFactory() throws JsonProcessingException{
	
	QUser quser = QUser.user;
	User user = queryFactory.selectFrom(quser)  
        .where(quser.userName.eq("dira"))  
        .fetchOne();
	ObjectMapper mapper= new ObjectMapper();
	
	System.out.println(mapper.writeValueAsString(user));
	}
	
	
	@Test
	@Transactional
	public void testOneToMany() throws JsonProcessingException{
		List<UserGroup> userg = userGroupQueryRepository.findByStatus("有效");
		ObjectMapper mapper= new ObjectMapper();
//		String content = null;
//		mapper.readValue(content, User.class);
		System.out.println("userCustomQueryRepository=="+userCustomQueryRepository.findByDTOUseBean());
		System.out.println("2userCustomQueryRepository=="+userCustomQueryRepository.findByDTOUseBean2());
		System.out.println("2userCustomQueryRepository=="+userCustomQueryRepository.findByDtoAndPager(0, 2));
		System.out.println(mapper.writeValueAsString(userg));
//		System.out.println(userg);
	}
	
}
