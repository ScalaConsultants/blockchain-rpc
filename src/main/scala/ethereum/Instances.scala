/**
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
  * this work for additional information regarding copyright ownership.
  * The ASF licenses this file to You under the Apache License, Version 2.0
  * (the "License"); you may not use this file except in compliance with
  * the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package io.tokenanalyst.blockchainrpc.ethereum

import cats.effect.IO
import io.circe.generic.auto._
import io.tokenanalyst.blockchainrpc.BasicMethods._
import io.tokenanalyst.blockchainrpc.Codecs._
import io.tokenanalyst.blockchainrpc.ethereum.Codecs._
import io.tokenanalyst.blockchainrpc.ethereum.Methods._
import io.tokenanalyst.blockchainrpc.ethereum.Protocol._
import io.tokenanalyst.blockchainrpc.{BatchRequest, BatchResponse, Ethereum}

object Instances {

  implicit val getReceiptInstance =
    new GetReceipt[Ethereum, ReceiptResponse] {
      override def getReceipt(
          a: Ethereum,
          hash: String
      ): IO[ReceiptResponse] = {
        a.client.post[ReceiptRequest, ReceiptResponse](
          ReceiptRequest(hash)
        )
      }
    }

  implicit val getReceiptsInstance =
    new GetReceipts[Ethereum, BatchResponse[ReceiptResponse]] {
      override def getReceipts(
          a: Ethereum,
          hashes: Seq[String]
      ): IO[BatchResponse[ReceiptResponse]] = {
        a.client.post[
          BatchRequest[ReceiptRequest],
          BatchResponse[ReceiptResponse]
        ](
          BatchRequest[ReceiptRequest](hashes.map(ReceiptRequest.apply))
        )
      }
    }

  implicit val getBlockWithTransactionsByHashInstance =
    new GetBlockByHash[Ethereum, BlockWithTransactionsResponse] {
      override def getBlockByHash(
          a: Ethereum,
          hash: String
      ): IO[BlockWithTransactionsResponse] = {
        a.client.post[BlockByHashRequest, BlockWithTransactionsResponse](
          BlockByHashRequest(hash, true)
        )
      }
    }

  implicit val getBlockByHashInstance =
    new GetBlockByHash[Ethereum, BlockResponse] {
      override def getBlockByHash(
          a: Ethereum,
          hash: String
      ): IO[BlockResponse] = {
        a.client.post[BlockByHashRequest, BlockResponse](
          BlockByHashRequest(hash, false)
        )
      }
    }

  implicit val getBlockWithTransactionsByHeightInstance =
    new GetBlockByHeight[Ethereum, BlockWithTransactionsResponse] {
      override def getBlockByHeight(
          a: Ethereum,
          height: Long
      ): IO[BlockWithTransactionsResponse] =
        a.client.post[BlockByHeightRequest, BlockWithTransactionsResponse](
          BlockByHeightRequest(height, true)
        )
    }

  implicit val getBlockByHeightInstance =
    new GetBlockByHeight[Ethereum, BlockResponse] {
      override def getBlockByHeight(
          a: Ethereum,
          height: Long
      ): IO[BlockResponse] =
        a.client.post[BlockByHeightRequest, BlockResponse](
          BlockByHeightRequest(height, false)
        )
    }

  implicit val getBestBlockHeightInstance =
    new GetBestBlockHeight[Ethereum] {
      override def getBestBlockHeight(a: Ethereum): IO[Long] =
        for {
          json <- a.client
            .postJson[BestBlockHeightRequest](new BestBlockHeightRequest)
        } yield HexTools
          .parseQuantity(json.asObject.get("result").get.asString.get)
          .toLong
    }

  implicit val getTransactionsInstance =
    new GetTransactions[Ethereum, BatchResponse[
      TransactionResponse
    ]] {
      override def getTransactions(
          a: Ethereum,
          hashes: Seq[String]
      ): IO[BatchResponse[TransactionResponse]] =
        a.client.post[
          BatchRequest[TransactionRequest],
          BatchResponse[TransactionResponse]
        ](
          BatchRequest[TransactionRequest](hashes.map(TransactionRequest.apply))
        )
    }

  implicit val getTransactionInstance =
    new GetTransaction[Ethereum, TransactionResponse] {
      override def getTransaction(
          a: Ethereum,
          hash: String
      ): IO[TransactionResponse] =
        a.client.post[TransactionRequest, TransactionResponse](
          TransactionRequest(hash)
        )
    }
}
