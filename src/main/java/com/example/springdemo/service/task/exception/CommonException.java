package com.example.springdemo.service.task.exception;

public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String REQUIRED_PARAM_SUFFIX = "000";
    public static final String ILLEGAL_PARAM_SUFFIX = "001";
    private Integer code;
    private String errorMessage;

    public CommonException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public CommonException(String errorMessage) {
        super(errorMessage);
        this.code = -1;
        this.errorMessage = errorMessage;
    }

    public CommonException(AbstractServiceException exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static CommonException throwEx(AbstractServiceException e) {
        throw new CommonException(e);
    }
}