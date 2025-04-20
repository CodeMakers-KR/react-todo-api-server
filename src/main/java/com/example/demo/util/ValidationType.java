package com.example.demo.util;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.util.Validator.Type;

public class ValidationType {
	Type validationType;
	String errorMessage;
	Object compareValue;
	boolean result;
	
	public static ValidationType of(Type validationType, String errorMesssage) {
		return new ValidationType(validationType, errorMesssage);
	}
	
	public static ValidationType of(Type validationType, String errorMesssage, Object compareValue) {
		return new ValidationType(validationType, errorMesssage, compareValue);
	}
	
	public ValidationType(Type validationType, String errorMesssage) {
		this.validationType = validationType;
		this.errorMessage = errorMesssage;
		this.result = true;
	}
	
	public ValidationType(Type validationType, String errorMesssage, Object compareValue) {
		this.validationType = validationType;
		this.errorMessage = errorMesssage;
		this.compareValue = compareValue;
		this.result = true;
	}
	
	public void compare(Object object, String fieldName) {
		Class<?> fieldType = getFieldType(object, fieldName);
		if (validationType == Type.NOT_EMPTY) {
			if (fieldType == String.class) {
				String value = getStringFieldValue(object, fieldName);
				result = !StringUtil.isEmpty(value);
			}
		}
		else if (validationType == Type.EQUAL) {
			if (fieldType == String.class) {
				result = getStringFieldValue(object, fieldName).equals(compareValue.toString());
			}
			else if (fieldType == Integer.class || fieldType == int.class) {
				result = getIntFieldValue(object, fieldName) == StringUtil.parseInt(compareValue);
			}
		}
		else if (validationType == Type.EQUAL_IGNORE_CASE) {
			if (fieldType == String.class) {
				result = getStringFieldValue(object, fieldName).equalsIgnoreCase(compareValue.toString());
			}
		}
		else if (validationType == Type.EMAIL || validationType == Type.PASSWORD) {
			if (fieldType == String.class) {
				String pattern = validationType.value;
				String value = getStringFieldValue(object, fieldName);
				if (value != null) {
					result = value.matches(pattern);
				}
			}
		}
		else if (validationType == Type.SIZE) {
			if (fieldType == List.class || fieldType == ArrayList.class) {
				List<?> value = getListFieldValue(object, fieldName);
				if (value != null) {
					result = value.size() < StringUtil.parseInt(compareValue);
				}
			}
		}
		else if (validationType == Type.MIN) {
			if (fieldType == Integer.class || fieldType == int.class) {
				result = getIntFieldValue(object, fieldName) >= StringUtil.parseInt(compareValue);
			}
		}
		else if (validationType == Type.MAX) {
			if (fieldType == Integer.class || fieldType == int.class) {
				result = getIntFieldValue(object, fieldName) <= StringUtil.parseInt(compareValue);
			}
		}
		else if (validationType == Type.DATE) {
			String value = getStringFieldValue(object, fieldName);
			if (!StringUtil.isEmpty(value)) {
				LocalDate valueLocalDate = LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
				LocalDate refValueLocalDate = LocalDate.parse(compareValue.toString(), DateTimeFormatter.ISO_DATE);
				result = !valueLocalDate.isEqual(refValueLocalDate) || !valueLocalDate.isAfter(refValueLocalDate);
			}
		}
	}
	
	private List<?> getListFieldValue(Object object, String fieldName) {
		if (getFieldValue(object, fieldName) instanceof List) {
			return (List<?>) getFieldValue(object, fieldName);
		}
		
		return null;
	}
	
	private int getIntFieldValue(Object object, String fieldName) {
		String value = getStringFieldValue(object, fieldName);
		if (value == null) {
			return 0;
		}
		return Integer.parseInt(value);
	}
	
	private String getStringFieldValue(Object object, String fieldName) {
		Object value = getFieldValue(object, fieldName);
		if (value == null) {
			return null;
		}
		return value.toString();
	}
	
	private Object getFieldValue(Object object, String fieldName) {
		Class<?> tempObject = object.getClass();
		
		while(tempObject != Object.class) {
			Field field = null;
			try {
				field = tempObject.getDeclaredField(fieldName);
				if (field != null) {
					field.setAccessible(true);
					return field.get(object);
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			tempObject = tempObject.getSuperclass();
		}
		
		return null;
	}
	
	private Class<?> getFieldType(Object object, String fieldName) {
		Class<?> tempObject = object.getClass();
		
		while (tempObject != Object.class) {
			Field field = null;
			try {
				field = tempObject.getDeclaredField(fieldName);
				if (field != null) {
					field.setAccessible(true);
					return field.getType();
				}
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			
			tempObject = tempObject.getSuperclass();
		}
		
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		ValidationType other = (ValidationType) obj;
		return this.validationType == other.validationType;
	}
}
