import java.util.*;
import java.util.concurrent.PriorityQueue;

// Classe principal do sistema de gerenciamento
class SistemaLavaRapido {
    private Queue<Carro> filaEspera;
    private List<Baia> baias;
    private List<Carro> carrosAtendidos;
    private PriorityQueue<Carro> filaPrioridade; // Para ordenar por tempo de espera
    
    public SistemaLavaRapido(int numeroBaias) {
        this.filaEspera = new LinkedList<>();
        this.baias = new ArrayList<>();
        this.carrosAtendidos = new ArrayList<>();
        this.filaPrioridade = new PriorityQueue<>((c1, c2) -> 
            Long.compare(c1.getTempoChegada(), c2.getTempoChegada()));
        
        // Inicializar baias
        for (int i = 1; i <= numeroBaias; i++) {
            baias.add(new Baia(i));
        }
    }
    
    // Adicionar carro à fila
    public void adicionarCarro(Carro carro) {
        filaEspera.offer(carro);
        filaPrioridade.offer(carro);
        System.out.println("✓ " + carro + " adicionado à fila");
        processarFila();
    }
    
    // Processar a fila e atribuir carros às baias disponíveis
    public void processarFila() {
        Iterator<Baia> iterator = baias.iterator();
        
        while (iterator.hasNext() && !filaEspera.isEmpty()) {
            Baia baia = iterator.next();
            
            if (baia.isDisponivel()) {
                Carro proximoCarro = determinarProximoCarro();
                if (proximoCarro != null) {
                    baia.ocupar(proximoCarro);
                    filaEspera.remove(proximoCarro);
                    filaPrioridade.remove(proximoCarro);
                    
                    System.out.println("🚗 " + proximoCarro + " iniciou atendimento na " + 
                                     "Baia " + baia.getNumero());
                }
            }
        }
    }
    
    // Algoritmo para determinar próximo carro (FIFO com consideração de tempo de serviço)
    private Carro determinarProximoCarro() {
        if (filaEspera.isEmpty()) return null;
        
        // Estratégia: FIFO puro (ordem de chegada)
        return filaEspera.peek();
        
        /* Alternativa - Estratégia híbrida (descomentar se desejar):
        // Priorizar carros com serviços mais rápidos se a fila estiver grande
        if (filaEspera.size() > 5) {
            return filaEspera.stream()
                .min(Comparator.comparing(c -> c.getTipoServico().getTempoMinutos()))
                .orElse(filaEspera.peek());
        }
        return filaEspera.peek();
        */
    }
    
    // Calcular tempo de espera estimado para um carro
    public long calcularTempoEsperaEstimado(Carro carro) {
        long tempoAtual = System.currentTimeMillis();
        long tempoEsperaBase = tempoAtual - carro.getTempoChegada();
        
        // Calcular quantos carros estão na frente
        int posicaoNaFila = 0;
        for (Carro c : filaEspera) {
            if (c.getTempoChegada() < carro.getTempoChegada()) {
                posicaoNaFila++;
            }
            if (c.equals(carro)) break;
        }
        
        // Estimar tempo baseado na capacidade das baias
        int baiasDisponiveis = (int) baias.stream().mapToLong(b -> b.isDisponivel() ? 1 : 0).sum();
        if (baiasDisponiveis == 0) {
            // Encontrar a baia que vai liberar mais cedo
            long menorTempoLiberacao = baias.stream()
                .mapToLong(Baia::getTempoLiberacao)
                .min().orElse(tempoAtual);
            tempoEsperaBase += (menorTempoLiberacao - tempoAtual);
        }
        
        // Adicionar tempo estimado baseado na posição na fila
        long tempoEstimadoServicos = posicaoNaFila * 25 * 60000L / baias.size(); // 25 min média
        
        return (tempoEsperaBase + tempoEstimadoServicos) / 60000L; // Retornar em minutos
    }
    
    // Exibir status completo do sistema
    public void exibirStatus() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 STATUS DO LAVA-RÁPIDO");
        System.out.println("=".repeat(60));
        
        // Status das baias
        System.out.println("🏪 BAIAS:");
        for (Baia baia : baias) {
            System.out.println("  " + baia);
        }
        
        // Fila de espera
        System.out.println("\n⏳ FILA DE ESPERA (" + filaEspera.size() + " carros):");
        if (filaEspera.isEmpty()) {
            System.out.println("  Nenhum carro na fila");
        } else {
            int posicao = 1;
            for (Carro carro : filaEspera) {
                long tempoEspera = calcularTempoEsperaEstimado(carro);
                System.out.println(String.format("  %d. %s - Espera estimada: %d min", 
                                 posicao++, carro, tempoEspera));
            }
        }
        
        System.out.println("=".repeat(60));
    }
    
    // Simular passagem de tempo (para testes)
    public void simularTempo(long minutos) {
        System.out.println("⏰ Simulando " + minutos + " minutos...");
        try {
            Thread.sleep(1000); // Simular 1 segundo = tempo acelerado
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        processarFila();
    }
    
    // Getters para estatísticas
    public int getTotalCarrosNaFila() { return filaEspera.size(); }
    public int getBaiasOcupadas() { return (int) baias.stream().mapToLong(b -> b.isOcupada() ? 1 : 0).sum(); }
    public int getBaiasDisponiveis() { return baias.size() - getBaiasOcupadas(); }
}

