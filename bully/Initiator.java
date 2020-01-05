
package it.unipr.sowide.actodes.examples.bully;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.passive.CycleListActor.TimeoutMeasure;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.Length;
import it.unipr.sowide.actodes.executor.passive.CycleScheduler;

/**
 *
 * The {@code Initiator} class builds the initial set of actors and starts
 * a simulation of the classical dining philosophers concurrency problem.
 *
**/

public final class Initiator extends Builder
{
  static final int ACTOR_NUMBER = 4;
  final int length = 100;

  @Override
  public void build(final Executor<? extends Actor> e)
  {
    for (int i = 0; i < ACTOR_NUMBER; i++)
    {
      NodeState state = new NodeState();

      state.setID(i);
      e.actor(new Node(state, length));
    }
  }

  public static void main(final String[] v)
  {

    final int length = 100;

    Configuration c =  SpaceInfo.INFO.getConfiguration();
    c.setExecutor(new CycleScheduler( new Initiator(), new Length(length), TimeoutMeasure.CY));
    c.start();
  }
}
