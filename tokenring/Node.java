package it.unipr.sowide.actodes.examples.tokenring;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.registry.Reference;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

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
  private Store store;
  private Token token;
  private Token messageToken;
  private Token storeToken;
  Reference referenceLeader;
  static final int ACTOR_NUMBER = 5;
  int idLeader;
  private int id;
  private static final MessagePattern OK = MessagePattern.contentPattern(new IsInstance(NodeState.class));
  private static final MessagePattern HMAP = MessagePattern.contentPattern(new IsInstance(Store.class));
  private static final MessagePattern TOKEN = MessagePattern.contentPattern(new IsInstance(Token.class));
  int postElection;
  private int randomTime;
  private int length;
  private boolean ping;
  private boolean hmap;
  private boolean stop;
  private boolean dead;

  HashMap<Integer, Reference> hashmap = new HashMap<Integer, Reference>();

  public Node(final NodeState s, final int length)
  {
    this.state = s;
    referenceLeader = null;
    randomTime = (int) (Math.random() * (length - length/4)) + length/4;
    this.length = length;
    ping = true;
    store = new Store();
    hmap = false;
    stop = false;
    dead = false;
    token = new Token();
    storeToken = new Token();
    postElection = -1;



  }

  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {

      this.id = this.state.getID();
      System.out.println("[START]     Start del nodo: " + this.id);

      store.setID(this.id);
      store.setReference(getReference());
      send(SPACE, store);
      return null;
    };

    c.define(START, a);

    MessageHandler h = (m) -> {
      Store tmpHash = (Store) m.getContent();
      hashmap.put(tmpHash.getID(), tmpHash.getReference());
      return null;
    };

    MessageHandler g = (m) -> {
      stop = true;
      Token s = (Token) m.getContent();

      messageToken = new Token();
      for(int i = 0; i < s.getTokenID().size(); i++)
      {
        storeToken.setTokenID(s.getTokenID().get(i));
        if(this.id != s.getTokenID().get(i))
        {
          messageToken.setTokenID(s.getTokenID().get(i));
        }
      }

      if(hashmap.get(this.id + 1) != null) {
        send(hashmap.get(this.id + 1), messageToken);
      } else
      {
        send(hashmap.get((this.id + 1) % (this.id + 1)), messageToken);
      }
      if(postElection == -1) {
        System.out.println("Nodo " + this.id + ". Token totali: " + storeToken.getTokenID());
      }
      if(storeToken.getTokenID().size() == SpaceInfo.INFO.getPopulation())
      {
        stop = false;
        postElection = Collections.max(storeToken.getTokenID());

      }

      return null;
    };


    a = (m) -> {

      if(SpaceInfo.INFO.getPopulation() == 1)
      {
        System.out.println("Non sono presenti nodi gestibili. Per vedere la cronologia delle elezioni, scorrere in alto.");

      } else {

        if (!hmap) {
          for (Object objectName : hashmap.keySet()) {
            System.out.println("Key: " + objectName + " with value: " + hashmap.get(objectName) + ". I'm: " + getReference());
          }
        }

        hmap = true;

        if (this.id == ACTOR_NUMBER - 1) {
          return new Coordinator(this.state);
        }

        if (referenceLeader != null && (randomTime % 2 == 0)) {

          if (ping == true) {
            System.out.println("[CYCLE]     Nodo: " + this.id + ". Ping! (non-deterministico)");
            send(referenceLeader, "PING");
          }
        }

        randomTime = randomTime - (int) (Math.random() * (length / 4 - length / 8)) + length / 8;

        if (this.id == postElection) {

          System.out.println("Elezione effettuata: il nuovo coordinatore è il nodo: " + postElection);

          postElection = -1;

          storeToken.getTokenID().removeAll(storeToken.getTokenID());
          messageToken.getTokenID().removeAll(messageToken.getTokenID());
          token.getTokenID().removeAll(token.getTokenID());

          hashmap.clear();

          return new Coordinator(this.state);
        }


        if (dead && !stop) {

          postElection = -1;

          token.getTokenID().removeAll(token.getTokenID());
          storeToken.getTokenID().removeAll(storeToken.getTokenID());


          if (hashmap.get(this.id + 1) != null) {

            token.setTokenID(this.id);
            System.out.println("Node " + this.id + " send to " + (this.id + 1) + " this tokens: " + this.token.getTokenID());
            send(hashmap.get(this.id + 1), this.token);
          } else if (hashmap.get(this.id + 1) == null) {
            if (hashmap.containsKey(this.id + 2)) {
              this.token.setTokenID(this.id);
              //System.out.println("Second IF token: " + this.token.getTokenID());

              send(hashmap.get(this.id + 2), this.token);
            } else if (hashmap.get(this.id + 1) == null) {
              this.token.setTokenID(this.id);
              System.out.println("Node " + this.id + " send to " + (this.id) % this.id + " this tokens: " + this.token.getTokenID());
              send(hashmap.get((this.id) % this.id), this.token);
            }
          }
        }

      }
      return null;
    };

    MessageHandler b = (m) -> {

      NodeState tmpNode = (NodeState) m.getContent();

      if(tmpNode.getOperation().equals("LEADER"))
      {
        referenceLeader = m.getSender();
        idLeader = tmpNode.getID();
        ping = true;
        dead = false;
      }
      else if(tmpNode.getOperation().equals("DEAD")) {
        ping = false;
        dead = true;
        stop = false;

        hashmap.remove(idLeader);

      }
      return null;
    };

    c.define(OK, b);
    c.define(HMAP, h);
    c.define(TOKEN, g);
    c.define(CYCLE, a);
  }
}
