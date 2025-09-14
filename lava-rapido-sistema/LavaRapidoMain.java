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

// Classe principal para demonstração
public class LavaRapidoMain {
    public static void main(String[] args) {
        // Criar sistema com 3 baias
        SistemaLavaRapido sistema = new SistemaLavaRapido(3);
        
        System.out.println("🚙 SISTEMA DE GERENCIAMENTO - LAVA-RÁPIDO");
        System.out.println("Iniciando com 3 baias de lavagem\n");
        
        // Simular chegada de carros
        sistema.adicionarCarro(new Carro("ABC-1234", TipoServico.LAVAGEM_SIMPLES));
        sistema.adicionarCarro(new Carro("XYZ-5678", TipoServico.LAVAGEM_COMPLETA));
        sistema.adicionarCarro(new Carro("DEF-9876", TipoServico.ENCERAMENTO));
        sistema.adicionarCarro(new Carro("GHI-5432", TipoServico.DETALHAMENTO));
        sistema.adicionarCarro(new Carro("JKL-1111", TipoServico.LAVAGEM_SIMPLES));
        
        // Exibir status inicial
        sistema.exibirStatus();
        
        // Simular passagem de tempo
        sistema.simularTempo(20);
        
        // Adicionar mais carros
        sistema.adicionarCarro(new Carro("MNO-2222", TipoServico.LAVAGEM_COMPLETA));
        sistema.adicionarCarro(new Carro("PQR-3333", TipoServico.LAVAGEM_SIMPLES));
        
        // Status final
        sistema.exibirStatus();
        
        // Demonstrar uso interativo
        demonstrarUsoInterativo(sistema);
    }
    
    private static void demonstrarUsoInterativo(SistemaLavaRapido sistema) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n🎯 MODO INTERATIVO (digite 'sair' para terminar)");
        
        while (true) {
            System.out.println("\nOpções:");
            System.out.println("1. Adicionar carro");
            System.out.println("2. Ver status");
            System.out.println("3. Simular tempo");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1":
                    System.out.print("Digite a placa do carro: ");
                    String placa = scanner.nextLine();
                    
                    System.out.println("Tipos de serviço:");
                    TipoServico[] tipos = TipoServico.values();
                    for (int i = 0; i < tipos.length; i++) {
                        System.out.println((i + 1) + ". " + tipos[i].getDescricao() + 
                                         " (" + tipos[i].getTempoMinutos() + " min)");
                    }
                    
                    System.out.print("Escolha o tipo de serviço (1-" + tipos.length + "): ");
                    try {
                        int tipoIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if (tipoIndex >= 0 && tipoIndex < tipos.length) {
                            sistema.adicionarCarro(new Carro(placa, tipos[tipoIndex]));
                        } else {
                            System.out.println("❌ Opção inválida!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Por favor, digite um número válido!");
                    }
                    break;
                    
                case "2":
                    sistema.exibirStatus();
                    break;
                    
                case "3":
                    System.out.print("Quantos minutos simular? ");
                    try {
                        long minutos = Long.parseLong(scanner.nextLine());
                        sistema.simularTempo(minutos);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Por favor, digite um número válido!");
                    }
                    break;
                    
                case "4":
                case "sair":
                    System.out.println("👋 Encerrando sistema...");
                    return;
                    
                default:
                    System.out.println("❌ Opção inválida!");
            }
        }
    }
}