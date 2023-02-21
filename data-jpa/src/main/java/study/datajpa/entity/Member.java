package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedQuery( //실무에서 거의 사용 안한다. 하지만 앱 로딩 시점에 해당 쿼리를 파싱하기 때문에 오류가 안나온다.
        name="Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
@ToString(of = {"id", "username","age"})
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {

        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username=username;
        this.age=age;
        if(team !=null){
            this.changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username=username;
        this.age=age;
    }

    public void changeTeam(Team team){
        this.team=team;
        team.getMembers().add(this);
    }


}
