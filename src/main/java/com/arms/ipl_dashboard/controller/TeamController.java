package com.arms.ipl_dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.arms.ipl_dashboard.model.Team;
import com.arms.ipl_dashboard.repository.TeamRepository;

@RestController
public class TeamController {

    private TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        System.out.println("Controller:" + teamName);
        Team team1 = teamRepository.findByTeamName(teamName);
        System.out.println(team1);
        return team1;        
    }
}
