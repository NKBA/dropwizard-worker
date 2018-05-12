package io.stardog.dropwizard.worker.examples;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.ImmutableList;
import io.stardog.dropwizard.worker.WorkMethods;
import io.stardog.dropwizard.worker.data.WorkMessage;
import io.stardog.dropwizard.worker.data.WorkMethod;
import io.stardog.dropwizard.worker.senders.RedisSender;
import io.stardog.dropwizard.worker.workers.RedisWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class RedisExample {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisExample.class);

    public static void main(String[] arg) throws Exception {
        WorkMethods workMethods = WorkMethods.of(ImmutableList.of(
                WorkMethod.of("ping", params -> LOGGER.info("Received pong"))
        ));

        RedisWorker worker = new RedisWorker(workMethods, new Jedis("localhost", 6379), "example");
        worker.start();

        LOGGER.info("Sending ping");

        RedisSender sender = new RedisSender(new Jedis("localhost", 6379), "example");
        sender.send(WorkMessage.of("ping"));

        LOGGER.info("Sending another ping");
        sender.send(WorkMessage.of("ping"));

        LOGGER.info("Sleeping for 5 seconds...");

        Thread.sleep(5000);

        LOGGER.info("Shutting down");

        worker.stop();
    }
}
