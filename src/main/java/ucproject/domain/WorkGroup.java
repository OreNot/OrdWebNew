package ucproject.domain;

import javax.persistence.*;

@Entity
public class WorkGroup {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workgroup_seq_gen")
    @SequenceGenerator(name = "workgroup_seq_gen", sequenceName = "workgroup_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    String name;

    public WorkGroup() {
    }

    public WorkGroup(String name) {
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

    public void setName(String workGroupName) {
        this.name = name;
    }
}
