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
package tezos

import io.circe.generic.auto._
import io.circe.parser.decode
import io.tokenanalyst.blockchainrpc.tezos.conseil.Codecs._
import io.tokenanalyst.blockchainrpc.tezos.conseil.Protocol.{BlockResponse, _}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProtocolSpec extends AnyFlatSpec with Matchers {

  behavior of "Tezos protocol"

  it should "decode TransactionResponse" in {
    val response =
      """
         {"secret":null,"storage_size":null,"number_of_slots":null,"internal":false,"delegatable":null,"source":"tz1XFTtQKCUfZkE8nWpJEdFgy3PADSUio9fA","consumed_gas":10207,"timestamp":1582801196000,"pkh":null,"nonce":null,"block_level":842455,"branch":null,"balance":null,"operation_group_hash":"opWnr67A4FaqXMjeArCu9juxYMmwk8T1e2K3JmogA7Ev5mzpQBm","public_key":null,"paid_storage_size_diff":null,"amount":4453298580,"delegate":null,"proposal":null,"block_hash":"BM37ywcCyTjxmSK2tWT7ymzi8zzLtzrrgoUYU1BS7u5HRZppRhE","spendable":null,"cycle":205,"status":"applied","operation_id":21458126,"manager_pubkey":null,"slots":null,"storage_limit":300,"storage":null,"counter":2598889,"script":null,"kind":"transaction","originated_contracts":null,"gas_limit":10600,"parameters":null,"destination":"tz1NcoDFXMAfB26mpBhVrdSHmppyTeccT6Fi","ballot":null,"period":null,"fee":1420,"level":null}
      """

    val decoded = decode[TransactionResponse](response)
    decoded shouldEqual Right(
      TransactionResponse(
        None,
        None,
        None,
        false,
        None,
        Some("tz1XFTtQKCUfZkE8nWpJEdFgy3PADSUio9fA"),
        10207,
        1582801196000L,
        None,
        None,
        842455L,
        Some(2598889L),
        None,
        None,
        Some("opWnr67A4FaqXMjeArCu9juxYMmwk8T1e2K3JmogA7Ev5mzpQBm"),
        None,
        None,
        None,
        None,
        Some("BM37ywcCyTjxmSK2tWT7ymzi8zzLtzrrgoUYU1BS7u5HRZppRhE"),
        Some(4453298580L),
        Some(10600L),
        None,
        Some(205L),
        "applied",
        Some(21458126L),
        None,
        None,
        Some(300L),
        None,
        None,
        "transaction",
        None,
        None,
        Some("tz1NcoDFXMAfB26mpBhVrdSHmppyTeccT6Fi"),
        None,
        None,
        Some(1420L),
        None
      )
    )
  }

  it should "decode BlockResponse snake case" in {
    val response =
      """
         {"meta_voting_period":25,"operations_hash":"LLoaK5U2CPj35KW5oxiSyTs6Loojmj7n627NZCfxdu5n3WJLxti1T","priority":0,"meta_level_position":842454,"consumed_gas":51232,"timestamp":1582801196000,"current_expected_quorum":5805,"context":"CoUjfNenv9MjtBCSG1nz9HibZtoohAQQDxKLWs4tVMLN9atGrbkT","baker":"tz1bf816tUrSLYsWkUrsDFH9kkbpg3oXjriR","active_proposal":"PsCARTHAGazKbHtnKfLzQg3kms52kSRpgnDY982a9oYsSXRLQEb","proto":5,"signature":"sigYcPZuKBDzFWsRRAiwS5psoS8CBatLm4ENQL4SKAhJpWd2QicbKt9YGrJp8aeDQ1FGVzqf3LQzLhzepqAfR1WeKhsfZNuJ","meta_cycle":205,"period_kind":"promotion_vote","hash":"BM37ywcCyTjxmSK2tWT7ymzi8zzLtzrrgoUYU1BS7u5HRZppRhE","meta_voting_period_position":23254,"fitness":"01,000000000002dad7","validation_pass":4,"meta_level":842455,"nonce_hash":null,"expected_commitment":false,"protocol":"PsBabyM1eUXZseaJdmXFApDSBqj8YBfwELoxZHHW77EMcAbbwAS","predecessor":"BKjXVcBJ7WXhxqnjpH4rLcUG3ufUxbAo3KzcgWUxaPrSpH8yxES","meta_cycle_position":2774,"chain_id":"NetXdQprcVkpaWU","level":842455}
      """
    val decoded = decode[BlockResponse](response)
    decoded shouldEqual Right(
      BlockResponse(
        842455,
        5,
        Some("BKjXVcBJ7WXhxqnjpH4rLcUG3ufUxbAo3KzcgWUxaPrSpH8yxES"),
        1582801196000L,
        4,
        "01,000000000002dad7",
        "CoUjfNenv9MjtBCSG1nz9HibZtoohAQQDxKLWs4tVMLN9atGrbkT",
        "sigYcPZuKBDzFWsRRAiwS5psoS8CBatLm4ENQL4SKAhJpWd2QicbKt9YGrJp8aeDQ1FGVzqf3LQzLhzepqAfR1WeKhsfZNuJ",
        "PsBabyM1eUXZseaJdmXFApDSBqj8YBfwELoxZHHW77EMcAbbwAS",
        "NetXdQprcVkpaWU",
        "BM37ywcCyTjxmSK2tWT7ymzi8zzLtzrrgoUYU1BS7u5HRZppRhE",
        "LLoaK5U2CPj35KW5oxiSyTs6Loojmj7n627NZCfxdu5n3WJLxti1T",
        "promotion_vote",
        5805,
        Some("PsCARTHAGazKbHtnKfLzQg3kms52kSRpgnDY982a9oYsSXRLQEb"),
        "tz1bf816tUrSLYsWkUrsDFH9kkbpg3oXjriR",
        51232,
        842455L,
        842454L,
        205,
        2774,
        25,
        23254,
        false,
        0
      )
    )
  }

  it should "decode BlockResponse camel case extended" in {
    val response =
      """
        {"block":{"level":859088,"proto":6,"predecessor":"BL5dhBUujN8KTke1SncgzNgHHJuqZhbSWNEJYoEBoW5XScqGJSa","timestamp":1583828497000,"validationPass":4,"fitness":"01,0000000000031bd0","context":"CoV4WAaYiezLQ27Bx4ZnEZucTyY9Cbmn83Bu6i2BShmozUBQ5p8C","signature":"sigSaJSu3v4HwbfsAjZ3pT2jPfFhoqEqa2zjGdbGYAAEQBKiC445dagM8A4ejj7SCbuuk7rL8mkLka7FruZv5UMWFeTvGcgj","protocol":"PsCARTHAGazKbHtnKfLzQg3kms52kSRpgnDY982a9oYsSXRLQEb","chainId":"NetXdQprcVkpaWU","hash":"BLezjhhxkRqbX4J2GrsWNDhdjppaek2nyYkzYb9vXBXzKiknnvr","operationsHash":"LLoZYoPURAJmw3Nu5bijA5YsEUv6ccHNhqV4aZ9haBkcGBpfpDbiQ","periodKind":"proposal","currentExpectedQuorum":5764,"baker":"tz1VmiY38m3y95HqQLjMwqnMS7sdMfGomzKi","consumedGas":30621,"metaLevel":859088,"metaLevelPosition":859087,"metaCycle":209,"metaCyclePosition":3023,"metaVotingPeriod":26,"metaVotingPeriodPosition":7119,"expectedCommitment":false,"priority":0},"operation_groups":[]}
      """
    val decoded = decode[ExtendedBlockResponse](response)
    decoded shouldEqual Right(
      ExtendedBlockResponse(
        859088,
        6,
        Some("BL5dhBUujN8KTke1SncgzNgHHJuqZhbSWNEJYoEBoW5XScqGJSa"),
        1583828497000L,
        4,
        "01,0000000000031bd0",
        "CoV4WAaYiezLQ27Bx4ZnEZucTyY9Cbmn83Bu6i2BShmozUBQ5p8C",
        "sigSaJSu3v4HwbfsAjZ3pT2jPfFhoqEqa2zjGdbGYAAEQBKiC445dagM8A4ejj7SCbuuk7rL8mkLka7FruZv5UMWFeTvGcgj",
        "PsCARTHAGazKbHtnKfLzQg3kms52kSRpgnDY982a9oYsSXRLQEb",
        "NetXdQprcVkpaWU",
        "BLezjhhxkRqbX4J2GrsWNDhdjppaek2nyYkzYb9vXBXzKiknnvr",
        "LLoZYoPURAJmw3Nu5bijA5YsEUv6ccHNhqV4aZ9haBkcGBpfpDbiQ",
        "proposal",
        5764,
        None,
        "tz1VmiY38m3y95HqQLjMwqnMS7sdMfGomzKi",
        30621,
        859088,
        859087,
        209,
        3023,
        26,
        7119,
        false,
        0
      )
    )
  }
}
