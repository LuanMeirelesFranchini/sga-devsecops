# SGA - Sistema de Gerenciamento de Atendimento (DevSecOps & Cloud Security)

Projeto prático desenvolvido para aprendizado de **Cloud Security**, **Docker Hardening**, **Spring Boot 3 (Java 21)** e **DevSecOps**.

---

## 🚀 Como Executar no seu Ubuntu Server 24.04 (VirtualBox)

Existem duas formas fáceis de rodar na sua VM:

### Opção 1: Via Linha de Comando na VM (SSH / Terminal)
1. Clone este repositório na sua máquina Ubuntu ou copie a pasta:
   ```bash
   git clone <seu-repo>
   cd SGA
   ```
2. Execute o comando do Docker Compose para subir todo o ambiente:
   ```bash
   docker compose up -d --build
   ```

### Opção 2: Via Portainer (Interface Web `http://192.168.0.171:9000`)
1. No Portainer, vá em **Stacks** -> **Add Stack**.
2. Nomeie como `sga-stack`.
3. Escolha **Repository** (se estiver no GitHub) ou cole o conteúdo do `docker-compose.yml` na aba **Web editor**.
4. Clique em **Deploy the stack**.

---

## 🌐 Como Acessar as Interfaces

Substitua `SEU_IP_UBUNTU` pelo IP real da sua máquina (ex: `192.168.0.171` ou `localhost`).

### 📱 Sistema Principal (Porta 80)
- 🏠 **Página Inicial:** `http://SEU_IP_UBUNTU/`
- 🎟️ **Totem de Emitir Senha (Kiosk):** `http://SEU_IP_UBUNTU/kiosk.html`
- 📺 **Painel Chamador de TV:** `http://SEU_IP_UBUNTU/painel.html`
- 🎧 **Painel do Atendente:** `http://SEU_IP_UBUNTU/atendente.html`
- 📈 **Relatórios:** `http://SEU_IP_UBUNTU/relatorios.html`
- ⚙️ **Administração:** `http://SEU_IP_UBUNTU/admin.html`

### 📊 Observabilidade e Segurança
- 📉 **Grafana (Dashboards Visuais):** `http://SEU_IP_UBUNTU:3000` (Usuário/Senha: `admin`)
- 🤖 **Prometheus (Métricas Brutas):** `http://SEU_IP_UBUNTU:9090`
- 🗄️ **PostgreSQL (Acesso Direto ao Banco):** Porta `5432`

---

## 🔒 Destaques de Segurança & DevSecOps Aplicados

1. **Hardening de Contêiner Backend:**
   - Multi-stage build com Maven 3.9 + Java 21 JDK -> Alpine JRE 21.
   - Aplicação executa com usuário sem privilégios `appuser` (UID não-root).
   - Healthcheck integrado para orquestração segura.

2. **Nginx Security Headers & Reverse Proxy:**
   - `X-Frame-Options`, `X-XSS-Protection`, `X-Content-Type-Options` ativados no `nginx.conf`.
   - Proxy reverso interno na rede Docker `sga-network` (a API Spring Boot não precisa expor a porta 8080 publicamente para o mundo externo).

3. **Arquitetura de Banco de Dados Protegida:**
   - PostgreSQL 16 rodando em rede privada e isolada do Docker.
