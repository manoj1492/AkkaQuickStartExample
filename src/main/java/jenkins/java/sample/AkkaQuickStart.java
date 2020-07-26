package jenkins.java.sample;

import java.io.IOException;

import akka.actor.typed.ActorSystem;

public class AkkaQuickStart {

    public static void main(String args[]) {
        // Here GreeterMain is the guardian Actor that bootstraps the application 
        final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "helloakka");

        greeterMain.tell(new GreeterMain.SayHello("Charles"));

        try {
        System.out.println(">>> Press ENTER to exit <<<");
        System.in.read();
        } 
        catch (IOException ignored) {
        } 
        finally {
        greeterMain.terminate();
        }
    }
}