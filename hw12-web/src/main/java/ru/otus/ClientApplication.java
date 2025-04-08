package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.server.ClientWebServer;
import ru.otus.server.ClientWebServerImpl;
import ru.otus.services.*;

public class ClientApplication {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    private static final String REALM_PROPERTIES_FILE = "realm.properties";

    public static void main(String[] args) throws Exception {
        var serviceClient = createServiceClient();
        var templateProcessor = creteTemplateProcessor();

        var authService = createAuthService();

        ClientWebServer server =
                new ClientWebServerImpl(WEB_SERVER_PORT, serviceClient, authService, templateProcessor);

        server.start();
        server.join();
    }

    private static TemplateProcessor creteTemplateProcessor() {
        return new TemplateProcessorImpl(TEMPLATES_DIR);
    }

    private static ServiceClient createServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        return new ServiceClientImpl(dbServiceClient);
    }

    private static AuthService createAuthService() throws Exception {
        return new AuthServiceImpl(REALM_PROPERTIES_FILE);
    }
}
