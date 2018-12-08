package org.claim.audit.common.model;

import java.util.List;

import javax.persistence.Entity;
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
public class UserGroup {

	@Id
    private String id;
    private String name;
    private String status;
    
    @OneToMany
    @JoinColumn(name = "groupId")
    private List<User> userList;
	@Override
	public String toString() {
		return "UserGroup [id=" + id + ", name=" + name + ", status=" + status
				+ ", userList=" + userList + "]";
	}
}
