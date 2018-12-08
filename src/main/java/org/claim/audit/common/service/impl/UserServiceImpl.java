package org.claim.audit.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.claim.audit.common.dao.UserGroupQueryRepository;
import org.claim.audit.common.dao.UserQueryRepository;
import org.claim.audit.common.service.facade.UserService;
import org.claim.audit.common.model.QUser;
import org.claim.audit.common.model.UserGroup;
import org.claim.audit.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserQueryRepository userRepository;
	@Autowired
	UserGroupQueryRepository userGroupQueryRepository;


	public User findUserByName(String name) {
		List<User> list = userRepository.findByUserName(name);
		User realuser = null;
		if (list != null && list.size() > 0) {
			realuser = list.get(0);
		}
		return realuser;
	}

	public Object getAllUser() {
		List<User> userList = userRepository.findAll();
		return userList;
	}

	public Object getAllGroup() {
		List<Group> groupList = new ArrayList<Group>();
		List<UserGroup> userGroupList = new ArrayList<>();
		for (Group group : groupList) {
			UserGroup userGroup = new UserGroup();
			userGroup.setId(group.getId());
			userGroup.setName(group.getName());
			userGroupList.add(userGroup);
		}
		return userGroupList;
	}

	public Object getUserGroup(String groupId) {
		List<User> userList = userRepository.findByGroupId(groupId);
		return userList;
	}

	//根据传入的工作流流程变量,查出符合条件的组,并返回组名
	public  String findGroupEmployee(String status) {
		List<UserGroup> groupList = userGroupQueryRepository.findByStatus(status);
		List<String> list=new ArrayList<String>();
		for(UserGroup g:groupList){
			list.add(g.getId());
		}
		
//		test.replace('[', '');
		return StringUtils.join(list.toArray(), ",");
	}
	
	public  List<String> findUsernameByid(String groupId) {
		List<User> userList = userRepository.findByGroupId(groupId);
		List<String> list=new ArrayList<String>();
		for(User u:userList){
			list.add(u.getUserName());
		}
		
//		test.replace('[', '');
		return list;
	}
	public  List<String> findUsernameByid() {
		List<User> userList = userRepository.findByGroupId("manageGroup");
		List<String> list=new ArrayList<String>();
		for(User u:userList){
			list.add(u.getUserName());
		}
		
//		test.replace('[', '');
		return list;
	}
	/*
	 * 
	 * private List<UserBean> toMyUser(List<User> userList) { List<User>
	 * myUserList = new ArrayList<>(); for (User user : userList) { User myUser
	 * = new User(); myUser.setId(user.getId());
	 * myUser.setPassword(user.getPassword()); myUserList.add(myUser); } return
	 * myUserList; }
	 */

	public User findUserByUserNameQDSL(final String userName){  
        /** 
         * 该例是使用spring data QueryDSL实现 
         */  
        QUser quser = QUser.user;  
        Predicate predicate = quser.userName.eq(userName);// 根据用户名，查询user表  
        return userRepository.findOne(predicate);  
    } 
	
	
	 /** 
     * Details：使用join查询 
     */  
    public List<User> findUsersByJoin(){  
      return null;
    }  
}
