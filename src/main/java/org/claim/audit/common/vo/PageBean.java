package org.claim.audit.common.vo;


import java.util.List;




public class PageBean<T> {
	Integer totalRecords; //总条数
	Integer totalPages; //总页
	List<T> pageData; //当前页数据
	



	public PageBean(List<T> vacList, Integer pageSize) {
		this.totalRecords = vacList.size();
		this.totalPages = totalRecords%pageSize==0?totalRecords/pageSize:totalRecords/pageSize+1;
		this.pageData = vacList;
	}
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	
	
}
