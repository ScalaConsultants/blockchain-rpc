package io.tokenanalyst.blockchainrpc.tezos.conseil

import io.circe.{HCursor, Json}
import io.tokenanalyst.blockchainrpc.tezos.conseil.Protocol.QueryApi.{LongPredicate, OrderBy, OrderPredicate, Predicate, StringPredicate}
import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.auto._
import io.circe.generic.extras.semiauto._
import io.circe.generic.extras._, io.circe.syntax._
import io.tokenanalyst.blockchainrpc.RPCEncoder
import io.tokenanalyst.blockchainrpc.tezos.conseil.Protocol._

object Codecs {

  implicit def derivedBlockDecoder[A <: ExtendedBlockResponse: Decoder] = new Decoder[A] {
    def apply(a: HCursor): Decoder.Result[A] = {
      a.downField("block").as[A]
    }
  }

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
  implicit val derivedBlockDecoderSnakeCase: Decoder[BlockResponse] = deriveConfiguredDecoder[BlockResponse]

  implicit def derivedTransactionDecoder[A <: TransactionResponse: Decoder] = new Decoder[A] {
    def apply(a: HCursor): Decoder.Result[A] = a.as[A]
  }

  implicit val queryEncoder: Encoder[QueryApi.Query] = deriveConfiguredEncoder[QueryApi.Query]
  implicit val predicateEncoder: Encoder[Predicate] = Encoder.instance {
    case stringPredicate @ StringPredicate(_, _, _, _) => stringPredicate.asJson
    case longPredicate @ LongPredicate(_, _, _, _) => longPredicate.asJson
  }

  implicit val encodeFoo: Encoder[OrderBy] = new Encoder[OrderBy] {
    final def apply(orderBy: OrderBy): Json = Json.arr(
      orderBy.orderBy.map(_.asJson):_*
    )
  }

  implicit val blockTransactionsRequest =
    new RPCEncoder[BlockTransactionsRequest] {
      final def apply(a: BlockTransactionsRequest): Json = {
        QueryApi.Query(
          predicates = List(
            StringPredicate("kind", "eq", Seq("transaction"), false),
            LongPredicate("block_level", "eq", Seq(a.blockLevel), false),
            StringPredicate("status", "eq", Seq("applied"), false)
          ),
          orderBy = Some(OrderBy(Seq(OrderPredicate("block_level", "desc"))))).asJson
      }
    }

  implicit val blockbyLevelRequest =
    new RPCEncoder[BlockByLevelRequest] {
      final def apply(a: BlockByLevelRequest): Json = {
        QueryApi.Query(
          predicates = List(
            LongPredicate("level", "eq", Seq(a.level), false)
          )).asJson
      }
    }

  implicit val blockHashRequest = new RPCEncoder[BlockHashRequest] {
    final def apply(a: BlockHashRequest): Json =
      Json.obj()
  }

  implicit val getTransactionRequest = new RPCEncoder[TransactionRequest] {
    final def apply(a: TransactionRequest): Json =
      Json.obj()
  }

  implicit val bestBlockLevelRequest = new RPCEncoder[BestBlockLevelRequest] {
    final def apply(a: BestBlockLevelRequest): Json =
      Json.obj()
  }

  implicit val blockRequest = new RPCEncoder[BlockRequest] {
    final def apply(a: BlockRequest): Json =
      Json.obj()
  }
}
