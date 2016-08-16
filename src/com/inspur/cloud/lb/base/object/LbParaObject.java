package com.inspur.cloud.lb.base.object;

public class LbParaObject
{
  private String host;
  private String user;
  private String password;
  private String port;

  public String getHost()
  {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getUser() {
    return this.user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPort() {
    return this.port;
  }

  public void setPort(String port) {
    this.port = port;
  }
}