package backend.controller;
// Controller

import java.util.List;

import backend.repository.InvoiceRepo;

public class InvoiceController {
    private InvoiceRepo repo;

    public InvoiceController() {
        this.repo = new InvoiceRepo();
    }

    public List<List<String>> projectionQueryPage(List<String> columns) {
        return repo.projectionQuery(columns);
    }
}
