package dao;

import com.mysql.cj.protocol.Resultset;
import config.ConnectionFactory;
import model.Produto;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDao implements CrudDao<Produto, Long> {

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (
                Connection conexao = ConnectionFactory.abrirConexao();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setLong(1, id);
            return comando.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, preco, quantidade FROM produtos WHERE id = ?";

        try (
                Connection conexao = ConnectionFactory.abrirConexao();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setLong(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        }
    }

    @Override
    public boolean atualizar(Produto produto) throws SQLException {
        if (produto.getId() == null) {
            throw new IllegalArgumentException("O produto precisa ter um id para ser alterado");
        }

        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";

        try (
                Connection conexao = ConnectionFactory.abrirConexao();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, produto.getNome());
            comando.setDouble(2, produto.getPreco());
            comando.setInt(3, produto.getQuantidade());
            comando.setLong(4, produto.getId());
            return comando.executeUpdate() > 0;
        }
    }

    @Override
    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM produto ORDER BY id";

        List<Produto> produtoLista = new ArrayList<>();

        try(
                Connection conexao = ConnectionFactory.abrirConexao();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet resultado = comando.executeQuery()
        ) {
            while(resultado.next()){
                produtoLista.add(mapear(resultado));
            }

        }
        return produtoLista;
    }


    private Produto mapear(ResultSet resultado) throws SQLException{
        return new Produto(
                resultado.getLong("id"),
                resultado.getString("nome"),
                resultado.getDouble("preco"),
                resultado.getInt("quantidade")
        );
    }
}
