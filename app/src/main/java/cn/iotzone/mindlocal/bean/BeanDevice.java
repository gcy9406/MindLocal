package cn.iotzone.mindlocal.bean;

import java.io.Serializable;

public class BeanDevice
  implements Serializable
{
  private String head;
  private String ip;
  private String name;
  private boolean state;
  
  public String getHead()
  {
    return this.head;
  }
  
  public String getIp()
  {
    return this.ip;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public boolean isState()
  {
    return this.state;
  }
  
  public void setHead(String paramString)
  {
    this.head = paramString;
  }
  
  public void setIp(String paramString)
  {
    this.ip = paramString;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
  
  public void setState(boolean paramBoolean)
  {
    this.state = paramBoolean;
  }
}
