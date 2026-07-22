package backend.controller;

import java.util.List;

import backend.repository.EvidenceRepo;

public class EvidenceController {

    private final EvidenceRepo repo = new EvidenceRepo();

    public List<List<String>> evidencePerCase() {
        return repo.evidencePerCase();
    }
}
