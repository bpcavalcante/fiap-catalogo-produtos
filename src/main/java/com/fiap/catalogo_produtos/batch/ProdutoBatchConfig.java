package com.fiap.catalogo_produtos.batch;

import com.fiap.catalogo_produtos.model.Produto;
import com.fiap.catalogo_produtos.repository.ProdutoRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ProdutoBatchConfig {

    @Bean
    public FlatFileItemReader<Produto> reader() {
        return new FlatFileItemReaderBuilder<Produto>()
                .name("produtoItemReader")
                .resource(new ClassPathResource("produtos.csv"))
                .linesToSkip(1)  // Ignora a primeira linha (cabeçalhos)
                .delimited()
                .names("nome", "descricao", "preco", "quantidadeEstoque", "categoria", "fornecedor", "ativo")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Produto>() {{
                    setTargetType(Produto.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<Produto, Produto> processor() {
        return produto -> produto;  // Nenhum processamento específico por enquanto
    }

    @Bean
    public ItemWriter<Produto> writer(ProdutoRepository produtoRepository) {
        return new RepositoryItemWriterBuilder<Produto>()
                .repository(produtoRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     ItemReader<Produto> reader, ItemProcessor<Produto, Produto> processor,
                     ItemWriter<Produto> writer) {
        return new StepBuilder("produtoStep", jobRepository)
                .<Produto, Produto>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("produtoJob", jobRepository)
                .start(step)
                .build();
    }
}
