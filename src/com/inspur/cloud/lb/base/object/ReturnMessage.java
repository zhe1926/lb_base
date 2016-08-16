package com.inspur.cloud.lb.base.object;

public class ReturnMessage
{
  private boolean code;
  private String message;

  public boolean isCode()
  {
    return this.code;
  }

  public void setCode(boolean code) {
    this.code = code;
  }
  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}