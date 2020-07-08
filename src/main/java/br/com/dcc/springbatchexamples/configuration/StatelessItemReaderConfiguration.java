package br.com.dcc.springbatchexamples.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.dcc.springbatchexamples.listener.SimpleChunkListener;
import br.com.dcc.springbatchexamples.reader.StatelessItemReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StatelessItemReaderConfiguration {

	@Bean
	public StatelessItemReader statelessItemReader() {
		List<String> data = new ArrayList<>();
		data.add("First");
		data.add("Second");
		data.add("Third");
		return new StatelessItemReader(data);
	}

	@Bean
	public Step statelessItemReaderStep1(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("statelessItemReaderStep1")
				.<String, String>chunk(2)
				.faultTolerant()
				.listener(new SimpleChunkListener())
				.reader(statelessItemReader())
				.writer(list -> {
					for (String curItem : list) {
						log.info("Current item is: {}", curItem);
					}
				})
				.build();
	}

	@Bean
	public Job statelessItemReaderJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		return jobBuilderFactory.get("StatelessItemReaderJob")
				.start(statelessItemReaderStep1(stepBuilderFactory))
				.build();

	}

}
