package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {
    @PersistenceContext
    EntityManager em;


    @Test
    public void testEntity() throws Exception {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);


        //when
        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",11,teamA);
        Member member3 = new Member("member3",22,teamB);
        Member member4 = new Member("member4",33,teamB);
        Member member5 = new Member("member5",25,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);

        em.flush();
        em.clear();


        //then
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member= " + member);
            System.out.println("member.team= " + member.getTeam());
        }


    }

}