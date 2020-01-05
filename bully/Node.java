package it.unipr.sowide.actodes.examples.bully;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code ThinkingPhilosopher} class defines a behavior representing a
 * thinking philosopher. This behavior moves to the {@code HungryPhilosopher}
 * behavior after a time between a predefined maximum and minimum value.
 *
**/

public class Node extends Behavior
{
  private static final long serialVersionUID = 1L;

  private NodeState state;
  private Reference referenceLeader;
  static final int ACTOR_NUMBER = 4;
  private int id;
  private boolean ping;
  private static final MessagePattern OK = MessagePattern.contentPattern(new IsInstance(NodeState.class));
  private  int length;
  private MessageHandler rp;
  private Integer count;
  private int randomTime;

  public Node(final NodeState s, final int length)
  {
    this.state = s;
    referenceLeader = null;
    ping = true;
    this.length = length;
    randomTime = (int) (Math.random() * (length - length/4)) + length/4;
  }

  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {

      this.id = this.state.getID();
      System.out.println("[START]     Start del nodo: " + this.id);
      return null;
    };

    c.define(START, a);

    a = (m) -> {
      if (this.id == ACTOR_NUMBER - 1) {
        return new Coordinator(this.state);
      }

      if(referenceLeader != null && ping && (randomTime % 2 == 0)) {
        System.out.println("[CYCLE]     Nodo: " + this.id + ". Ping! (non-deterministico)");
        send(referenceLeader, "PING");
      }

      randomTime = randomTime - (int) (Math.random() * (length/4 - length/8)) + length/8;
      return null;
    };

    MessageHandler b = (m) -> {

      NodeState tmpNode = (NodeState) m.getContent();

      if(tmpNode.getOperation().equals("LEADER"))
      {
        referenceLeader = m.getSender();
      }
      else if(tmpNode.getOperation().equals("DEAD"))
      {
        System.out.println("[ELEC]      Generazione nuova elezione: " + getReference());
        ping = false;
        this.state.setOperation("ELECTION");
        send(SPACE, this.state);
      }
      else if(tmpNode.getOperation().equals("ELECTION") && this.id < tmpNode.getID())
      {
        //non sono il leader
        System.out.println(tmpNode.getID());
        System.out.println("Il nodo " + this.id + " NON è il coordinatore");

      }
      else if(tmpNode.getOperation().equals("ELECTION") && this.id > tmpNode.getID())
      {
        //continua elezione
        System.out.println("Il nodo " + this.id + " è il coordinatore");
      }
      return null;
    };

    c.define(OK, b);
    c.define(CYCLE, a);
  }
}
