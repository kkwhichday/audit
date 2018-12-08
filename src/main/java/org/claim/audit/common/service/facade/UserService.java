package org.claim.audit.common.service.facade;




import java.util.List;

import org.claim.audit.common.model.User;



public interface UserService {

	public User findUserByName(String name);
	
    public Object getAllUser();

    public Object getAllGroup();

    //获取组用户
    public Object getUserGroup(String groupId);
    
    //根据状态条件查找对应的组信息
    public  String findGroupEmployee(String status);
    
    public User findUserByUserNameQDSL(final String userName);

    public  List<String> findUsernameByid(String groupId) ;
	public  List<String> findUsernameByid() ;
}
