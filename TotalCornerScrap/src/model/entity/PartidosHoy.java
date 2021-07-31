package model.entity;

import java.util.List;

import com.google.gson.annotations.Expose;

public class PartidosHoy {
	@Expose
	int success;
	@Expose
	Pagination pagination;
	@Expose
	List <PartidosResultado> data;
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	public List<PartidosResultado> getData() {
		return data;
	}
	public void setData(List<PartidosResultado> data) {
		this.data = data;
	}
	

}
