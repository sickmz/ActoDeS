package it.unipr.sowide.actodes.examples.tokenring;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.examples.tokenring.Dead;
import it.unipr.sowide.actodes.examples.tokenring.NodeState;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;

/**
 *
 * The {@code HungryPhilosopher} class defines a behavior representing a
 * hungry philosopher. This behavior moves to the {@code EatingPhilosopher}
 * behavior when it gets both the two forks.
 *
**/

public class Coordinator extends Behavior {

  private static final long serialVersionUID = 1L;
  private static final MessagePattern STRING = MessagePattern.contentPattern(new IsInstance(String.class));
  int cycle;
  int id;
  private NodeState state;

  public Coordinator(NodeState state) {
    this.state = state;
    this.id = this.state.getID();
    cycle = 100 / 2;
  }

  @Override
  public void cases(final CaseFactory c) {

    MessageHandler a = (m) -> {
      System.out.println("[COORD]     Il nodo " + this.id + " è il leader della rete");
      this.state.setOperation("LEADER");
      send(SPACE, this.state);
      return null;
    };

    MessageHandler b = (m) -> {
      cycle--;
      System.out.println("[RANDOM]    " + cycle);
      if (cycle == 0)
      {
        return new Dead(this.state);
      }
      return null;
    };

    MessageHandler d = (m) -> {
      if(m.getContent().equals("PING")) {
      //Consumo il messaggio
      }
      return null;
    };


    c.define(START, a);
    c.define(STRING, d);
    c.define(CYCLE, b);
  }
}
