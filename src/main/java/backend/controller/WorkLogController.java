package backend.controller;

import java.util.List;

import backend.repository.WorkLogRepo;

public class WorkLogController {

    private final WorkLogRepo repo = new WorkLogRepo();

    public List<List<String>> totalHoursPerLawyer() {
        return repo.totalHoursPerLawyer();
    }

    public List<List<String>> lawyersWithMultipleCases() {
        return repo.lawyersWithMultipleCases();
    }

    public List<List<String>> lawyersAboveAverageHours() {
        return repo.lawyersAboveAverageHours();
    }
}
