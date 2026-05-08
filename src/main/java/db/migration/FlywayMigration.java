package db.migration;

import com.htweb.configs.HibernateConfigs;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FlywayMigration {

    public static void main(String[] args) {
//        String dbActive = args.length > 0 ? args[0] : "postgresql";
//        String profile = args.length > 1 ? args[1] : "dev";
//
//        System.setProperty("spring.database.active", dbActive);
//        System.setProperty("spring.profiles.active", profile);

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateConfigs.class);

        Flyway flyway = context.getBean(Flyway.class);
        flyway.migrate();

        System.out.println("Migration completed!");
        context.close();
    }
}
