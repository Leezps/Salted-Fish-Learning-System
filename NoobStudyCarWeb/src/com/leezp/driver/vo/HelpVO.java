package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.HelpEntity;

public class HelpVO {
	private int state;
	private String message;
	private List<HelpEntity> help;
	
	public HelpVO(int state, String message, List<HelpEntity> help) {
		super();
		this.state = state;
		this.message = message;
		this.help = help;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<HelpEntity> getHelp() {
		return help;
	}

	public void setHelp(List<HelpEntity> help) {
		this.help = help;
	}
	
}
