package io.tokenanalyst.blockchainrpc.tezos.conseil

import io.tokenanalyst.blockchainrpc.BasicMethods._
import io.tokenanalyst.blockchainrpc.TezosMethods.GetTransactionsByBlockHeight
import io.tokenanalyst.blockchainrpc.tezos.conseil.Protocol._
import io.tokenanalyst.blockchainrpc.tezos.conseil.Instances._
import io.tokenanalyst.blockchainrpc.{BatchResponse, Tezos}

object Syntax {
  implicit class TezosOps(tezos: Tezos) {

    def getTransaction(hash: String) =
      implicitly[GetTransaction[Tezos, TransactionResponse]].getTransaction(tezos, hash)

    def getTransactions(hashes: Seq[String]) =
      implicitly[GetTransactions[Tezos, BatchResponse[TransactionResponse]]]
      .getTransactions(tezos, hashes)

    def getTransactions(blockLevel: Long) =
      implicitly[GetTransactionsByBlockHeight[Tezos, BatchResponse[TransactionResponse]]]
        .getTransactions(tezos, blockLevel)

    def getNextBlockHash() =
      implicitly[GetNextBlockHash[Tezos]].getNextBlockHash(tezos)

    def getBlockByHeight(height: Long) =
      implicitly[GetBlockByHeight[Tezos, BlockResponse]]
        .getBlockByHeight(tezos, height)

    def getBestBlockHash() =
      implicitly[GetBestBlockHash[Tezos]].getBestBlockHash(tezos)

    def getBestBlockHeight() =
      implicitly[GetBestBlockHeight[Tezos]].getBestBlockHeight(tezos)

    def getBlockByHash(hash: String) =
      implicitly[GetBlockByHash[Tezos, BlockResponse]].getBlockByHash(tezos, hash)
  }
}
