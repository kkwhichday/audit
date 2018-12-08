package org.claim.audit.common.util;

import java.io.IOException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	public static final ObjectMapper mapper = new ObjectMapper(); 
	public static <T> T  JsonToObject(String json,Class<T> type){
//		ObjectMapper mapper = new ObjectMapper();
		T object = null;
		try {
			object = mapper.readValue(json, type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	
	 public static <T> T JsonToObject(String json, Class<?> collectionClass, Class<?>... elementClasses){
			T object = null;
			try {
				object = mapper.readValue(json, getCollectionType(collectionClass,elementClasses));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object;
	 }
	 
	
	public static <T> String  ObjectToJson(T obj){
//		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	   /**   
     * 获取泛型的Collection Type  
     * @param collectionClass 泛型的Collection   
     * @param elementClasses 元素类   
     * @return JavaType Java类型   
     * @since 1.0   
     */   
 public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
     return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
 }
	
}
