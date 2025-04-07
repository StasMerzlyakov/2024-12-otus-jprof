package ru.otus.web;

public class ClientModel {
    private final long id;
    private final String name;
    private final String address;
    private final String phones;

    public ClientModel(long id, String name, String address, String phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public ClientModel(String name, String address, String phones) {
        this(0, name, address, phones);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhones() {
        return phones;
    }
}
