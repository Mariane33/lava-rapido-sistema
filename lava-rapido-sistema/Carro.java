// Classe que representa um carro na fila
class Carro {
    private static int contadorId = 1;
    private int id;
    private String placa;
    private TipoServico tipoServico;
    private long tempoChegada;
    private long tempoInicioAtendimento;
    private long tempoFimAtendimento;
    
    public Carro(String placa, TipoServico tipoServico) {
        this.id = contadorId++;
        this.placa = placa;
        this.tipoServico = tipoServico;
        this.tempoChegada = System.currentTimeMillis();
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public String getPlaca() { return placa; }
    public TipoServico getTipoServico() { return tipoServico; }
    public long getTempoChegada() { return tempoChegada; }
    public long getTempoInicioAtendimento() { return tempoInicioAtendimento; }
    public void setTempoInicioAtendimento(long tempo) { this.tempoInicioAtendimento = tempo; }
    public long getTempoFimAtendimento() { return tempoFimAtendimento; }
    public void setTempoFimAtendimento(long tempo) { this.tempoFimAtendimento = tempo; }
    
    @Override
    public String toString() {
        return String.format("Carro[ID:%d, Placa:%s, Serviço:%s]", 
                           id, placa, tipoServico.getDescricao());
    }
}
