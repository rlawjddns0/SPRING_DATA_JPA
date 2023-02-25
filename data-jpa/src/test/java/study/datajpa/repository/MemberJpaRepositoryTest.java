package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired MemberRepository memberRepository;

    @DisplayName("MemberRepsitory Test")
    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");

        //when
        Member saveMember = memberJpaRepository.save(member);

        //then
        Member findMember = memberJpaRepository.find(saveMember.getId());
        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    @DisplayName("basic crud test")
    public void basicCRUD(){
        Member member1 = new Member("member4");
        Member member2 = new Member("member5");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1=memberJpaRepository.findById(member1.getId()).get();
        Member findMember2=memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

    }
    @Test
    public void test11(){
        Member m1 = new Member("ssss",10);
        Member m2 = new Member("ssss111",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result=memberRepository.findByUsernameAndAgeGreaterThan("ssss111", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("ssss111");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findByHello(){
        Member m1 = new Member("ssss",10);
        Member m2 = new Member("ssss111",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result=memberRepository.findHelloBy();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo(m1.getUsername());
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("ssss",10);
        Member m2 = new Member("ssss111",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("ssss", "ssss111"));
        for (Member member : result) {
            System.out.println(member);

        }

    }

    @Test
    public void paging(){
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));
        memberJpaRepository.save(new Member("member6",10));

        int age=10;
        int offset=0;
        int limit=3;
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);


        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(6);

    }

    @Test
    public void findMemberByLike(){
        memberJpaRepository.save(new Member("sss",10));
        memberJpaRepository.save(new Member("www",11));
        memberJpaRepository.save(new Member("sdw",12));
        memberJpaRepository.save(new Member("ssees",15));
        memberJpaRepository.save(new Member("sssssstt",20));
        memberJpaRepository.save(new Member("bbrbr",24));

        List<Member> result = memberRepository.findMemberByUsernameLike("%ss%");
        System.out.println(result.size());
        for (Member member : result) {
            System.out.println(member);
        }

    }

    @Test
    public void bulkUpdate(){
        memberJpaRepository.save(new Member("sss",10));
        memberJpaRepository.save(new Member("www",19));
        memberJpaRepository.save(new Member("sdw",120));
        memberJpaRepository.save(new Member("ssees",21));
        memberJpaRepository.save(new Member("sssssstt",2));
        memberJpaRepository.save(new Member("bbrbr",24));

        int resultCount=memberJpaRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);

    }


}