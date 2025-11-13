# Avaliação Padrões de Projeto 2° Bimestre

- **Aluno**: Arthur Phelipe Mayer Santos
- **RGM**: 29211247

## Questão 1: Algoritmos de Risco (Strategy)

**Padrão Escolhido: Strategy**

Por quê?

1. **Encapsulamento de Algoritmos**: O requisito principal era ter uma "família" de algoritmos que pudessem ser trocados. O padrão Strategy é feito exatamente para isso: ele encapsula cada algoritmo (estratégia) em sua própria classe ( `ValueAtRiskStrategy` , `StressTestingStrategy` ), todas implementando uma interface comum ( `RiskAlgorithm` ).

2. **Intercambialidade em Runtime**: O padrão permite que o objeto de "Contexto" (o `RiskProcessor` ) tenha uma referência à interface da estratégia. Isso permite que o cliente troque a implementação concreta (o algoritmo) a qualquer momento usando o método `setAlgorithm()`, sem qualquer `if/else` ou `switch` no código do processador.

3. **Desacoplamento (SOLID - D)**: O `RiskProcessor` (módulo de alto nível) não depende das implementações concretas (módulos de baixo nível). Ambos dependem da abstração (`RiskAlgorithm`). Isso satisfaz a **Inversão de Dependência**.

4. **Princípio Aberto/Fechado (SOLID - O)**: O `RiskProcessor` está **fechado para modificação**, mas **aberto para extensão**. Se a empresa criar um novo "Algoritmo de Risco X", nós apenas criamos a classe `AlgoritmoXRiskStrategy` e a injetamos. Nenhuma linha de código no `RiskProcessor` precisa ser alterada.

## Questão 2: Integração com Legado (Adapter)

**Padrão Escolhido: Adapter**

Por quê?

1. **Compatibilização de Interfaces**: Este é o propósito canônico do Adapter. O padrão atua como um "tradutor" ou "ponte" que se encaixa entre o cliente e o sistema legado. O cliente chama o método `autorizar()` do adaptador, e o adaptador faz todo o "trabalho sujo" de tradução.

2. **Encapsulamento da Complexidade**: Toda a lógica de tradução fica encapsulada dentro do `AdapterBancario`. Isso inclui:

    - **Tradução de Dados**: Converter `"BRL"` para o código `3`.

    - **Mapeamento de Estrutura**: Montar o `HashMap` complexo que o legado espera.

    - **Adição de Dados Padrão**: Incluir campos obrigatórios do legado que o cliente não fornece (como o `CODIGO_LOJA`).

3. **Tradução Bidirecional**: O adaptador não serve apenas para "enviar" a chamada. Ele também foi usado para traduzir a resposta. O `HashMap` de resposta do legado foi convertido de volta para um DTO `RespostaAutorizacao` limpo, tornando o sistema legado completamente invisível para o cliente.

4. **Responsabilidade Única (SOLID - S)**: O adaptador tem uma única responsabilidade: adaptar. A lógica de negócio moderna (cliente) e a lógica de negócio legada (sistema) permanecem em suas próprias classes, perfeitamente desacopladas.

## Questão 3: Máquina de Estados (State)

**Padrão Escolhido: State**

Por quê?

1. **Evitar Condicionais Complexas**: A alternativa seria um método `verificarSensores()` na classe UsinaNuclear com um `switch` ou `if-else` gigante para tratar cada estado. Isso é um pesadelo de manutenção e viola o Princípio Aberto/Fechado.

2. **Coesão e Responsabilidade Única (SOLID - S)**: O State Pattern move toda a lógica de um estado específico para sua própria classe. A classe `AlertaAmareloState` tem uma única responsabilidade: saber o que fazer enquanto estiver em alerta amarelo e para quais estados ela pode ir (`OPERACAO_NORMAL` ou `ALERTA_VERMELHO`). O comportamento de cada estado fica coeso e encapsulado.

3. **Prevenção de Transições Inválidas**: O padrão torna impossível realizar transições perigosas. A classe `OperacaoNormalState` só conhece os estados `AlertaAmareloState` e `DesligadaState`. Ela nem sabe da existência do `EmergenciaState`. Isso garante programaticamente que a regra "só pode ir para EMERGENCIA via ALERTA_VERMELHO" seja cumprida.

4. **Sobrescrita de Comportamento**: O requisito de "modo manutenção" (Req. 5) foi elegantemente resolvido tratando-o como mais um estado. Quando a usina entra no `ManutencaoState`, seu método `verificarCondicoes()` (que ignora os sensores) "sobreescreve" o comportamento de todos os outros estados.

## Questão 4: Cadeia de Validação (Chain of Responsibility)

**Padrão Escolhido: Chain of Responsibility**

Por quê?

1. **Desacoplamento de Validadores**: O CoR é perfeito para "cadeias". Cada validador (`Validator`) é um elo. Ele só precisa conhecer a interface `Validator` e quem é o seu "próximo" (`next`). Isso desacopla o `XmlSchemaValidator` do `CertificateValidator`.

2. **Flexibilidade e Aberto/Fechado (SOLID - O)**: Podemos reordenar a cadeia ou adicionar um novo `SextoValidador` no meio dela sem alterar nenhuma das classes de validação existentes.

3. **Controle Avançado via Objeto de Contexto**: O CoR simples apenas passa a requisição. Para os requisitos avançados, foi usada uma variação poderosa do padrão onde um objeto `ValidationContext` é passado através da cadeia. Este objeto foi crucial:

    - Circuit Breaker (Req. 3): O `ValidationContext` carregava o contador de falhas (`getFailureCount()`). O `AbstractValidator` verificava `context.isCircuitOpen()` antes de executar.

    - Rollback (Req. 4 e Restr. 2): O `DatabaseValidator` adicionava uma ação `Runnable` de rollback à pilha (`Deque`) dentro do `ValidationContext`. Quando o SefazValidator falhou, o cliente (o `main`) detectou a falha (`context.hasFailed()`) e chamou `context.executeRollbacks()`, que executou as ações da pilha em ordem LIFO (Last-In, First-Out).

    - Parada Condicional (Restr. 1): A lógica no `AbstractValidator` verifica o `ValidationResult` (SUCCESS, SOFT_FAIL, HARD_FAIL). Se um validador (como o `XmlSchemaValidator`) retorna HARD_FAIL, o `AbstractValidator` deliberadamente não chama o `next.validate()`, interrompendo a cadeia.

    - Timeouts (Restr. 3): A lógica de medição de tempo foi centralizada no `AbstractValidator`, que "envolve" a chamada ao `performValidation()` da subclasse.