package com.fiap.catalogo_produtos.controller;

import com.fiap.catalogo_produtos.model.Produto;
import com.fiap.catalogo_produtos.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarProdutos_DeveRetornarListaDeProdutos() {
        List<Produto> produtosMock = Arrays.asList(
                new Produto(1L, "Produto A", "Descrição A", BigDecimal.valueOf(10.0), 5, "Categoria A", "Fornecedor A", true),
                new Produto(2L, "Produto B", "Descrição B", BigDecimal.valueOf(10.0), 3, "Categoria B", "Fornecedor B", true)
        );
        when(produtoService.listarProdutos()).thenReturn(produtosMock);

        List<Produto> resultado = produtoController.listarProdutos();

        assertEquals(2, resultado.size());
        verify(produtoService, times(1)).listarProdutos();
    }

    @Test
    void buscarProduto_QuandoExiste_DeveRetornarProduto() {
        Produto produtoMock = new Produto(1L, "Produto A", "Descrição A", BigDecimal.valueOf(10.0), 5, "Categoria A", "Fornecedor A", true);
        when(produtoService.buscarProdutoPorId(1L)).thenReturn(Optional.of(produtoMock));

        ResponseEntity<Produto> response = produtoController.buscarProduto(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(produtoMock, response.getBody());
        verify(produtoService, times(1)).buscarProdutoPorId(1L);
    }

    @Test
    void buscarProduto_QuandoNaoExiste_DeveRetornarNotFound() {
        when(produtoService.buscarProdutoPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<Produto> response = produtoController.buscarProduto(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(produtoService, times(1)).buscarProdutoPorId(99L);
    }

    @Test
    void salvarProduto_DeveRetornarProdutoCriado() {
        Produto produtoNovo = new Produto(null, "Produto Novo", "Nova Descrição", BigDecimal.valueOf(10.0), 10, "Nova Categoria", "Novo Fornecedor", true);
        Produto produtoSalvo = new Produto(1L, "Produto Novo", "Nova Descrição", BigDecimal.valueOf(10.0), 10, "Nova Categoria", "Novo Fornecedor", true);

        when(produtoService.salvarProduto(produtoNovo)).thenReturn(produtoSalvo);

        ResponseEntity<Produto> response = produtoController.salvarProduto(produtoNovo);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(produtoSalvo, response.getBody());
        verify(produtoService, times(1)).salvarProduto(produtoNovo);
    }

    @Test
    void deletarProduto_DeveRetornarNoContent() {
        doNothing().when(produtoService).deletarProduto(1L);

        ResponseEntity<Void> response = produtoController.deletarProduto(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(produtoService, times(1)).deletarProduto(1L);
    }

    @Test
    void importarProdutos_ComSucesso_DeveRetornarOk() throws Exception {
        doNothing().when(produtoService).importarProdutos();

        ResponseEntity<String> response = produtoController.importarProdutos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Importação iniciada com sucesso.", response.getBody());
        verify(produtoService, times(1)).importarProdutos();
    }

    @Test
    void importarProdutos_ComErro_DeveRetornarInternalServerError() throws Exception {
        doThrow(new RuntimeException("Erro de importação")).when(produtoService).importarProdutos();

        ResponseEntity<String> response = produtoController.importarProdutos();

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erro ao iniciar importação.", response.getBody());
        verify(produtoService, times(1)).importarProdutos();
    }

}