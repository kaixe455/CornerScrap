package model.entity;

import com.google.gson.annotations.Expose;

public class PartidosResultado {
	@Expose
	String id;
	@Expose
	String [] p_corner;
	@Expose
	String [] p_odds;
	@Expose
	String [] p_goal;
	@Expose
	String [] p_asian;
	@Expose
	String h;
	@Expose
	String a;
	@Expose
	String start;
	@Expose
	String l;
	

	public String [] getP_corner() {
		return p_corner;
	}

	public void setP_corner(String[] p_corner) {
		this.p_corner = p_corner;
	}

	public String[] getP_odds() {
		return p_odds;
	}

	public void setP_odds(String[] p_odds) {
		this.p_odds = p_odds;
	}

	public String[] getP_goal() {
		return p_goal;
	}

	public void setP_goal(String[] p_goal) {
		this.p_goal = p_goal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String[] getP_asian() {
		return p_asian;
	}

	public void setP_asian(String[] p_asian) {
		this.p_asian = p_asian;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}
	
}
