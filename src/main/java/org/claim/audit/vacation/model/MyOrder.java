package org.claim.audit.vacation.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private Long uId;
    private BigDecimal total;

    //实体映射重复列必须设置：insertable = false,updatable = false
    @ManyToOne
    @JoinColumn(name = "uId", insertable = false, updatable = false)
    @JsonIgnore
    private Customer customer;

    @Override
	public String toString() {
		return "MyOrder [id=" + id + ", code=" + code + ", uId=" + uId
				+ ", total=" + total + ", customer=" + customer + "]";
	}
}
