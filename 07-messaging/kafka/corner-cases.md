# Kafka Corner Cases

## Offset Out of Range

Consuming from an offset that no longer exists in the log causes `OffsetOutOfRangeException`. Kafka retains logs based on `log.retention.hours` and `log.retention.bytes`. If the consumer is down longer than the retention period, offsets are lost.

Configure `auto.offset.reset` to `earliest` or `latest` to handle this. `earliest` reprocesses from the beginning of available data. `latest` skips to the end.

## Rebalance Storm

Frequent consumer group rebalances occur when consumers join or leave repeatedly. Each rebalance pauses all consumers in the group. Short session timeouts (`session.timeout.ms`) cause unnecessary rebalances if consumers are slow but not dead.

Use static group membership (`group.instance.id`) to avoid rebalances on temporary disconnections. Increase `max.poll.interval.ms` if processing takes longer than expected.

## Duplicate Delivery

At-least-once semantics mean duplicates are possible. Producers can send duplicate messages if retries occur. Consumers can reprocess messages if they commit offsets after processing fails.

Use idempotent producers (`enable.idempotence=true`) to prevent producer-side duplicates. Use consumer-side deduplication for end-to-end exactly-once guarantees.

## Under-Replicated Partitions

When a broker goes down, partitions may become under-replicated. `min.insync.replicas` determines how many replicas must acknowledge a write. If fewer replicas are available, the producer gets `NotEnoughReplicasException`.

Set `acks=all` with `min.insync.replicas=2` for durability. This means at least two replicas must confirm the write.

## Consumer Lag

Consumer lag is the difference between the latest offset and the consumer's committed offset. High lag indicates the consumer cannot keep up. Lag can grow if processing is slow or if the consumer crashes.

Monitor consumer lag with Kafka's built-in metrics. Use tools like Burrow or Kafka Lag Exporter for dashboards.

## Partition Leader Election

When the leader for a partition goes down, a new leader is elected from the in-sync replicas. This can take seconds to minutes depending on ISR size and broker configuration. During this time, the partition is unavailable for reads and writes.

`unclean.leader.election.enable=true` allows out-of-sync replicas to become leader. This increases availability but risks data loss.

## Message Size Limits

Messages exceeding `max.message.bytes` (broker) or `max.request.size` (producer) are rejected. The broker's limit should be greater than or equal to the producer's limit. Consumer `max.partition.fetch.bytes` must also accommodate the message size.

Large messages increase latency and memory usage. Consider compressing messages or splitting them.

## Transaction Timeout

Kafka transactions have a default timeout of 60 seconds (`transaction.timeout.ms`). If a transaction takes longer, it is aborted. This can happen with large batch processing.

Increase `transaction.timeout.ms` or break the transaction into smaller batches.

## Topic Deletion Issues

Deleting a topic while consumers are active causes errors. Consumers lose their offset tracking. Re-creating the topic with the same name does not restore old offsets.

Use `delete.topic.enable=true` on the broker. In production, prefer topic compaction or retention policies over deletion.

## Consumer Group Protocol Version

Different Kafka clients may use different protocol versions. Older clients may not support newer features like cooperative rebalancing. Ensure all clients in a consumer group use compatible versions.

Upgrade clients and brokers together. Test rebalancing behavior with mixed versions before deploying.
