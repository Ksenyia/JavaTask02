package by.tc.task02.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.tc.task02.dao.EntityDAO;
import by.tc.task02.dao.exeption.DaoExeption;
import by.tc.task02.entity.Entity;

public class EntityDAOImpl implements EntityDAO {
	
	private static final String emptyString = "";
	private static final String openingTagPattern = "<(([a-z-A-Z_:]+)([^><]*))>";
	private static final String closingTagPattern = "</(([a-z-A-Z_:]+)([^><]*))>";
	private static final String characterPattern = ">(.*)<";
	private static final String characterPatternStart = ">(.*)$";
	private static final String characterPatternEnd = "([^<]+)<";
	private static final String separators = "[ \\t\\n\\x0B\\f\\r\\s]+";
	
	private static boolean isLineContainOpeningTag(String line){
		Pattern patternOpeningTag = Pattern.compile(openingTagPattern,Pattern.DOTALL);
		Matcher matcherOpeningTag = patternOpeningTag.matcher(line);
		if(matcherOpeningTag.find()&&!matcherOpeningTag.group(1).isEmpty()){
			return true;
		}
		return false;
		
	}
 	private Entity getNewEntity(BufferedReader bufferedReader, String line){
		String name = null;
		Entity entity = new Entity();
		while(!isLineEmpty(line)){
			Pattern patternOpeningTag = Pattern.compile(openingTagPattern,Pattern.DOTALL);
			Matcher matcherOpeningTag = patternOpeningTag.matcher(line);
			line = goToNextLine(bufferedReader, line);
			int tagName = 2;
			int tagAttributes = 3;
			while(matcherOpeningTag.find()&&!matcherOpeningTag.group(tagName).isEmpty()){
				name = matcherOpeningTag.group(tagName);
				String attributesString = matcherOpeningTag.group(tagAttributes);
				if(!name.isEmpty()){
					line = addDataToEntity(bufferedReader, line, name, entity, attributesString);
				
					if(closeTag(line,entity.getName())){
						return entity;
					}
					line = goToNextLine(bufferedReader, line);
					while(isLineContainOpeningTag(line)){
						line = getNewEntity(bufferedReader, line, entity);
					}
				}
			}
			if(closeTag(line,entity.getName())){
				return entity;
			}
		}
		return entity;
	}
	private String getNewEntity(BufferedReader bufferedReader, String line,
			Entity entity) {
		Entity newEntity = getNewEntity(bufferedReader,line);
		if((newEntity.getName()!=null)){
			entity.setNewEntity(newEntity);
		}
		line = remove(line, newEntity, bufferedReader);
		line = goToNextLine(bufferedReader, line);
		return line;
	}
	private String goToNextLine(BufferedReader bufferedReader, String line) {
		Pattern patternSeparators = Pattern.compile(separators,Pattern.DOTALL);
		Matcher matcherSeparators = patternSeparators.matcher(line);
		while(isLineEmpty(line)||matcherSeparators.matches()){
			line = toNextLine(bufferedReader, line);
			matcherSeparators = patternSeparators.matcher(line);
		}
		return line;
	}
		
	private boolean isLineEmpty(String line){
		Pattern patternSeparators = Pattern.compile(separators,Pattern.DOTALL);
		Matcher matcherSeparators = patternSeparators.matcher(line);
		if(matcherSeparators.matches()||line.isEmpty()){
			
			return true;
		}
		return false;
	}
	
	private String addDataToEntity(BufferedReader bufferedReader, String line,
			String name, Entity entity, String attributesString) {
		entity.setName(name);
		if(!attributesString.isEmpty()){
			line = addAttributes(line, entity, attributesString);
		}
		line = addCharacter(line, entity, bufferedReader);
		return line;
	}

	private String toNextLine(BufferedReader bufferedReader, String line) {
		try { 
			line = bufferedReader.readLine();
		} catch (IOException e){
			 e.printStackTrace();
		}
		return line;

	}
	private boolean closeTag(String line, String Name) {
		Pattern patternCloseTag = Pattern.compile(closingTagPattern);
		Matcher matcherCloseTag = patternCloseTag.matcher(line.replaceAll(separators, emptyString));
		if((matcherCloseTag.find())){
			int closeTagName = 1;
			if(matcherCloseTag.group(closeTagName).equals(Name)){
			return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	private void addOneAttribute(HashMap<String,String> attributesMap,int i, String[]attributes) {
		int indexOfFirstAtribute = 2;
		int j = i-indexOfFirstAtribute;
		String value = attributes[i];
		String attribute= attributes[j];
		attributesMap.put(attribute, value);
	}
	private String addAttributes(String line, Entity entity,String attributesString) {
		String separator = "[=\"]";
		HashMap<String,String> attributesMap = new HashMap<String, String>();
		String[]attributes = attributesString.split(separator);
		int countOfAttributes = attributes.length-1;
		int step = 3;
		int indexOfFirstAtribute = 2;
		for (int i = countOfAttributes; i>=indexOfFirstAtribute; i-=step){

			addOneAttribute(attributesMap,i,attributes);
			entity.setAttributes(attributesMap);
		}
		return line;
	}
	private String addCharacter(String line, Entity entity,BufferedReader bufferedReader) {
		Pattern patternCharacter = Pattern.compile(characterPattern,Pattern.DOTALL);
		Matcher matcherCharacter = patternCharacter.matcher(line);
		if(matcherCharacter.find()){
			line = simpleAddCharacter(line, entity, matcherCharacter);
		}else{
			line = addCharacterFromNewLine(line, entity, bufferedReader);
		}
		return line;
	}
	private String addCharacterFromNewLine(String line, Entity entity, BufferedReader bufferedReader) {
		Pattern patternCharacter;
		Matcher matcherCharacter;
		patternCharacter = Pattern.compile(characterPatternStart,Pattern.DOTALL);
		matcherCharacter = patternCharacter.matcher(line);
		if(matcherCharacter.find()){
			int groupOfCharacter = 1;
			String character = matcherCharacter.group(groupOfCharacter);
			line = addLinesToCharacter(entity, bufferedReader, character, line );
		}
		return line;
	}
	private String addLinesToCharacter(Entity entity, BufferedReader bufferedReader, String character,String line) {
		Pattern patternCharacter;
		Matcher matcherCharacter;
		line = toNextLine(bufferedReader,line);
		patternCharacter = Pattern.compile(characterPatternEnd,Pattern.DOTALL);
		matcherCharacter = patternCharacter.matcher(line);
			while(!matcherCharacter.find()){
				String StartOfTag = "<";
				if(!line.contains(StartOfTag)){
				character = character.concat(line);
				}
				line = toNextLine(bufferedReader,line);
				patternCharacter = Pattern.compile(characterPatternEnd,Pattern.DOTALL);
				matcherCharacter = patternCharacter.matcher(line);
			}
		int groupForCharacter = 1;
		character = character.concat(matcherCharacter.group(groupForCharacter));
		entity.setCharacter(character);
		line = line.replaceFirst(matcherCharacter.group(groupForCharacter),emptyString);
		return line;
	}
	private String simpleAddCharacter(String line, Entity entity, Matcher matcherCharacter) {
		int groupForCharacter = 1;
		String character = matcherCharacter.group(groupForCharacter);
		entity.setCharacter(character);
		line = line.replaceFirst(character,emptyString);
		return line;
	}
	private Entity getEntity(BufferedReader bufferedReader){
		Entity entity = new Entity();
		String line = toNextLine(bufferedReader);
		int tagName = 2;
		Pattern patternOpeningTag = Pattern.compile(openingTagPattern,Pattern.DOTALL);
		Matcher matcherOpeningTag = patternOpeningTag.matcher(line);
		while(!matcherOpeningTag.find()||matcherOpeningTag.group(tagName).isEmpty()){
			line = toNextLine(bufferedReader);
			matcherOpeningTag = patternOpeningTag.matcher(line);
		}
		entity = getNewEntity(bufferedReader, line);
		return entity;
	}
	
	private String toNextLine(BufferedReader bufferedReader) {
		String line = null;
		try {   
			if ((line = bufferedReader.readLine()) != null) {
				Pattern pattern = Pattern.compile(separators,Pattern.DOTALL);
				Matcher matcher = pattern.matcher(line);
				if (line.isEmpty()||matcher.matches()){
					toNextLine(bufferedReader);
				}
			}
		} catch (IOException e){
			 e.printStackTrace();
		}
		return line;
	}

	private String remove(String line,Entity entity,BufferedReader bufferedReader){
		String name = entity.getName();
		String anySymbols = "(.*)";
		String OpeningTag = "<"+ name;
		String ClosingTag = "</"+ name +">";
		String stringToRemove = OpeningTag+anySymbols+ClosingTag;
		Pattern patternToRemove = Pattern.compile(stringToRemove);
		Matcher matcherToRemove = patternToRemove.matcher(line);
		if(matcherToRemove.find()){
			line = line.replace(matcherToRemove.group(),emptyString);
		}
		else{
			line = emptyString;
			line = goToNextLine(bufferedReader,line);
		}
		return line;
	}

	public Entity parse() throws DaoExeption {
		Entity entity = null;
		BufferedReader bufferedReader = null;
		try {   
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("task02.xml").getFile());
			bufferedReader = new BufferedReader(new FileReader(file));
			entity = getEntity(bufferedReader);

		} catch (IOException e){
			 throw new DaoExeption();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {    
					 throw new DaoExeption();
				}   
			}  
		}
		return entity;
	}


}
