/*
 * Copyright (c) 2010-2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.company.config;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.caching.Cache;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.samples.trader.company.command.Company;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyConfig {

    private static final int SNAPSHOT_THRESHOLD = 50;

    //TODO #28 this should become an overall snapshot trigger definition i.o. in the company config
    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, SNAPSHOT_THRESHOLD);
    }

    @Bean(name = "companyAggregateRepository")
    public Repository<Company> companyAggregateRepository(AggregateFactory<Company> companyAggregateFactory,
                                                          EventStore eventStore,
                                                          Cache cache,
                                                          SnapshotTriggerDefinition snapshotTriggerDefinition) {
        return new CachingEventSourcingRepository<>(
                companyAggregateFactory,
                eventStore,
                cache,
                snapshotTriggerDefinition);
    }
}
