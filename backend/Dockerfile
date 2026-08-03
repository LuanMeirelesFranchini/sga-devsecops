# ==========================================
# STAGE 1: Build com Maven e Java 21 JDK
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copia dependências primeiro para cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia código fonte e gera o pacote jar
COPY src ./src
RUN mvn package -DskipTests -B

# ==========================================
# STAGE 2: Runtime Seguro (Hardened Container)
# ==========================================
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Boas práticas de Segurança em Contêineres (DevSecOps):
# 1. Não executar a aplicação como root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# 2. Copiar apenas o artefato necessário do builder
COPY --from=builder /build/target/sga-backend-1.0.0.jar app.jar

# 3. Alterar a propriedade dos arquivos para o usuário não-root
RUN chown -R appuser:appgroup /app

# 4. Mudar para o usuário seguro não-privilegiado
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
