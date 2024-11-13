package com.arms.ipl_dashboard.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.arms.ipl_dashboard.model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @SuppressWarnings("null")
    @Override
    public Match process(final MatchInput matchInput) throws Exception{

        Match match = new Match();
        
        match.setId(Long.parseLong(matchInput.getId()));
        match.setCity(matchInput.getCity());
        match.setDate(LocalDate.parse(matchInput.getDate(),DateTimeFormatter.ISO_LOCAL_DATE)); //check
        match.setSeason(matchInput.getSeason());
        match.setMatchNumber(matchInput.getMatchnumber());
        match.setVenue(matchInput.getVenue());
        match.setTossWinner(matchInput.getTosswinner());
        match.setTossDecision(matchInput.getTossdecision());
        match.setWinningteam(matchInput.getWinningteam());
        match.setResult(matchInput.getWonby());
        match.setResultMargin(matchInput.getMargin());
        match.setPlayerOfMatch(matchInput.getPlayer_of_match());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());

        //Set Toss winner as Team1 
        match.setTeam1(matchInput.getTosswinner());
        match.setTeam2((matchInput.getTosswinner().equals(match.getTeam1())) 
                        ? matchInput.getTeam2() : matchInput.getTeam1());

        //Javabrains method - Set team1 as the Team that bats first using Toss Winner and Toss Decision
        /*
            String firstInnTeam, secondInnTeam;

            if ("bat".equals(matchInput.getTossdecision())) {
                firstInnTeam = matchInput.getTosswinner();
                secondInnTeam = matchInput.getTosswinner().equals(matchInput.getTeam1()) 
                                ? matchInput.getTeam2() : matchInput.getTeam1();
            } else {
                secondInnTeam = matchInput.getTosswinner();
                firstInnTeam = matchInput.getTosswinner().equals(matchInput.getTeam1()) 
                ? matchInput.getTeam2() : matchInput.getTeam1();
            }

            match.setTeam1(firstInnTeam);
            match.setTeam2(secondInnTeam);
        */

        return match;
    }  
}
