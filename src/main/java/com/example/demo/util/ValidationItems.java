package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ValidationItems {
	
	String fieldName;
	List<ValidationType> validationTypeList;
	
	public static ValidationItems of(String fieldName) {
		return new ValidationItems(fieldName);
	}
	
	public static ValidationItems of(String fieldName, ValidationType validationType) {
		return new ValidationItems(fieldName, validationType);
	}
	
	public static ValidationItems of(String fieldName, List<ValidationType> validationTypeList) {
		return new ValidationItems(fieldName, validationTypeList);
	}
	
	public ValidationItems(String fieldName) {
		this.fieldName = fieldName;
		this.validationTypeList = new ArrayList<>();
	}
	
	public ValidationItems(String fieldName, ValidationType validationType) {
		this.fieldName = fieldName;
		this.validationTypeList = new ArrayList<>();
		this.validationTypeList.add(validationType);
	}
	
	public ValidationItems(String fieldName, List<ValidationType> validationTypeList) {
		this.fieldName = fieldName;
		this.validationTypeList = validationTypeList;
	}
	
	public ValidationItems add(ValidationType validationType) {
		this.validationTypeList.add(validationType);
		return this;
	}
	
	public ValidationItems add(List<ValidationType> validationTypeList) {
		this.validationTypeList.addAll(validationTypeList);
		return this;
	}
	
	public void forEach(BiConsumer<String, ValidationType> consumer) {
		for (ValidationType validationType : validationTypeList) {
			consumer.accept(fieldName, validationType);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		ValidationItems other = (ValidationItems) obj;
		return this.fieldName.equals(other.fieldName);
	}
	
}
