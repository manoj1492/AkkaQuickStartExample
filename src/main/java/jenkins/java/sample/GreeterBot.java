package jenkins.java.sample;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class GreeterBot extends AbstractBehavior<Greeter.Greeted> {

    //Behavior - Sets up context
    public static Behavior<Greeter.Greeted> create(int max){
        return Behaviors.setup((context) -> new GreeterBot(context, max));
    }

    //State
    private int max;
    private int greetingsCounter;

    // Constructor
    private GreeterBot(ActorContext<Greeter.Greeted> context, int max) {
        super(context);
        this.max = max;
    }

    @Override
    public Receive<Greeter.Greeted> createReceive() {
        return newReceiveBuilder()
        .onMessage(Greeter.Greeted.class, this::onGreeted)
        .build();
    }


    // Behavior
    // this method increases greetingsCounter which is mutable
    // but it need not be synchronized since an actor processes one message at a time
    private Behavior<Greeter.Greeted> onGreeted(Greeter.Greeted message){
        greetingsCounter++;
        getContext().getLog().info("Greeting {} for {}", greetingsCounter, message.whom);
        if (greetingsCounter == max) {
            return Behaviors.stopped();
        }
        else{
            message.from.tell(new Greeter.Greet(message.whom, getContext().getSelf()));
            return this;
        }
    }

    
}