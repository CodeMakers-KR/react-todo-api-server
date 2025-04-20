package com.example.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator<T> {

	public enum Type {
		NOT_EMPTY
		, EQUAL
		, EQUAL_IGNORE_CASE
		, SIZE
		, PASSWORD("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./\\\\\\-])[a-zA-Z0-9!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./\\\\\\-]{10,}$")
		, EMAIL("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])")
		, MAX
		, MIN
		, DATE;
		
		String value;
		Type() {
		}
		
		Type(String value) {
			this.value = value;
		}
	}

	private T object;
	private List<ValidationItems> validationItems;
	
	public Validator(T object) {
		this.object = object;
		this.validationItems = new ArrayList<>();
	}
	
	public Validator<T> add(ValidationItems item) {
		this.validationItems.add(item);
		return this;
	}

	public boolean start() {
		for (ValidationItems validationItems : validationItems) {
			validationItems.forEach((fieldName, validationType) -> {
				validationType.compare(object, fieldName);
			});
		}
		
		return !this.hasErrors();
	}
	
	public boolean hasErrors() {
		for (ValidationItems validationItems : validationItems) {
			for (ValidationType validationType : validationItems.validationTypeList) {
				if ( !validationType.result ) {
					return true;
				}
			}
		}
		
		return false;
	}


	public Map<String, List<String>> getErrors() {
		
		if (!hasErrors()) {
			return null;
		}
		
		Map<String, List<String>> result = new HashMap<>();
		for (ValidationItems validationItems : validationItems) {
			result.put(validationItems.fieldName, new ArrayList<>());
			for (ValidationType validationType : validationItems.validationTypeList) {
				if ( !validationType.result ) {
					result.get(validationItems.fieldName).add(validationType.errorMessage);
				}
			}
		}
		
		return result;
	}


}