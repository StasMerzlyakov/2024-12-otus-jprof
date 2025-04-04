package ru.otus;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.CachedServiceClient;
import ru.otus.cache.CachedServiceManager;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

@SuppressWarnings({"java:S125", "java:S1481", "java:S1215"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws Exception {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        ResultSetMapper<Client> resultSetMapperClient = new ResultSetMapper<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(
                dbExecutor,
                entitySQLMetaDataClient,
                resultSetMapperClient,
                entityClassMetaDataClient); // реализация DataTemplate, универсальная

        // Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        var clientCache = new MyCache<Long, Client>();
        var cachedServiceClient = new CachedServiceClient(dbServiceClient, clientCache);

        cachedServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = cachedServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = cachedServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        // Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl<>(entityClassMetaDataManager);
        ResultSetMapper<Manager> resultSetMapperManager = new ResultSetMapper<>(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<>(
                dbExecutor, entitySQLMetaDataManager, resultSetMapperManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        var managerCache = new MyCache<Long, Manager>();
        var cachedServiceManager = new CachedServiceManager(dbServiceManager, managerCache);

        cachedServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = cachedServiceManager.saveManager(new Manager("ManagerSecond"));

        System.gc();

        // Информация о времени доступа
        long start = System.nanoTime();
        cachedServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("first iteration ns {}", System.nanoTime() - start);
        start = System.nanoTime();
        cachedServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("second iteration ns {}", System.nanoTime() - start);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
