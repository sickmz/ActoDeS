package it.unipr.sowide.actodes.examples.tokenring;

import java.io.Serializable;

public final class NodeState implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int id;
  private int prevID;
  private String operation;

  public NodeState()
  {
    this.id = 0;
  }

  public void setID(int id) { this.id = id; }

  public int getID()
  {
    return this.id;
  }

  public void setPrevID(int id) { this.prevID = id; }

  public int getPrevID()
  {
    return this.prevID;
  }

  public void setOperation(String operation) { this.operation = operation; }

  public String getOperation()
  {
    return this.operation;
  }

}
