package study.datajpa.repository;


import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

import javax.persistence.*;
import java.util.List;

@Repository
public class TeamJpaRepository {
    @PersistenceContext
    private EntityManager em;


    public Team save(Team team){
        em.persist(team);
        return team;
    }
    public void delete(Team team){
        em.remove(team);

    }
    public Team findById(Long id){
        return em.find(Team.class,id);
    }

    public List<Team> findAll(){
        return em.createQuery("select t from Team t",Team.class)
                .getResultList();
    }

    public Long count(){
        return em.createQuery("select count(t) from Team t",Long.class).getSingleResult();
    }

}
