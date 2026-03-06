package bss.core.page

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactory
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import java.io.Serializable

class PageManagerRepositoryFactoryBean<T : Repository<S, ID>?, S, ID : Serializable?>
    (repositoryInterface: Class<out T>?) : ReactiveMongoRepositoryFactoryBean<T, S, ID>(repositoryInterface!!) {

    override fun getFactoryInstance(operations: ReactiveMongoOperations): RepositoryFactorySupport {
        return PageManagerRepositoryFactory(operations)
    }
}

class PageManagerRepositoryFactory(operations: ReactiveMongoOperations) :
    ReactiveMongoRepositoryFactory(operations) {

    private lateinit var beanFactory: AutowireCapableBeanFactory

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return PageReactiveMongoRepositoryImpl::class.java
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
        super.setBeanFactory(beanFactory)
        this.beanFactory = beanFactory as AutowireCapableBeanFactory
    }

    override fun getTargetRepository(information: RepositoryInformation): Any {
        val targetRepository = super.getTargetRepository(information)
        beanFactory.autowireBean(targetRepository)
        return targetRepository
    }
}