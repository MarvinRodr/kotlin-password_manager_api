package com.marvinrodr.common.infrastructure

import com.marvinrodr.common.domain.DomainEvent
import com.marvinrodr.common.domain.Publisher

class InMemoryPublisher : Publisher {
    private val events = mutableListOf<DomainEvent>()
    override fun publish(events: List<DomainEvent>) {
        this.events += events
    }

    override fun get(): List<DomainEvent> {
        return events
    }

    override fun flush() {
        events.clear()
    }
}