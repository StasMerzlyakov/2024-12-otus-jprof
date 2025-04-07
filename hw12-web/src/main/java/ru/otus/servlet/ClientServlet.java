package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.otus.services.ServiceClient;
import ru.otus.services.TemplateProcessor;
import ru.otus.web.ClientModel;

public class ClientServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final transient ServiceClient serviceClient;
    private final transient TemplateProcessor templateProcessor;

    public ClientServlet(ServiceClient serviceClient, TemplateProcessor templateProcessor) {
        this.serviceClient = serviceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, serviceClient.findAll());

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        var name = req.getParameter("name");
        var address = req.getParameter("address");
        var phones = req.getParameter("phones");
        serviceClient.saveClient(new ClientModel(name, address, phones));
        response.sendRedirect("/clients");
    }
}
