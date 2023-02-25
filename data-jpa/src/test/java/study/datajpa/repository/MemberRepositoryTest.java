package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;



    @DisplayName("Spring Data Jpa Test")
    @Test
    public void springDataTest() throws Exception {
        //given
        Member saveMember = new Member("MemberB");
        memberRepository.save(saveMember);

        //when
        Member findMember = memberRepository.findById(saveMember.getId()).get();



        //then
        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());

    }

    @Test
    @DisplayName("basic crud test")
    public void basicCRUD(){
        Member member1 = new Member("member4");
        Member member2 = new Member("member5");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1=memberRepository.findById(member1.getId()).get();
        Member findMember2=memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

    }

    @Test
    @DisplayName("basic crud test")
    public void test2233(){
        Member member1 = new Member("member4",19);
        Member member2 = new Member("member5",20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result=memberRepository.findUser("member4",19);


        assertThat(result.get(0).getUsername()).isEqualTo("member4");

    }

    @Test
    public void findUserName(){

        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member1 = new Member("member4",19,team);
        memberRepository.save(member1);

        List<MemberDto> result=memberRepository.findByDto();
        for (MemberDto memberDto : result) {
            System.out.println(memberDto.toString());
        }
    }

    @Test
    public void findMemberDto(){
        Member member1 = new Member("member4",19);
        Member member2 = new Member("member5",20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result=memberRepository.findUsernameList();
        for (String s : result) {
            System.out.println(s);

        }
    }

    @Test
    public void returnTypeTest(){
        Member member1 = new Member("member4",19);
        Member member2 = new Member("member5",20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> listMember = memberRepository.findListByUsername("member4");//컬렉션 조회-> member4가 없으면 빈 컬렉션을준다(null이 아님)

        //단건 조회는 무조건 단건만 나와야 한다. 결과가 2개 이상 나와버리면 Exception 터짐짐
       Member findMember = memberRepository.findMemberByUsername("member4"); //Optionalx 단건조회 -> member4가 없으면 null을 반환
        Optional<Member> findMemberOptional = memberRepository.findMemberOptionalByUsername("member4"); //Optional 단건 -> member4가 없으면null도 아니고 optional 객체로 감싸서 온다.

    }

    @Test
    public void paging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));
        memberRepository.save(new Member("member6",10));

        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
//        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getUsername()));//유용하게 dto로 변환 해야한다.

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();
//        for (Member member : content) {
//            System.out.println(member.toString());
//        }
//        System.out.println(totalElements);
        assertThat(content.size()).isEqualTo(3);//size만큼 가져왔는지
        assertThat(page.getTotalElements()).isEqualTo(6);//총 몇개인지
        assertThat(page.getNumber()).isEqualTo(0);//현재 몇 페이지인지
        assertThat(page.getTotalPages()).isEqualTo(2);//총 몇페이지인지
        assertThat(page.isFirst()).isTrue();//첫 페이지가 맞는지
        assertThat(page.hasNext()).isTrue();//다음 페이지가 있는지


    }

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("sss",10));
        memberRepository.save(new Member("www",19));
        memberRepository.save(new Member("sdw",120));
        memberRepository.save(new Member("ssees",21));
        memberRepository.save(new Member("sssssstt",2));
        memberRepository.save(new Member("bbrbr",24));


        //벌크연산은 영속성 컨텍스트를 무시하고 바로 디비로 업데이트 날린다.
        int resultCount=memberRepository.bulkAgePlus(20);
//        em.clear();

        Member member = memberRepository.findMemberByUsername("bbrbr");

        System.out.println(member.toString());


        assertThat(resultCount).isEqualTo(3);

    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1",10,teamA));
        memberRepository.save(new Member("member2",19,teamB));

        em.flush();
        em.clear();

        //when
//        List<Member> members = memberRepository.findAll();//LAZY이기 때문에 멤버만 가져온다. team은 null로 남겨둘수 없기 때문에 가짜객체를 가져온다.
//        List<Member> members = memberRepository.findMemberFetchJoin();//fetch join으로 한방에 다 가져온다.
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member= "+ member.getUsername());
            System.out.println("member.team= "+ member.getTeam().getName());//이때 팀을 가져온다.
        }

    }

}