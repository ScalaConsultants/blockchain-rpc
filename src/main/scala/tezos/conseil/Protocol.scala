package io.tokenanalyst.blockchainrpc.tezos.conseil

import io.tokenanalyst.blockchainrpc.{RPCRequest, RPCResponse}

object Protocol {

  object QueryApi {
    final case class Query(predicates: List[Predicate], orderBy: Option[OrderBy] = None, limit: Option[Int] = None)

    sealed trait Predicate
    final case class LongPredicate(field: String, operation: String, set: Seq[Long], inverse: Boolean) extends Predicate
    final case class StringPredicate(field: String, operation: String, set: Seq[String], inverse: Boolean) extends Predicate

    final case class OrderPredicate(field: String, direction: String)
    final case class OrderBy(orderBy: Seq[OrderPredicate])
  }


  final case class TransactionResponse(
      secret: Option[String],
      storage_size: Option[Long],
      number_of_slots: Option[Int],
      internal: Boolean,
      delegatable: Option[Boolean],
      source: Option[String],
      consumed_gas: Long,
      timestamp: Long,
      pkh: Option[String],
      nonce: Option[String],
      block_level: Long,
      counter: Option[Long],
      branch: Option[String],
      balance: Option[Long],
      operation_group_hash: Option[String],
      public_key: Option[String],
      paid_storage_size_diff: Option[Long],
      delegate: Option[String],
      proposal: Option[String],
      block_hash: Option[String],
      amount: Option[Long],
      gas_limit: Option[Long],
      spendable: Option[Boolean],
      cycle: Option[Long],
      status: String,
      operation_id: Option[Long],
      manager_pubkey: Option[String],
      slots: Option[Long],
      storage_limit: Option[Long],
      storage: Option[String],
      script: Option[String],
      kind: String,
      originated_contracts: Option[String],
      parameters: Option[String],
      destination: Option[String],
      ballot: Option[String],
      period: Option[String],
      fee: Option[Long],
      level: Option[Long]
   ) extends RPCResponse

  case class BlockResponse(
      level: Long,
      proto: Int,
      predecessor: Option[String],
      timestamp: Long,
      validationPass: Int,
      fitness: String,
      context: String,
      signature: String,
      protocol: String,
      chainId: String,
      hash: String,
      operationsHash: String,
      periodKind: String,
      currentExpectedQuorum: Int,
      activeProposal: Option[String],
      baker: String,
      consumedGas: Int,
      metaLevel: Long,
      metaLevelPosition: Long,
      metaCycle: Int,
      metaCyclePosition: Int,
      metaVotingPeriod: Int,
      metaVotingPeriodPosition: Int,
      expectedCommitment: Boolean,
      priority: Int
  ) extends RPCResponse

  case class ExtendedBlockResponse(
      level: Long,
      proto: Int,
      predecessor: Option[String],
      timestamp: Long,
      validationPass: Int,
      fitness: String,
      context: String,
      signature: String,
      protocol: String,
      chainId: String,
      hash: String,
      operationsHash: String,
      periodKind: String,
      currentExpectedQuorum: Int,
      activeProposal: Option[String],
      baker: String,
      consumedGas: Int,
      metaLevel: Long,
      metaLevelPosition: Long,
      metaCycle: Int,
      metaCyclePosition: Int,
      metaVotingPeriod: Int,
      metaVotingPeriodPosition: Int,
      expectedCommitment: Boolean,
      priority: Int
  ) extends RPCResponse

  case class TransactionsRequest(hashes: Seq[String]) extends RPCRequest
  case class BlockTransactionsRequest(blockLevel: Long) extends RPCRequest
  case class TransactionRequest(hash: String) extends RPCRequest
  case class BestBlockLevelRequest() extends RPCRequest
  case class BlockRequest() extends RPCRequest
  case class BlockByLevelRequest(level: Long) extends RPCRequest
  case class BlockHashRequest(height: Long) extends RPCRequest

}
