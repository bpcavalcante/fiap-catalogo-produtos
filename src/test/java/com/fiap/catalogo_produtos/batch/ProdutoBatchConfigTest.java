package com.fiap.catalogo_produtos.batch;

import com.fiap.catalogo_produtos.model.Produto;
import com.fiap.catalogo_produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ProdutoBatchConfigTest {

    @InjectMocks
    private ProdutoBatchConfig produtoBatchConfig;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private JobRepository jobRepository;

    @BeforeEach
    void setUp() {
        produtoBatchConfig = new ProdutoBatchConfig();
    }

    @Test
    void testReader() throws Exception {
        FlatFileItemReader<Produto> reader = produtoBatchConfig.reader();

        assertNotNull(reader);
        assertEquals("produtoItemReader", reader.getName());

        reader.open(new ExecutionContext());

        Produto produto = reader.read();

        assertNotNull(produto, "O leitor não conseguiu processar os dados corretamente.");
    }

    @Test
    void testProcessor() throws Exception {
        Produto produto = new Produto();
        assertEquals(produto, produtoBatchConfig.processor().process(produto));
    }

    @Test
    void testWriter() {
        assertNotNull(produtoBatchConfig.writer(produtoRepository));
    }

    @Test
    void testStep() {
        Step step = produtoBatchConfig.step(jobRepository, transactionManager,
                produtoBatchConfig.reader(),
                produtoBatchConfig.processor(),
                produtoBatchConfig.writer(produtoRepository));

        assertNotNull(step);
        assertEquals("produtoStep", step.getName());
    }

    @Test
    void testJob() {
        Step step = mock(Step.class);
        Job job = produtoBatchConfig.job(jobRepository, step);

        assertNotNull(job);
        assertEquals("produtoJob", job.getName());
    }
}