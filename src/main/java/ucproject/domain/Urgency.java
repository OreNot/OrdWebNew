package ucproject.domain;


import javax.persistence.*;

@Entity
public class Urgency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urgency_seq_gen")
    @SequenceGenerator(name = "urgency_seq_gen", sequenceName = "urgency_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    String name;

    public Urgency() {
    }

    public Urgency(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
