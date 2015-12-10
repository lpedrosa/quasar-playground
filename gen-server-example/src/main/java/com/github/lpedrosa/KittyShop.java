package com.github.lpedrosa;

import co.paralleluniverse.actors.behaviors.Server;
import co.paralleluniverse.fibers.SuspendExecution;

public class KittyShop {

    public static Server<KittyServerCallMessage, Object, KittyServerCastMessage> spawn() {
        return new KittyServer().spawn();
    }

    public static Cat orderCat(Server<KittyServerCallMessage, Object, KittyServerCastMessage> server,
                               String name,
                               String colour,
                               String description) throws SuspendExecution, InterruptedException {
        return (Cat) server.call(new KittyServer.OrderMessage("Roger", "black", "a cat"));
    }

    public static void returnCat(Server<KittyServerCallMessage, Object, KittyServerCastMessage> server,
                                 Cat cat) throws SuspendExecution {

        server.cast(new KittyServer.ReturnMessage(cat));
    }


    public static void closeShop(Server<KittyServerCallMessage, Object, KittyServerCastMessage> server) throws SuspendExecution, InterruptedException {
        server.call(KittyServer.TerminateMessage);
    }

}
