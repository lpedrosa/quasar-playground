package com.github.lpedrosa;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.behaviors.ServerActor;
import co.paralleluniverse.fibers.SuspendExecution;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KittyServer extends ServerActor<KittyServerCallMessage, Object, KittyServerCastMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(KittyServer.class);

    private final Queue<Cat> cats;

    public KittyServer() {
        this.cats = new LinkedList<>();
    }

    @Override
    public Object handleCall(ActorRef<?> from, Object id, KittyServerCallMessage m) throws SuspendExecution {
        if (m instanceof OrderMessage) {
            OrderMessage msg = (OrderMessage) m;
            if (this.cats.isEmpty())
                return new Cat(msg.name, msg.colour, msg.description);
            else
                return this.cats.remove();
        } else if (m == TerminateMessage) {
            shutdown();
            reply(from, id, null);
            return null;
        } else {
            LOG.warn("Could not handle this: {}", m);
            replyError(from, id, new Exception("Dunno what is this: " + m));
            return null;
        }
    }

    @Override
    public void handleCast(ActorRef<?> from, Object id, KittyServerCastMessage m) throws SuspendExecution {
        if (m instanceof ReturnMessage)
            handleCast(from, id, (ReturnMessage) m);
    }

    public void handleCast(ActorRef<?> from, Object id, ReturnMessage m) throws SuspendExecution {
        this.cats.add(m.cat);
    }

    @Override
    public void handleInfo(Object m) throws SuspendExecution {
        LOG.warn("Received unexpected message: {}", m);
    }

    @Override
    public void terminate(Throwable cause) throws SuspendExecution {
        for(Cat c : this.cats)
            LOG.info("Cat {} was set free!", c.name);
        if (cause != null)
            LOG.info("Shutdown with error {}", cause);
    }

    public static class OrderMessage implements KittyServerCallMessage{
        public final String name;
        public final String colour;
        public final String description;

        public OrderMessage(String name, String colour, String description) {
            this.name = name;
            this.colour = colour;
            this.description = description;
        }
    }

    public static class ReturnMessage implements KittyServerCastMessage{
        public final Cat cat;

        public ReturnMessage(Cat cat) {
            this.cat = cat;
        }
    }

    public static KittyServerCallMessage TerminateMessage = new KittyServerCallMessage() {};
}
