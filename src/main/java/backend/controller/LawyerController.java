package backend.controller;

import java.util.List;

import backend.repository.LawyerRepo;

public class LawyerController {

    private final LawyerRepo repo = new LawyerRepo();

    public List<List<String>> joinByCaseType(String caseType) {
        return repo.joinByCaseType(caseType);
    }

    public List<List<String>> divisionLawyersAllCasesLogged() {
        return repo.divisionLawyersAllCasesLogged();
    }
}
