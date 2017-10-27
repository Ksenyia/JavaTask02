package by.tc.task02.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Entity {
	private String name;
	private String character;
	private ArrayList<Entity> list;
	private HashMap<String,String> attributes;
	
	public Entity(){
		name = "";
		character = "";
		list = new ArrayList<Entity>();
		attributes = new HashMap<String, String>();
	}
	
	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}


	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result
				+ ((character == null) ? 0 : character.hashCode());
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public void setList(ArrayList<Entity> list) {
		this.list = list;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (character == null) {
			if (other.character != null)
				return false;
		} else if (!character.equals(other.character))
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public ArrayList<Entity> getList() {
		return list;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNewEntity(Entity entity) {
		this.list.add(entity);
	}
	@Override
	public String toString() {
		return "Entity :name=" + name + ", character=" + character + ", attributes=" + attributes + ", list="
				+ list +  "]";
		//return "Entity :<" + name + ">"+"; id=" + attributes.values()  + "; list="
		//+ list +  " ";	
	}
	public boolean isEmpty(){
		System.out.print(name.isEmpty());
		if (name!=null&&!name.isEmpty()){
			return false;
		}
		if (!list.isEmpty()){
			return false;
		}
		if (!attributes.isEmpty()){
			return false;
		}
		if (!character.isEmpty()){
			return false;
		}
		return true;
	}

}
