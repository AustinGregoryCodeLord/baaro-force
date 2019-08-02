(ns baaro-force.characters
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [baaro-force.core :as core]
            [baaro-force.roll :as roll]))

(def common-enemy-list
  ["Goblin" "Lizardman"])

(def uncommon-enemy-list
  ["Thug" "Dark Mage"])

(def rare-enemy-list
  ["Assassin" "Pyromaniac"])

(def epic-enemy-list
  ["Shaun" "Craig"])

(def common-neutral-list
  ["Wolf" "Boar" "Undead"])

(def uncommon-neutral-list
  ["Bear" "Hawk" "Ghost"])

(def rare-neutral-list
  ["Demon" "Treant"])

(def epic-neutral-list
  ["Dragon" "Woodland King"])

(def creature-map
  {:enemy {:rarity {:common common-enemy-list
                    :uncommon uncommon-enemy-list
                    :rare rare-enemy-list
                    :epic epic-enemy-list}}
   :neutral {:rarity {:common common-neutral-list
                      :uncommon uncommon-neutral-list
                      :rare rare-neutral-list
                      :epic epic-neutral-list}}})

(defn creature-stats
  [creature-type rarity]
  (let [creature rarity]
    (case creature
      "Common" (hash-map :type creature-type
                         :rarity rarity
                         :health (+ 8 (rand-int 6))
                         :mana (+ 8 (rand-int 6))
                         :att (+ 8 (rand-int 6))
                         :def (+ 8 (rand-int 6))
                         :magic (+ 8 (rand-int 6)))
      "Uncommon" (hash-map :type creature-type
                           :rarity rarity
                           :health (+ 11 (rand-int 6))
                           :mana (+ 11 (rand-int 6))
                           :att (+ 11 (rand-int 6))
                           :def (+ 11 (rand-int 6))
                           :magic (+ 11 (rand-int 6)))
      "Rare" (hash-map :type creature-type
                       :rarity rarity
                       :health (+ 14 (rand-int 6))
                       :mana (+ 14 (rand-int 6))
                       :att (+ 14 (rand-int 6))
                       :def (+ 14 (rand-int 6))
                       :magic (+ 14 (rand-int 6)))
      "Epic" (hash-map :type creature-type
                       :rarity rarity
                       :health (+ 17 (rand-int 6))
                       :mana (+ 17 (rand-int 6))
                       :att (+ 17 (rand-int 6))
                       :def (+ 17 (rand-int 6))
                       :magic (+ 17 (rand-int 6))))))

(defn create-random-enemy-creature
  []
  (let [rarity (roll/roll-for-rarity (roll/roll-d100))
        name (rand-nth (get-in creature-map [:enemy :rarity (utils/keywordize-class rarity)]))
        stats (creature-stats name rarity)]
    stats))

(defn create-random-neutral-creature
  []
  (let [rarity (roll/roll-for-rarity (roll/roll-d100))
        name (rand-nth (get-in creature-map [:neutral :rarity (utils/keywordize-class rarity)]))
        stats (creature-stats name rarity)]
    stats))