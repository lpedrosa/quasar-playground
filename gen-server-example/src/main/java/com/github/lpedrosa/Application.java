package com.github.lpedrosa;

import co.paralleluniverse.actors.behaviors.Server;
import co.paralleluniverse.fibers.SuspendExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws SuspendExecution, InterruptedException {
        Server<KittyServerCallMessage, Object, KittyServerCastMessage> server = KittyShop.spawn();

        Cat cat = KittyShop.orderCat(server, "Roger", "black", "a cat");

        LOG.info(cat.toString());

        Cat cat2 = new Cat("Timothy", "brown and white", "another cat");

        KittyShop.returnCat(server, cat2);

        Cat cat3 = KittyShop.orderCat(server, "Roger", "black", "a cat");

        LOG.info(cat3.toString());

        KittyShop.returnCat(server, cat3);

        try {
            server.call(new KittyServerCallMessage() {
            });
        } catch (RuntimeException ex) {
            LOG.error("Something the server doesn't know how to handle!", ex);
        }

        KittyShop.closeShop(server);

        try {
            KittyShop.orderCat(server, "", "", "");
        } catch (RuntimeException ex) {
            if (ex.getCause() == null)
                LOG.error("SERVER NO LONGER ALIVE", ex);
        }
    }
}
