package conexao.banco;

import java.util.Date;

public class Operacoes {
    private Integer idOperacoes;
    private String nome;
    private Integer qtdPolicias;
    private Date data;
    private String descriscao;
    private Local local;

    public Operacoes(Integer idOperacoes, String nome, Integer qtdPolicias, Date data, String descriscao, Local local) {
        this.idOperacoes = idOperacoes;
        this.nome = nome;
        this.qtdPolicias = qtdPolicias;
        this.data = data;
        this.descriscao = descriscao;
        this.local = local;
    }

    public Integer getIdOperacoes() {
        return idOperacoes;
    }

    public void setIdOperacoes(Integer idOperacoes) {
        this.idOperacoes = idOperacoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQtdPolicias() {
        return qtdPolicias;
    }

    public void setQtdPolicias(Integer qtdPolicias) {
        this.qtdPolicias = qtdPolicias;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescriscao() {
        return descriscao;
    }

    public void setDescriscao(String descriscao) {
        this.descriscao = descriscao;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return "Operacoes{" +
                "idOperacoes=" + idOperacoes +
                ", nome='" + nome + '\'' +
                ", qtdPolicias=" + qtdPolicias +
                ", data=" + data +
                ", descriscao='" + descriscao + '\'' +
                ", local=" + local +
                '}';
    }
}
