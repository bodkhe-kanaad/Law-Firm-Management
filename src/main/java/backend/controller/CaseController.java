package backend.controller;

import java.util.List;

import backend.model.CaseFile.caseTypes;
import backend.model.SelectionCondition;
import backend.repository.CaseRepo;

public class CaseController {
    private static CaseRepo repo = new CaseRepo();

    public List<List<String>> selectionQuery(List<SelectionCondition> conditions) {
        return repo.selectionQuery(conditions);
    }

    public void insertQuery(int case_id, java.sql.Date opening_date, java.sql.Date closing_date,
        caseTypes case_type, int is_successful,
        int client_id, int lawyer_id) {
            repo.insertQuery(case_id, opening_date, closing_date,case_type, is_successful,client_id, lawyer_id);
}
}
