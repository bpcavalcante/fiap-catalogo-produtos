package com.fiap.catalogo_produtos.service;

import com.fiap.catalogo_produtos.model.Produto;
import com.fiap.catalogo_produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("100.0"));
    }

    @Test
    void listarProdutosTest() {
        List<Produto> produtos = List.of(produto);
        when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> result = produtoService.listarProdutos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Produto Teste", result.get(0).getNome());
        assertEquals(new BigDecimal("100.0"), result.get(0).getPreco());  // Verificando BigDecimal
    }

    @Test
    void buscarProdutoPorIdTest() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> result = produtoService.buscarProdutoPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("Produto Teste", result.get().getNome());
        assertEquals(new BigDecimal("100.0"), result.get().getPreco());
    }

    @Test
    void salvarProdutoTest() {
        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto result = produtoService.salvarProduto(produto);

        assertNotNull(result);
        assertEquals("Produto Teste", result.getNome());
        assertEquals(new BigDecimal("100.0"), result.getPreco());
    }

    @Test
    void deletarProdutoTest() {
        doNothing().when(produtoRepository).deleteById(1L);

        produtoService.deletarProduto(1L);

        verify(produtoRepository, times(1)).deleteById(1L);
    }

}
