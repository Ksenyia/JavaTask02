package by.tc.task02.main;

import java.util.ArrayList;
import java.util.HashMap;

import by.tc.task02.entity.Entity;

public class PrintEntity {
	public static void print(Entity entity) {
		
		String character = entity.getCharacter();
		ArrayList<Entity>list = entity.getList();
		HashMap<String, String> attributes =  entity.getAttributes();
		for(String value : attributes.values()){
			System.out.print(value+" ");
		}
		System.out.println(character+" ");
		for(Entity nextEntity: list){
			System.out.print(" ");
			print(nextEntity);
		}
	}
}
