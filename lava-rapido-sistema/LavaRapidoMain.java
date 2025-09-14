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