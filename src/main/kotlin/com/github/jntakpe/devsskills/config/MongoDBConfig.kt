package com.github.jntakpe.devsskills.config

import com.mongodb.ConnectionString
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.async.client.MongoClientSettings
import com.mongodb.connection.ClusterSettings
import com.mongodb.connection.SslSettings
import com.mongodb.connection.netty.NettyStreamFactoryFactory
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration

@Configuration
@EnableConfigurationProperties(MongoProperties::class)
class MongoDBConfig(val mongoProperties: MongoProperties, val profileConfig: ProfileConfig) : AbstractReactiveMongoConfiguration() {

    override fun mongoClient(): MongoClient? {
        return if (profileConfig.isActive(Profiles.CLOUD)) MongoClients.create(settings())
        else if (profileConfig.isActive(Profiles.DOCKER)) MongoClients.create(ConnectionString("mongodb://${mongoProperties.host}"))
        else MongoClients.create()
    }

    override fun getDatabaseName() = mongoProperties.database

    private fun settings() = MongoClientSettings.builder()
            .sslSettings(SslSettings.builder()
                    .enabled(true)
                    .build())
            .clusterSettings(clusterSettings())
            .credentialList(listOf(mongoCredentials()))
            .streamFactoryFactory(NettyStreamFactoryFactory.builder().build())
            .build()

    private fun clusterSettings() = ClusterSettings.builder()
            .hosts(listOf(ServerAddress(mongoProperties.host, mongoProperties.port)))
            .build()

    private fun mongoCredentials() = MongoCredential.createMongoCRCredential(
            mongoProperties.username,
            mongoProperties.database,
            mongoProperties.password
    )

}