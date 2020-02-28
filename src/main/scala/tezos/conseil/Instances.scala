package io.tokenanalyst.blockchainrpc.tezos.conseil

import cats.effect.IO
import io.circe.generic.auto._
import io.tokenanalyst.blockchainrpc.Codecs._
import io.tokenanalyst.blockchainrpc.TezosMethods._
import io.tokenanalyst.blockchainrpc.BasicMethods._
import io.tokenanalyst.blockchainrpc.tezos.conseil.Codecs._
import io.tokenanalyst.blockchainrpc.tezos.conseil.Protocol._
import io.tokenanalyst.blockchainrpc.{BatchResponse, Tezos}
import org.http4s.{Header, Headers}

object Instances {
  implicit val getNextBlockHashInstance =
    new GetNextBlockHash[Tezos] {
      override def getNextBlockHash(a: Tezos): IO[String] =
        a.client.nextBlockHash()
    }

  implicit val getTransactionInstance =
    new GetTransaction[Tezos, TransactionResponse] {
      override def getTransaction(
          tezos: Tezos,
          hash: String
      ): IO[TransactionResponse] = {
        for {
          res <- tezos.client.post[TransactionRequest, TransactionResponse](
            TransactionRequest(hash),
            Some("/v2/data/tezos/mainnet/operations"),
            Headers.of(Header("apiKey", tezos.apiKey))
          )
        } yield res
      }
    }

  implicit val getTransactionsInstance =
    new GetTransactions[Tezos, BatchResponse[TransactionResponse]] {
      override def getTransactions(
          tezos: Tezos,
          hashes: Seq[String]
      ): IO[BatchResponse[TransactionResponse]] =
        for {
          res <- tezos.client
            .post[TransactionsRequest, BatchResponse[
              TransactionResponse
            ]](
              TransactionsRequest(hashes),
              Some("/v2/data/tezos/mainnet/operations"),
              Headers.of(Header("apiKey", tezos.apiKey))
            )
        } yield res
    }

  implicit val getTransactionsByBlockHeight =
    new GetTransactionsByBlockHeight[Tezos, BatchResponse[TransactionResponse]] {
      override def getTransactions(
          tezos: Tezos,
          blockLevel: Long
      ): IO[BatchResponse[TransactionResponse]] =
        for {
          res <- tezos.client
            .post[BlockTransactionsRequest, BatchResponse[
              TransactionResponse
            ]](
              BlockTransactionsRequest(blockLevel),
              Some("/v2/data/tezos/mainnet/operations"),
              Headers.of(Header("apiKey", tezos.apiKey))
            )
        } yield res
    }

  implicit val getBlockHashInstance = new GetBlockHash[Tezos] {
    override def getBlockHash(a: Tezos, height: Long): IO[String] =
      for {
        json <- a.client
          .postJson[BlockHashRequest](BlockHashRequest(height))
      } yield json.asObject.get("result").get.asString.get
  }

  implicit val getBestBlockHashInstance = new GetBestBlockHash[Tezos] {
    override def getBestBlockHash(tezos: Tezos): IO[String] =
      for {
        json <- tezos.client
          .getJson[BestBlockLevelRequest](
            new BestBlockLevelRequest, Some("/v2/data/tezos/mainnet/blocks/head"),
            Headers.of(Header("apiKey", tezos.apiKey))
          )
      } yield json.asObject.get("hash").get.asString.get
  }

  implicit val getBestBlockHeightInstance = new GetBestBlockHeight[Tezos] {
    override def getBestBlockHeight(tezos: Tezos): IO[Long] =
      for {
        json <- tezos.client
          .getJson[BestBlockLevelRequest](
            new BestBlockLevelRequest,
            Some("/v2/data/tezos/mainnet/blocks/head"),
            Headers.of(Header("apiKey", tezos.apiKey))
          )
      } yield json.asObject.get("level").get.asNumber.get.toLong.get
  }

  implicit val getBlockByHashInstance =
    new GetBlockByHash[Tezos, BlockResponse] {
      override def getBlockByHash(
          tezos: Tezos,
          hash: String
      ): IO[BlockResponse] = {
        tezos.client.get[BlockRequest, BlockResponse](
          BlockRequest(),
          Some(s"/v2/data/tezos/mainnet/blocks/$hash"),
          Headers.of(Header("apiKey", tezos.apiKey))
        )
      }
    }

  implicit val getBlockByHeightInstance =
    new GetBlockByHeight[Tezos, BlockResponse] {
      override def getBlockByHeight(
          tezos: Tezos,
          height: Long
      ): IO[BlockResponse] =
        for {
          hash <- getBlockHashInstance.getBlockHash(tezos, height)
          data <- getBlockByHashInstance.getBlockByHash(tezos, hash)
        } yield data
    }
}
