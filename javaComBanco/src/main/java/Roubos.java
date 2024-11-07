public class Roubos {

    private Integer idRoubos;
    private String dp;
    private String natureza;
    private Integer ano;
    private Integer janeiro;
    private Integer fevereiro;
    private Integer marco;
    private Integer abril;
    private Integer maio;
    private Integer junho;
    private Integer julho;
    private Integer agosto;
    private Integer setembro;
    private Integer outubro;
    private Integer novembro;
    private Integer dezembro;
    private Double total;


    public Roubos() {

    }

    public Roubos(Integer idRoubos, String dp, String natureza, Integer ano, Integer janeiro, Integer fevereiro, Integer marco, Integer abril, Integer junho, Integer maio, Integer julho, Integer agosto, Integer setembro, Integer outubro, Integer novembro, Integer dezembro, Double total) {
        this.idRoubos = idRoubos;
        this.dp = dp;
        this.natureza = natureza;
        this.ano = ano;
        this.janeiro = janeiro;
        this.fevereiro = fevereiro;
        this.marco = marco;
        this.abril = abril;
        this.junho = junho;
        this.maio = maio;
        this.julho = julho;
        this.agosto = agosto;
        this.setembro = setembro;
        this.outubro = outubro;
        this.novembro = novembro;
        this.dezembro = dezembro;
        this.total = total;
    }

    public Integer getIdRoubos() {
        return idRoubos;
    }

    public void setIdRoubos(Integer idRoubos) {
        this.idRoubos = idRoubos;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getJaneiro() {
        return janeiro;
    }

    public void setJaneiro(Integer janeiro) {
        this.janeiro = janeiro;
    }

    public Integer getFevereiro() {
        return fevereiro;
    }

    public void setFevereiro(Integer fevereiro) {
        this.fevereiro = fevereiro;
    }

    public Integer getMarco() {
        return marco;
    }

    public void setMarco(Integer marco) {
        this.marco = marco;
    }

    public Integer getAbril() {
        return abril;
    }

    public void setAbril(Integer abril) {
        this.abril = abril;
    }

    public Integer getMaio() {
        return maio;
    }

    public void setMaio(Integer maio) {
        this.maio = maio;
    }

    public Integer getJunho() {
        return junho;
    }

    public void setJunho(Integer junho) {
        this.junho = junho;
    }

    public Integer getJulho() {
        return julho;
    }

    public void setJulho(Integer julho) {
        this.julho = julho;
    }

    public Integer getAgosto() {
        return agosto;
    }

    public void setAgosto(Integer agosto) {
        this.agosto = agosto;
    }

    public Integer getSetembro() {
        return setembro;
    }

    public void setSetembro(Integer setembro) {
        this.setembro = setembro;
    }

    public Integer getOutubro() {
        return outubro;
    }

    public void setOutubro(Integer outubro) {
        this.outubro = outubro;
    }

    public Integer getNovembro() {
        return novembro;
    }

    public void setNovembro(Integer novembro) {
        this.novembro = novembro;
    }

    public Integer getDezembro() {
        return dezembro;
    }

    public void setDezembro(Integer dezembro) {
        this.dezembro = dezembro;
    }

    public Double getTotal() {
        System.out.println(janeiro);
        return total;
    }

    public void setTotal(int total) {
        this.total = (double) total;
    }

    @Override
    public String toString() {
        return "Roubos{" +
                "idDados=" + idRoubos +
                ", dp='" + dp + '\'' +
                ", natureza='" + natureza + '\'' +
                ", ano=" + ano +
                 janeiro +
                ", fevereiro" + fevereiro +
                ", marco=" + marco +
                ", abril=" + abril +
                ", maio=" + maio +
                ", junho=" + junho +
                ", julho=" + julho +
                ", agosto=" + agosto +
                ", setembro=" + setembro +
                ", outubro=" + outubro +
                ", novembro=" + novembro +
                ", dezembro=" + dezembro +
                ", total=" + total +
                '}';

    }
}
