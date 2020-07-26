package jenkins.java.sample;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Behaviors;

/**
 * Steps to create an Actor
 * 1. Create a class which extends AbstractBehavior class
 * 2. Give Message Type<T> when extending AbstractBehavior. 
 *    The message type can be a marker interface and 
 *    all child messages will implement this interface.
 * 3. Create behavior methods according to messages.
 * 4. Override createRecieve method of AbstractBehavior class to recieve messages.
 * 5. Lastly, create a static method to setup the ActorContext(this is like initializing the actor). 
 * 
 * Important points to keep in mind while creating Actors
 * 1. Messages should be immutable, since they are shared between different threads.
 * 2. Give proper names to messages
 * 3. Good practice to keep initial behaviour of an actor as static factory method
 *  and messages as static to understand what type of messages the actor expects and handles. 
 * 4. Use "akka.actor.typed" library for type-strict coding.
 *
 */
public class Greeter extends AbstractBehavior<Greeter.Greet> {
    // Message
    public static final class Greet{
        public final String who;
        public final ActorRef<Greeted> replyTo;

        public Greet(String who, ActorRef<Greeted> replyTo){
            this.who = who;
            this.replyTo = replyTo;
        }
    }

    // Message
    public static final class Greeted{
        public final String whom;
        public final ActorRef<Greet> from; 

        public Greeted(String whom, ActorRef<Greet> actorRef) {
            this.whom = whom;
            this.from = actorRef;
        }
    }

    // Constructor
    private Greeter(ActorContext<Greet> context) {
        super(context);
    }

    // Behavior - sets up Context
    public static Behavior<Greet> create(){
        return Behaviors.setup(Greeter::new);
    }

    // Method to recieve messages
    @Override
    public Receive<Greet> createReceive() {
        return newReceiveBuilder()
        .onMessage(Greet.class, this::onGreet)
        .build();
    }

    // Behavior
    private Behavior<Greet> onGreet(Greet command) {
        getContext().getLog().info("Hello {}!", command.who);
        // asynchronous operation that doesn't block caller's thread
        command.replyTo.tell(new Greeted(command.who, getContext().getSelf()));
        return this;
    }
}
