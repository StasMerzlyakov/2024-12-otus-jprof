package ru.otus.services;

import java.util.List;
import ru.otus.web.ClientModel;

public interface ServiceClient {
    void saveClient(ClientModel clientModel);

    List<ClientModel> findAll();
}
