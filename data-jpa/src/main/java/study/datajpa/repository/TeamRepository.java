package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

import java.util.List;

@Repository
public class TeamRepository {
    @PersistenceContext
    private EntityManager em;


    public Team findById(Long id){
        return em.find(Team.class,id);


    }

    public List<Team> findAll(){
        return em.createQuery("select t from Team t",Team.class)
                .getResultList();
    }

}
