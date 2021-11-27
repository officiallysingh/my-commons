package com.clearcaptions.ccwsv3.common.boot.config.aws;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.COUCHBASE_CONFIGURATION_BEAN_NAME;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.convert.CouchbaseCustomConversions;

import com.clearcaptions.ccwsv3.common.boot.converter.OffsetDateTimeToStringConverter;
import com.clearcaptions.ccwsv3.common.boot.converter.StringToOffsetDateTimeConverter;;
/**
 * Overriding Spring's default couchbase configurations
 * to point to ClearCaptions couchbase database. Namespaces
 * have been defined as environmental variables.
 */
@Configuration(value = COUCHBASE_CONFIGURATION_BEAN_NAME)
@EnableConfigurationProperties(value = { CouchbaseProperties.class })
@ConditionalOnProperty(prefix = "spring.couchbase", name = "connection-string")
@ConditionalOnClass(CouchbaseClientFactory.class)
@ConditionalOnMissingBean(name = COUCHBASE_CONFIGURATION_BEAN_NAME)
public class CouchbaseAutoConfiguration extends AbstractCouchbaseConfiguration {
	
	private final CouchbaseProperties properties;
	
	private final String bucketName;
	
	public CouchbaseAutoConfiguration(final CouchbaseProperties properties, 
			@Value("${spring.data.couchbase.bucket-name}") 
				final String bucketName) {
		this.properties = properties;
		this.bucketName = bucketName;
	}
	
    @Override
    public String getConnectionString() {
        return this.properties.getConnectionString();
    }

    @Override
    public String getBucketName() {
        return this.bucketName;
    }

    @Override
    public String getUserName() {
        return this.properties.getUsername();
    }

    @Override
    public String getPassword() {
        return this.properties.getPassword();
    }

    @Override
    public CouchbaseCustomConversions customConversions() {
        return new CouchbaseCustomConversions(Arrays.asList(OffsetDateTimeToStringConverter.INSTANCE,
        		StringToOffsetDateTimeConverter.INSTANCE));
    }

}