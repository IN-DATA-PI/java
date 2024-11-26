package conexao.banco;

import noticacoes.slack.Slack;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public class Departamento {
    private Integer idDepartamento;
    private String nome;
    private String telefone;
    private String email;
    private String descricao;
    private String sigla;
    private List<Roubos> roubos;
    private List<Slack> notificacoes;


    public Departamento(Integer idDepartamento, String nome, String telefone, String email, String descricao, String sigla, List<Roubos> roubos, List<Slack> notificacoes) {
        this.idDepartamento = idDepartamento;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.descricao = descricao;
        this.sigla = sigla;
        this.roubos = roubos;
        this.notificacoes = notificacoes;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public List<Roubos> getRoubos() {
        return roubos;
    }

    public void setRoubos(List<Roubos> roubos) {
        this.roubos = roubos;
    }

    public List<Slack> getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(List<Slack> notificacoes) {
        this.notificacoes = notificacoes;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "idDepartamento=" + idDepartamento +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", descricao='" + descricao + '\'' +
                ", sigla='" + sigla + '\'' +
                ", roubos=" + roubos +
                ", notificacoes=" + notificacoes +
                '}';
    }
}
