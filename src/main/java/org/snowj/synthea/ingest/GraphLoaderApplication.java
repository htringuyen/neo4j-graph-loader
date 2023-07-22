package org.snowj.synthea.ingest;

import org.snowj.synthea.ingest.config.GraphLoaderConfig;
import org.snowj.synthea.ingest.config.IngestManagerConfig;
import org.snowj.synthea.ingest.loader.GraphLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.file.Path;

public class GraphLoaderApplication {
    private ApplicationContext appContext;
    private GraphLoader graphLoader;


    public GraphLoaderApplication() {
        var ctx = new AnnotationConfigApplicationContext();
        var profile = ctx.getEnvironment().getProperty("neo4j.data.protocol");
        System.out.println("============== profile: " + profile);
        ctx.getEnvironment().setActiveProfiles("filecsv");
        ctx.register(IngestManagerConfig.class, GraphLoaderConfig.class);
        ctx.refresh();

        this.appContext = ctx;
        this.graphLoader = ctx.getBean(GraphLoader.class);
    }

    public void run() {
        graphLoader.loadNeo4jGraph();
    }

    public static void main(String... args) {
        var app = new GraphLoaderApplication();
        app.run();
    }

    private static void testPath() {
        var path = Path.of("classpath:/ingest.properties");
        System.out.println(path.toUri());
    }


}
