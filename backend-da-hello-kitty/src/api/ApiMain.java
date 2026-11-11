package api;

import dao.ProdutoDao;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;

public class ApiMain {
    public static void main(String[] args){
        ProdutoDao produtoDao = new ProdutoDao();

        int porta = Integer.parseInt(System.getenv().getOrDefault("API_PORT", "7070"));

        Javalin app = Javalin.create(config-> {
            config.routes.get("/", ctx->ctx.json(Map.of("aplicacao", "Aula Exemplo API", "status", "online")));

            config.routes.get("/api/produtos", ctx->executar(ctx, ()->ctx.json(produtoDao.listarTodos())));
        }).start(porta);

        System.out.println("API disponivel em http://localhost:" + app.port())
    }
    private static void executar(Context ctx, AcaoHttp acao){
        try{
            acao.executar();
        } catch (SecurityException e){
            ctx.status(401).json(Map.of("erro", e.getMessage()));
        } catch (NoSuchElementException e){
            ctx.status(404).json(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e){
            ctx.status(409).json(Map.of("erro", e.getMessage()));
        } catch (IllegalArgumentException e){
            ctx.status(400).json(Map.of("erro", e.getMessage()));
        } catch (SQLException e){
            ctx.status(500).json(Map.of("erro", "Falha ao acessar o banco de dados!"));
            e.printStackTrace();
        } catch (Exception e){
            ctx.status(500).json(Map.of("erro", "Erro interno na API"));
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    private interface AcaoHttp{
        void executar() throws Exception;
    }
}


