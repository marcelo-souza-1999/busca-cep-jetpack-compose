# 📬 Busca CEP - Jetpack Compose

> Aplicativo para buscar informações de endereço a partir de um CEP, utilizando **Jetpack Compose**
> e boas práticas modernas.  
> O usuário digita um CEP e obtém **rua**, **bairro**, **cidade** e **estado**, com tratamento de
> erros e navegação para detalhes do cep.

## ⚙️ Tecnologias utilizadas

- **Kotlin**: Linguagem principal.
- **JDK 21**: Versão mínima recomendada.
- **Jetpack Compose (Material Design 3)**: UI declarativa e componentes modernos.
- **Koin Annotations**: Injeção de dependências simplificada.
- **Retrofit & OKHttp**: Requisições HTTP para API de CEP.
- **Detekt**: Análise estática de código.
- **JUnit 4**: Testes unitários.
- **Espresso**: Testes instrumentados de UI.

---

## 📚 Estrutura do Projeto

- **`presentation`**: Contém as telas, temas e componentes de UI.
- **`viewmodel`**: Lida com o gerenciamento de estado das telas.
- **`data`**: Contém a lógica de acesso a dados, incluindo configurações do Room.
- **`utils`**: Ferramentas auxiliares para o desenvolvimento.
- **`di`**: Configurações de injeção de dependências usando Koin Annotations.
- **`test`**: Contém os testes unitários do projeto.
- **`androidTest`**: Contém os testes instrumentados do projeto.

---

## 📝 Funcionalidades principais

- **Buscar CEP**: Digite um CEP e obtenha dados de endereço válidos.
- **Validação de CEP**: Verifica CEP vazio ou inválido.
- **Tratamento de erros**: Diálogos de erro de rede e CEP inválido.
- **UI responsiva**: LazyColumn, Scroll e foco automático no campo CEP.
- **Arquitetura MVVM**: ViewModel com StateFlow e Koin para DI.

---

## 🚀 Como rodar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/marcelo-souza-1999/busca-cep-jetpack-compose.git
   ```
2. Abra no **Android Studio (Arctic Fox ou superior)**.
3. Garanta JDK 21 e Gradle configurados.
4. Execute em emulador ou dispositivo:
   ```bash
   ./gradlew installDebug
   ```

---

## 📸 Funcionalidades em Vídeo

<details>
<summary>📽️ Clique para visualizar o vídeo</summary>

https://github.com/user-attachments/assets/4e59a5b0-21f5-4046-ba14-6a07c4bfae95

</details>

---

## 📫 Contribuindo

1. Fork do repositório.
2. Crie uma branch:
   ```bash
   git checkout -b feature/minha-melhoria
   ```
3. Commit e push:
   ```bash
   git commit -m "Descrição da melhoria"
   git push origin feature/minha-melhoria
   ```
4. Abra Pull Request para revisão.

---

## 📧 Contato

- **Nome**: Marcelo Souza
- **Email**: [marcelocaregnatodesouza@gmail.com](mailto:marcelocaregnatodesouza@gmail.com)
- **LinkedIn**: [Clique aqui](https://www.linkedin.com/in/marcelosouza-1999/)
