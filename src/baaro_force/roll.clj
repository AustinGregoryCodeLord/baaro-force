(ns baaro-force.roll
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [baaro-force.core :as core]
            [baaro-force.items :as items]
            [baaro-force.utils :as utils]))

(def rarity-distribution
  {:common 50
   :uncommon 30
   :rare 20
   :epic 10})

(defn roll-d2
  []
  (inc (rand-int 2)))

(defn roll-d4
  []
  (inc (rand-int 4)))

(defn roll-d6
  []
  (inc (rand-int 6)))

(defn roll-d12
  []
  (inc (rand-int 12)))

(defn roll-d20
  []
  (inc (rand-int 20)))

(defn roll-d100
  []
  (inc (rand-int 100)))

(defn roll-d1000
  []
  (inc (rand-int 1000)))

(defn item?
  []
  (roll-d2))

(defn roll-for-rarity
  [roll]
  (if (<= roll 50)
    "Common"
    (if (and (> roll 50) (<= roll 80))
      "Uncommon"
      (if (and (> roll 80) (<= roll 90))
        "Rare"
        "Epic"))))

(defn roll-armor-piece
  [armor-type]
  (let [rarity (roll-for-rarity (roll-d100))
        armor (rand-nth (get-in items/armor-map [(utils/keywordize-class armor-type) :rarity (utils/keywordize-class rarity)]))]
    armor))

(defn roll-weapon-piece
  [weapon-type]
  (let [rarity (roll-for-rarity (roll-d100))
        weapon (rand-nth (get-in items/weapon-map [(utils/keywordize-class weapon-type) :rarity (utils/keywordize-class rarity)]))]
    weapon))

(defn populate-treasure-slots
  []
  (let [rolls (into [] (repeatedly 6 #(rand-int 2)))
        head (if (= (get rolls 0) 1)
               (roll-armor-piece "Head")
               nil)
        chest (if (= (get rolls 1) 1)
                (roll-armor-piece "Chest")
                nil)
        legs (if (= (get rolls 2) 1)
               (roll-armor-piece "Legs")
               nil)
        main-hand (if (= (get rolls 3) 1)
                    (roll-weapon-piece "Main-hand")
                    nil)
        off-hand (if (= (get rolls 4) 1)
                   (roll-weapon-piece "Off-hand")
                   nil)
        two-hand (if (= (get rolls 5) 1)
                   (roll-weapon-piece "Two-hand")
                   nil)
        gold (str (inc (rand-int 100)) " Gold")]
    (into [] (remove nil? [gold head chest legs main-hand off-hand two-hand]))))