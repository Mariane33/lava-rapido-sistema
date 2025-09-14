// Enum para tipos de serviço
enum TipoServico {
    LAVAGEM_SIMPLES(15, "Lavagem Simples"),
    LAVAGEM_COMPLETA(30, "Lavagem Completa"),
    ENCERAMENTO(45, "Enceramento"),
    DETALHAMENTO(60, "Detalhamento Completo");
    
    private final int tempoMinutos;
    private final String descricao;
    
    TipoServico(int tempoMinutos, String descricao) {
        this.tempoMinutos = tempoMinutos;
        this.descricao = descricao;
    }
    
    public int getTempoMinutos() { return tempoMinutos; }
    public String getDescricao() { return descricao; }
}
