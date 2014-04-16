/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedro
 */
public class RelatorioRE {
    
    public List<Relatorio> getRelatorios(){
        
        String consulta;
        

                consulta = "SELECT p.idproduto, p.nomeproduto, e.loteentrada, "
                + "e.datavalidade, sum(quantidadetotalentrada), sum(quantidadetotalsaida) " 
                + "FROM entradas as e, produto as p " 
                + "WHERE e.statusproduto = 1 " 
                + "AND produtoentrada_idproduto = p.idproduto " 
                + "GROUP BY p.idproduto,e.loteentrada,e.datavalidade";
                
        System.out.println(consulta);
        
        List<Relatorio> relatorios = new ArrayList<Relatorio>();
        
        // Classe que vai recuperar os dados do banco de dados
        
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            
            pstm = conn.prepareStatement(consulta);

            rset = pstm.executeQuery();
            
            // Enquanto existir dados no banco de dados, faça
                
            while (rset.next()) {
                
                
                Relatorio relatorio = new Relatorio();
                relatorio.setNomeProduto(rset.getString(2)); // nome do produto
                relatorio.setNumeroLote(rset.getString(3));
                relatorio.setDataValidade(rset.getDate(4));
                relatorio.setQuantidade(rset.getLong(5) - rset.getLong(6)); // tabela 3 quantidadetotal 4 quantidade saida
                System.out.println(relatorio.getQuantidade());
                relatorios.add(relatorio);     
            }
            
        }catch (Exception e) {

            e.printStackTrace();
            
        }finally {

            try {

                if (rset != null) {

                    rset.close();
                }

                if (pstm != null) {

                    pstm.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return relatorios;
    }
    
}
