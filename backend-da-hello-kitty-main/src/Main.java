import dao.ProdutoDao;
import model.Produto;

import javax.print.DocFlavor;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        ProdutoDao produtoDao = new ProdutoDao();

        try{
            Produto produto = new Produto(00001L, "Banana prata", 6.50, 10);

            // INSERT
            produtoDao.criar(produto);
            System.out.println("Criado: " + produto);

            // READ (select por id)
            produtoDao.buscarPorId(produto.getId()).ifPresent(encontrado ->
                    System.out.println("Encontrado: " + encontrado));

            // UPDATE
            produto.setPreco(5.12);
            produto.setQuantidade(20);
            System.out.println("Atualizado: " + produtoDao.atualizar(produto));

            // READ (select all)
            System.out.println("Produtos cadastrados: ");
            produtoDao.listarTodos().forEach(System.out::println);

            // DELETE
            System.out.println("Excluido: " + produtoDao.excluir(produto.getId()));

        } catch (SQLException e) {
            System.err.println("Não foi possivel acessar o MySQL.");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }

    }
}