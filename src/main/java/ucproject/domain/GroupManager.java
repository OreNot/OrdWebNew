package ucproject.domain;


import javax.persistence.*;

@Entity
public class GroupManager {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupmanager_seq_gen")
    @SequenceGenerator(name = "groupmanager_seq_gen", sequenceName = "groupmanager_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "fio_id")
    private Fio fio;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "workgroup_id")
    private WorkGroup workGroup;

    public GroupManager() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Fio getFio() {
        return fio;
    }

    public void setFio(Fio fio) {
        this.fio = fio;
    }

    public WorkGroup getWorkGroup() {
        return workGroup;
    }

    public void setWorkGroup(WorkGroup workGroup) {
        this.workGroup = workGroup;
    }
}
