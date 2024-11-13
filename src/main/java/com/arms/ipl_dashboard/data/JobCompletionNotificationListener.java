package com.arms.ipl_dashboard.data;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import com.arms.ipl_dashboard.model.Team;

@SuppressWarnings("deprecation")
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager em;

    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

  @SuppressWarnings("null")
  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
        log.info("JOB completed");
      
    Map<String, Team> teamDataMap = new HashMap<>();
            
    //JPA doesn't support UNION
    em.createQuery("select team1, count(1) from Match group by team1", Object[].class)
        .getResultList()
        .stream()
        .map(e -> new Team((String)e[0], (long)e[1]))
        .forEach(team -> teamDataMap.put(team.getTeamName(), team));    
  
    em.createQuery("select team2, count(1) from Match group by team2", Object[].class)
        .getResultList()
        .stream()
        .forEach(e -> {
            Team team = teamDataMap.get((String) e[0]);
            if(team != null)
                team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
            else 
                teamDataMap.put(team.getTeamName(), team);
        });

    em.createQuery("select winningteam, count(1) from Match group by winningteam", Object[].class)
        .getResultList()
        .stream()
        .forEach(e -> {
            Team team = teamDataMap.get((String) e[0]);
            if(team != null)
                team.setTotalWins((long) e[1]);
        });

        try {
            teamDataMap.values().forEach(team -> em.persist(team) );
        } catch (Exception e) {
            e.printStackTrace();
        } finally { 
            System.out.println("No exception in persist");
        }

       boolean emptyRs = em.createQuery("select teamName, totalMatches, totalWins from Team ", Object[].class)
       .getResultList().isEmpty();
       
        System.out.println("Boolean:" + emptyRs);
    }
  }
}
