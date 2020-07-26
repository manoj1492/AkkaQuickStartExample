package jenkins.java.sample;

import org.junit.ClassRule;
import org.junit.Test;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;

/**
 * Unit test for simple App.
 */
public class AkkaQuickStartTest 
{
    @ClassRule // Read about it
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Test
    public void testGreeterActorSendingOfGreeting()
    {
        TestProbe<Greeter.Greeted> testProbe = testKit.createTestProbe();
        ActorRef<Greeter.Greet> underTest = testKit.spawn(Greeter.create(), "greeter");
        underTest.tell(new Greeter.Greet("Charles", testProbe.getRef()));
        //testProbe.expectMessage(new Greeter.Greeted("Charles", underTest));
        testProbe.expectMessageClass(Greeter.Greeted.class);
    }
}
