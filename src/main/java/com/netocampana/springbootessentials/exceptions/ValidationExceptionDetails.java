package com.netocampana.springbootessentials.exceptions;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails{

    private String fields;
    private String fieldsMessage;

}