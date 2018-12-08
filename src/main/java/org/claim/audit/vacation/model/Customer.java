package org.claim.audit.vacation.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String userName;
    private String password;
    private String address;
	private String groupId;
    
    //一对多，一个客户对应多个订单，关联的字段是订单里的cId字段
    @OneToMany
    @JoinColumn(name = "uId")
    private List<MyOrder> myOrders;
    
    @Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password="
				+ password + ", address=" + address + ", groupId=" + groupId
				+ ", myOrders=" + myOrders + "]";
	}

}
