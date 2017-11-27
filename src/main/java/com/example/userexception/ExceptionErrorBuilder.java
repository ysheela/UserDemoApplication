package com.example.userexception;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ExceptionErrorBuilder {

    public static ExceptionResponse fromBindingErrors(Errors errors) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorMessage("Validation failed. " + errors.getErrorCount() + " error(s)");

        List<String> errorList = new ArrayList<>();
        for (ObjectError objectError : errors.getAllErrors()) {
            errorList.add(objectError.getDefaultMessage());
        }

        response.setErrors(errorList);
        return response;
    }
}