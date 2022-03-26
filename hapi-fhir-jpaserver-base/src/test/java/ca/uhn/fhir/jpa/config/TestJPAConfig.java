package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.jpa.util.Batch2JobHelper;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@Import({
	SubscriptionSubmitterConfig.class,
	SubscriptionProcessorConfig.class,
	SubscriptionChannelConfig.class
})
public class TestJPAConfig {
	@Bean
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();

		if ("true".equals(System.getProperty("mass_ingestion_mode"))) {
			retVal.setMassIngestionMode(true);
		}

		return retVal;
	}

	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

	@Bean
	public ModelConfig modelConfig() {
		ModelConfig config = daoConfig().getModelConfig();
		return config;
	}

	/*
	Please do not rename this bean to "transactionManager()" as this will conflict with the transactionManager
	provided by Spring Batch.
	 */
	@Bean
	@Primary
	public JpaTransactionManager hapiTransactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

	@Lazy
	@Bean
	public SubscriptionTestUtil subscriptionTestUtil() {
		return new SubscriptionTestUtil();
	}

	@Bean
	@Primary
	public SubscriptionDeliveringRestHookSubscriber stoppableSubscriptionDeliveringRestHookSubscriber() {
		return new StoppableSubscriptionDeliveringRestHookSubscriber();
	}

	@Bean
	public BatchJobHelper batchJobHelper(JobExplorer theJobExplorer) {
		return new BatchJobHelper(theJobExplorer);
	}

	@Bean
	public Batch2JobHelper batch2JobHelper() {
		return new Batch2JobHelper();
	}

	@Bean
	@Lazy
	public IBinaryStorageSvc binaryStorage() {
		return new MemoryBinaryStorageSvcImpl();
	}
}
