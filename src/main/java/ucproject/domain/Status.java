package ucproject.domain;

import javax.persistence.*;


@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_seq_gen")
    @SequenceGenerator(name = "status_seq_gen", sequenceName = "status_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    private String name;

    public Status() {
    }

    public Status(String name) {
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
