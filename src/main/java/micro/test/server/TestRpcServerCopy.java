package micro.test.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import micro.test.server.service.TestRpcServiceImpl;

/**
 * @author : ddv
 * @since : 2019/12/9 4:07 PM
 */

public class TestRpcServerCopy {
    private int port = 2000;
    private TestRpcServiceImpl service = new TestRpcServiceImpl();
    private Server server;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) throws InterruptedException, IOException {
        TestRpcServerCopy server = new TestRpcServerCopy();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                server.stop();
                System.err.println("*** server shut down");
            }
        });
        server.blockUntilShutdown();

    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(service).build().start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                TestRpcServerCopy.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}