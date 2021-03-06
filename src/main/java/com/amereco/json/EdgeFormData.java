package com.amereco.json;

public class EdgeFormData {

	public int id;
	private String label;
	private String value;
	private int count;
	private int groupId = -1;
	private int groupRepeatableCount = -1;
	private boolean isGroup;

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean group) {
		isGroup = group;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		if(value != null){
			value = value.replace(label+"#", "");
			if(value.contains("null")){
			    return null;
            }
			return value;
		}
		return null;
		
	}



	public void setValue(String value) {
		this.value = value;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupRepeatableCount() {
		return groupRepeatableCount;
	}

	public void setGroupRepeatableCount(int groupRepeatableCount) {
		this.groupRepeatableCount = groupRepeatableCount;
	}

	@Override
	public String toString() {
		return "EdgeFormData [id=" + id + ", label=" + label + ", value=" + value + ", count="
				+ count + ", groupId=" + groupId + ", groupRepeatableCount=" + groupRepeatableCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeFormData other = (EdgeFormData) obj;
		if (label == null) {
			if (other.label != null){}
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}





}