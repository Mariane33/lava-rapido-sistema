🚗 Sistema de Gerenciamento - Lava-Rápido
📋 Descrição do Projeto
Sistema desenvolvido para gerenciar filas de atendimento em um lava-rápido, garantindo eficiência e justiça no atendimento aos clientes. O sistema implementa algoritmos de fila e escalonamento para otimizar o uso das baias de lavagem.
🎯 Objetivos

Gerenciar fila de carros respeitando ordem de chegada (FIFO)
Otimizar uso de múltiplas baias de lavagem
Calcular tempo de espera estimado para cada cliente
Fornecer interface para monitoramento em tempo real

🛠️ Tecnologias Utilizadas

Java 8+ - Linguagem principal
Collections Framework - Gerenciamento de filas
Scanner - Interface de linha de comando
Git/GitHub - Controle de versão

📁 Estrutura do Projeto
src/main/java/com/lavarapido/
├── LavaRapidoMain.java        # Classe principal e interface
├── SistemaLavaRapido.java     # Gerenciador do sistema
├── Carro.java                 # Modelo do carro
├── Baia.java                  # Modelo da baia de lavagem
└── TipoServico.java          # Enum dos tipos de serviço
🚀 Como Executar
Pré-requisitos

Java JDK 8 ou superior instalado
Git instalado (opcional)

Passos para execução

Clone o repositório:

bash   git clone https://github.com/SEU_USUARIO/lava-rapido-sistema.git
   cd lava-rapido-sistema

Compile o projeto:

bash   javac -cp src src/main/java/com/lavarapido/*.java -d build

Execute o sistema:

bash   java -cp build com.lavarapido.LavaRapidoMain
Ou compilação simples (todos os arquivos na raiz):
bash   javac *.java
   java LavaRapidoMain
💡 Funcionalidades
⚙️ Principais Features

✅ Gerenciamento de Fila FIFO - Primeiro a chegar, primeiro a ser atendido
✅ Múltiplas Baias - Atendimento simultâneo de vários carros
✅ Tipos de Serviço Variados - Diferentes tempos de atendimento
✅ Cálculo de Tempo de Espera - Estimativa baseada na posição na fila
✅ Interface Interativa - Adicionar carros e simular tempo
✅ Status em Tempo Real - Monitoramento das baias e fila

🎮 Modo Interativo
O sistema oferece um menu interativo com as opções:

Adicionar carro - Registrar novo carro na fila
Ver status - Visualizar estado completo do sistema
Simular tempo - Acelerar passagem do tempo
Sair - Encerrar aplicação

🔧 Tipos de Serviço
ServiçoTempoDescriçãoLavagem Simples15 minLavagem básica externaLavagem Completa30 minLavagem externa + internaEnceramento45 minLavagem + enceramentoDetalhamento60 minServiço completo + detalhes
🏗️ Arquitetura
Padrões de Design Utilizados

MVC (Model-View-Controller) - Separação de responsabilidades
Strategy Pattern - Algoritmos de escalonamento
Observer Pattern - Notificações de mudança de estado

Algoritmos Implementados

FIFO (First In, First Out) - Algoritmo principal de fila
Cálculo de Tempo de Espera - Baseado em posição e capacidade
Gerenciamento de Recursos - Distribuição eficiente das baias

📊 Exemplo de Uso
java// Criar sistema com 3 baias
SistemaLavaRapido sistema = new SistemaLavaRapido(3);

// Adicionar carros à fila
sistema.adicionarCarro(new Carro("ABC-1234", TipoServico.LAVAGEM_SIMPLES));
sistema.adicionarCarro(new Carro("XYZ-5678", TipoServico.ENCERAMENTO));

// Visualizar status
sistema.exibirStatus();

// Calcular tempo de espera
long tempoEspera = sistema.calcularTempoEsperaEstimado(carro);
🔮 Possíveis Melhorias

 Interface gráfica (JavaFX/Swing)
 Banco de dados para persistência
 Sistema de prioridades (clientes VIP)
 Algoritmos de escalonamento alternativos (SJF, Priority Queue)
 Relatórios estatísticos
 API REST para integração web
 Sistema de notificações por SMS/Email

📈 Estruturas de Dados

Queue<Carro> - Fila principal (LinkedList)
List<Baia> - Lista de baias (ArrayList)
PriorityQueue<Carro> - Fila com prioridade por tempo

🧪 Testes
Cenários de Teste Implementados

Chegada sequencial - Carros chegando um após o outro
Capacidade máxima - Mais carros que baias disponíveis
Diferentes tipos de serviço - Mistura de tempos de atendimento
Simulação de tempo - Liberação automática de baias

👥 Contribuição
Este é um projeto acadêmico, mas sugestões e melhorias são bem-vindas!
Como contribuir:

Faça um fork do projeto
Crie uma branch para sua feature (git checkout -b feature/AmazingFeature)
Commit suas mudanças (git commit -m 'Add some AmazingFeature')
Push para a branch (git push origin feature/AmazingFeature)
Abra um Pull Request

📝 Licença
Este projeto é desenvolvido para fins acadêmicos. Uso livre para estudos e aprendizado.
👨‍💻 Autor

Mariane dos Santos - Desenvolvimento inicial


⭐ Se este projeto foi útil para você, considere dar uma estrela!