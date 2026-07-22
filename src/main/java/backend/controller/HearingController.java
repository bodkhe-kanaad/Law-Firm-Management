package backend.controller;

import java.util.List;

import backend.repository.HearingRepo;

public class HearingController {

    private final HearingRepo repo = new HearingRepo();

    public List<List<String>> hearingSchedule() {
        return repo.hearingSchedule();
    }
}
