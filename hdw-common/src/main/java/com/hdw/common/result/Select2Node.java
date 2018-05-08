package com.hdw.common.result;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hdw.common.util.JacksonUtils;

/**
 * 
 * @Descriptin Select2 对象
 * @author TuMinglong
 * @Date 2018年5月5日 上午9:35:26
 */
public class Select2Node implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private Long id;
	/**
	 * 内容
	 */
	private String text;

	/**
	 * 是否选中
	 */
	private boolean selected = false;

	@JsonInclude(Include.NON_NULL)
	private List<Select2Node> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public List<Select2Node> getChildren() {
		return children;
	}

	public void setChildren(List<Select2Node> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return JacksonUtils.toJson(this);
	}

}
