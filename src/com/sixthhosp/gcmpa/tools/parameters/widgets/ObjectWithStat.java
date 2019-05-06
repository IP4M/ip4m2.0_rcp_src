package com.sixthhosp.gcmpa.tools.parameters.widgets;

/**
 * 选项类型GUI组件返回内容状态的封装类
 * 
 * @author zhengzequn
 *
 */
public class ObjectWithStat {

	private Object object;
	private boolean selected;

	public ObjectWithStat() {
		// TODO Auto-generated constructor stub
	}

	public ObjectWithStat(Object object, boolean b) {
		// TODO Auto-generated constructor stub
		this.object = object;
		this.selected = b;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (selected) {
			return object.toString() + " ---- selected";
		} else {
			return object.toString() + " ---- unselected";
		}
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}