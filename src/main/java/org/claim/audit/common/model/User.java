package org.claim.audit.common.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String userName;
    private String password;
    private String address;
	private String groupId;
	@ManyToOne
	@JoinColumn(name = "groupId", insertable = false, updatable = false)
	@JsonIgnore
	private UserGroup userGroup;
	
    

    @Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password="
				+ password + ", address=" + address + ", groupId=" + groupId
				+ ", userGroup=" + userGroup + "]";
	}

}
