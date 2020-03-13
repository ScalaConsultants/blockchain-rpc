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
package examples.tezos

import cats.effect.{ExitCode, IO, IOApp}
import io.tokenanalyst.blockchainrpc.tezos.conseil.Syntax._
import io.tokenanalyst.blockchainrpc.{Config, RPCClient, Tezos}

import scala.concurrent.ExecutionContext.global

object CatchupFromZero extends IOApp {

  def loop(rpc: Tezos, current: Long = 9000000L, until: Long = 9120000): IO[Unit] =
    for {
      block <- rpc.getBlockByHeight(current)
      _ <- IO { println(s"block ${block.level} - ${block.hash}") }
      transactions <- rpc.getTransactions(block.level)
      _ <- IO(println(s"transactions: ${transactions.seq.size}"))
      l <- if (current + 1 < until) loop(rpc, current + 1, until) else IO.unit
    } yield l

  def run(args: List[String]): IO[ExitCode] = {
    implicit val ec = global
    implicit val config = Config.fromEnv
    RPCClient
      .tezosConseil(
        config.hosts,
        config.port,
        config.username,
        config.password,
        onErrorRetry = { (_, e: Throwable) =>
          IO(println(e))
        },
        apiKey = "abc"
      )
      .use { tezos =>
        for {
          _ <- loop(tezos)
        } yield ExitCode(0)
      }
  }
}
