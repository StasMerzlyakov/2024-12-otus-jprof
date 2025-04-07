package ru.otus.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.web.ClientModel;

public class ServiceClientImpl implements ServiceClient {

    private final DBServiceClient dbServiceClient;

    public ServiceClientImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void saveClient(ClientModel clientModel) {
        var client = toClient(clientModel);
        dbServiceClient.saveClient(client);
    }

    @Override
    public List<ClientModel> findAll() {
        return dbServiceClient.findAll().stream()
                .map(client -> fromClient(client))
                .toList();
    }

    private ClientModel fromClient(Client client) {
        var phones = client.getPhones().stream().map(Phone::getNumber).collect(Collectors.joining(","));
        var address = client.getAddress().getStreet();
        var name = client.getName();
        var id = client.getId();
        return new ClientModel(id, name, address, phones);
    }

    private Client toClient(ClientModel clientModel) {

        var street = clientModel.getAddress();
        var name = clientModel.getName();
        var phones = Arrays.stream(clientModel.getPhones().split(","))
                .map(number -> new Phone(null, number))
                .toList();

        return new Client(null, name, new Address(null, street), phones);
    }
}
