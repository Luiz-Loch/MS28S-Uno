# Uno card game created by Josh Braza
![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPmGsqMocCsAkoqM3FpvqH7abDSASIqjhJKMfGziZl4mPcFw-x)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/c4f7241d53e541839380d71e0ab507b8)](https://app.codacy.com/gh/paivapereira/MS28S-Uno/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

# MS28S-Uno

**Jogo UNO (Java)**

Repositório com uma implementação do jogo de cartas *Uno*, originalmente criada por Joshua Braza e aqui mantida como fork. Este projeto é uma aplicação Java baseada em Gradle que implementa a lógica e a interface (console/GUI conforme a versão) do jogo Uno.

---

## Tabela de conteúdos

* Changelog
* Sobre
* Recursos
* Requisitos
* Instalação
* Como executar
* Como jogar (rápido)
* Estrutura do projeto
* Licença

---

## Changelog

### Funcionalidades adicionadas
- **Empilhamento de `+2`** — agora cartas `+2` podem ser empilhadas sucessivamente. Quando jogadores encadeiam `+2`, o efeito acumula-se conforme a política da implementação (o próximo jogador deve comprar a soma ou responder com outra carta `+2`, conforme as regras do jogo).
- **Regra do `9`** — a "Regra do 9" exige uma reação rápida dos jogadores sempre que uma carta de número 9 é jogada. O último jogador humano a reagir no tempo estipulado deve ser penalizado com a compra de 3 cartas.
- **PC curinga** — quando o PC joga uma carta Curinga, ele deve ter uma lógica simples (heurística) para escolher a próxima cor do jogo, em vez de escolher automaticamente e aleatoriamente. Essa escolha deve ser baseada na cor com maior ocorrência na sua mão, maximizando suas chances de seguir jogando.
  
### Evolução
- **Refatoração de código** — melhorias de estrutura e organização do código para facilitar manutenção e extensão (renomeação de classes, extração de métodos, organização de pacotes).
- **Melhorias nas mensagens/UX do console/GUI** — pequenas melhorias na legibilidade e clareza das mensagens exibidas ao jogador.
- **Migração para Gradle** — o projeto agora utiliza Gradle como sistema de build, com estrutura padronizada, dependências gerenciadas e execução facilitada por tasks (`./gradlew run`, `java(buildArtifact): MS28S-UNO` no VS Code).

### Correção
- **Bugfixes variados** — correções de bugs detectados em regras de turno, verificação de vitória e comportamento de cartas especiais (por exemplo: correções relacionadas à atualização do estado após efeitos de cartas).

### Integração
- **Pipeline CI** — adicionada integração contínua com **GitHub Actions** (workflow para build/test/linters).
- **Codecificação / Quality Gate** — adicionado badge do **Codacy** no README (indicador de qualidade/coverage / análise estática conforme configuração atual).

---

## Sobre

Este projeto implementa as regras básicas do Uno, incluindo compra de cartas, descartes, cartas de ação (pular, inverter, +2, +4, coringa) e verificação de vitória. Foi originalmente criado por Joshua Braza; este repositório é um fork e contém o código fonte em Java.

---

## Recursos

* Implementação das regras do Uno
* Lógica de turno e efeitos de cartas de ação
* Suporte para múltiplos jogadores (local/CPU)
* Build com Gradle

---

## Requisitos

* Java JDK 8 ou superior
* Gradle (opcional — o projeto inclui `gradlew` para uso sem instalação global)
* Git (para clonar o repositório)

---

## Instalação

Clone o repositório e faça o build:

```bash
# clonar
git clone https://github.com/Luiz-Loch/MS28S-Uno.git
cd MS28S-Uno
```
```bash
# build usando o wrapper do Gradle (recomendado)
# macOS / Linux
./gradlew build
```
```bash
# Windows
gradlew.bat build
```

O `build` compilará os fontes e gerará artefatos na pasta `build/libs/` (se o projeto estiver configurado para gerar um JAR).

---

## Como executar

Dependendo da configuração do `build.gradle`, você pode executar de duas formas comuns:

1. Usando o task do Gradle (se existir `application` plugin):

```bash
# macOS / Linux
./gradlew run
```
```bash
# Windows
gradlew.bat run
```

2. Executando o JAR gerado diretamente:

```bash
java -jar build/libs/<nome-do-jar-gerado>.jar
```

3. **plugin Java do VS Code**

* No VS Code, abra o menu **Terminal > Run Task...** (ou pressione `Ctrl+Shift+P` e escolha **Tasks: Run Task**).
* Selecione a task **`java(buildArtifact): MS28S-UNO`**.

---

## Como jogar (rápido)

As regras seguem o Uno tradicional:

* Cada jogador começa com 7 cartas.
* No seu turno, o jogador deve descartar uma carta compatível em cor, número ou símbolo.
* Cartas especiais: `+2`, `+4`, `inverter`, `pular`, `coringa` (muda cor).
* Ao ficar com 1 carta, diga "UNO" — o não cumprimento pode ter penalidade (ver regras do código).
* O primeiro jogador a não ter cartas vence.

Consulte o código / documentação interna para detalhes exatos da implementação (ex.: regras de empilhamento de +2/+4, obrigatoriedade de comprar, etc.).

---

## Estrutura do projeto (visão geral)

* `src/main/java` — código fonte Java
* `out/` — classes compiladas (gerado)
* `build.gradle` — configuração do Gradle
* `gradlew`, `gradlew.bat` — wrappers do Gradle
* `LICENSE` — licença MIT

---

# License MIT

Copyright (c) 2019 Joshua Braza

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
