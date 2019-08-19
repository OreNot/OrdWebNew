package ucproject.domain;

import javax.persistence.*;

@Entity
public class Fio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fio_seq_gen")
    @SequenceGenerator(name = "fio_seq_gen", sequenceName = "fio_sequence", initialValue = 3, allocationSize = 1)
    private Integer id;

    String fio;

    public Fio() {
    }

    public Fio(String fio) {
        this.fio = fio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}
