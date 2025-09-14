// Classe que representa uma baia de lavagem
class Baia {
    private int numero;
    private boolean ocupada;
    private Carro carroAtual;
    private long tempoLiberacao;
    
    public Baia(int numero) {
        this.numero = numero;
        this.ocupada = false;
    }
    
    public boolean isDisponivel() {
        if (ocupada && System.currentTimeMillis() >= tempoLiberacao) {
            liberar();
        }
        return !ocupada;
    }
    
    public void ocupar(Carro carro) {
        this.carroAtual = carro;
        this.ocupada = true;
        long agora = System.currentTimeMillis();
        carro.setTempoInicioAtendimento(agora);
        this.tempoLiberacao = agora + (carro.getTipoServico().getTempoMinutos() * 60000L);
        carro.setTempoFimAtendimento(this.tempoLiberacao);
    }
    
    public void liberar() {
        this.carroAtual = null;
        this.ocupada = false;
        this.tempoLiberacao = 0;
    }
    
    // Getters
    public int getNumero() { return numero; }
    public boolean isOcupada() { return ocupada; }
    public Carro getCarroAtual() { return carroAtual; }
    public long getTempoLiberacao() { return tempoLiberacao; }
    
    @Override
    public String toString() {
        if (ocupada) {
            long minutosRestantes = (tempoLiberacao - System.currentTimeMillis()) / 60000L;
            return String.format("Baia %d: OCUPADA - %s (≈%d min restantes)", 
                               numero, carroAtual.toString(), Math.max(0, minutosRestantes));
        }
        return String.format("Baia %d: DISPONÍVEL", numero);
    }
}
