package it.unipr.sowide.actodes.examples.bully;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;

public class Dead extends Behavior {

    NodeState state;
    private static final MessagePattern PATTERN = MessagePattern.contentPattern(new IsInstance(String.class));
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";


    int id;

    public Dead(NodeState state) {
        this.state = state;
        this.id = this.state.getID();
    }

    public void cases(final CaseFactory c)
    {
        MessageHandler a = (m) -> {
            System.out.println(ANSI_RED + "[CRASH]     Il nodo coordinatore " + this.id + " è crashato" + ANSI_RESET);
            this.state.setOperation("DEAD");
            send(SPACE, this.state);
            return null;
        };

        c.define(START, a);
    }
}
