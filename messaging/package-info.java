/**
 *
 * Shows how actors can exchange messages and how actors
 * can create other actors in remote actor spaces if
 * its execution involves several actors spaces.
 *
 * Both standalone and distributed executions are started by the {@code main}
 * method contained in the {@code Initiator} class.
 *
 * This class allows to a user to select the type of execution, the number of
 * messages to be sent and the number of nodes of the distributed application.
 *
 * When the execution is distributed on a set of actors spaces, then
 * the first must be a broker actor space, then there can be zero or more
 * node actor space, and finally the last one must be the
 * initiator actor space.
 *
**/
package it.unipr.sowide.actodes.examples.messaging;
