(ns baaro-force.items
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [baaro-force.core :as core]))

(def relic-map
  {:banner-of-baaro {:rarity   "Common"
                     :modifier "+2 att to team"}
   :aura-of-marvel  {:rarity "Common"
                     :modifier "+2 magic to team"}
   :forcefield-of-daranaan  {:rarity "Common"
                             :modifier "+2 def to team"}
   :purple-plague {:rarity "Common"
                   :modifier "-2 health to enemies"}
   :golden-heart {:rarity "Rare"
                  :modifier "+10hp to a unit"}
   :surge-of-inspiration {:rarity "Rare"
                          :modifier "+1 level to whole team"}
   :black-bandana {:rarity "Uncommon"
                   :modifier "+10% chance of treasure (from each slot)"}
   :old-wooden-book {:rarity "Uncommon"
                     :modifier "+10% experience rates for team"}
   :winds-of-ronin {:rarity "Rare"
                    :modifier "+1 movement to team"}
   :immortal-lobster {:rarity "Rare"
                      :modifier "The next unit that dies is restored to full hp"}
   :fruit-basket {:rarity "Common"
                  :modifier "Restore 4 health to whole team"}
   :brody's-wrist {:rarity "Rare"
                   :modifier "Enemies have a 25% chance to be frozen at start of combat"}
   :tepn's-shoes {:rarity "Rare"
                  :modifier "Your team cannot be slowed"}
   :galaxy-pill {:rarity "Epic"
                 :modifier "Instantly promote a unit to the next tier with randomized options"}
   :lion-friend {:rarity "Epic"
                 :modifier "Get a lion companion to fight for you until death"}
   :stanky-soup {:rarity "Uncommon"
                 :modifier "+50% att to team for first round of combat"}
   :blessing-of-nachman {:rarity "Uncommon"
                         :modifier "+50% def to team for first round of combat"}
   :hoob's-true-power {:rarity "Uncommon"
                       :modifier "+50% magic to team for first round of combat"}
   :zynai's-rocket-car {:rarity "Uncommon"
                        :modifier "+2 movement to team for first round of combat"}
   :proctored's-discovery {:rarity "Uncommon"
                           :modifier "Discover two random weapons"}
   :scyragon's-crafting {:rarity "Uncommon"
                         :modifier "Discover two random armor pieces"}
   :kat's-cauldron {:rarity "Uncommon"
                    :modifier "Discover three random potions"}
   :urie's-maul {:rarity "Rare"
                 :modifier "10% chance to stun enemy upon dealing damage for team"}
   :honrogo's-howl {:rarity "Uncommon"
                    :modifier "+1 stats to all animal classes on your team"}
   :cable's-theory {:rarity "Epic"
                    :modifier "+1 to team size"}
   :shaun's-decision {:rarity "Epic"
                      :modifier "Discover a character two levels higher than your team's average"}
   :ines'-wish {:rarity "Epic"
                :modifier "Restore all friendly characters to max health"}})

(def potion-map
  {:dragon's-breath-potion "Deals x damage to all enemies"
   :muscle-bolster-potion "+5 att to target character for combat"
   :forcefield-potion "+5 def to target character for combat"
   :star-power-potion "+5 magic to target character for combat"
   :cranberry-elixir "Restore a unit's health by 10"
   :demon's-rage-potion "+10 att to target character for turn"
   :bear's-hide-potion "+10 def to target character for turn"
   :wizard-juice-potion "+10 magic to target character for turn"
   :griffin-wing-potion "+5 movement to target character for turn"
   :feather-potion "+2 movement to target character for combat"
   :putrid-potion "-2 stats to units in an area"
   :toxic-sludge-potion "poison units in an area by 3"})

(def common-relics
  ["Banner of Baaro"
   "Aura of Marvel"
   "Forcefield"])

(def uncommon-relics
  ["Relic 1"])

(def rare-relics
  ["Relic 2"])

(def common-chest-armor
  ["Wood Chestplate"])

(def uncommon-chest-armor
  ["Steel Chestplate"])

(def rare-chest-armor
  ["Dragon Chestplate"])

(def epic-chest-armor
  ["Shaun's Tunic"])

(def common-head-armor
  ["Wood Helmet"])

(def uncommon-head-armor
  ["Steel Helmet"])

(def rare-head-armor
  ["Dragon Helmet"])

(def epic-head-armor
  ["Shaun's Helm"])

(def common-leg-armor
  ["Wood Legplate"])

(def uncommon-leg-armor
  ["Steel Legplate"])

(def rare-leg-armor
  ["Dragon Legplate"])

(def epic-leg-armor
  ["Shaun's Trousers"])

(def common-main-hand
  ["Wood Shortsword"])

(def uncommon-main-hand
  ["Steel Shortsword"])

(def rare-main-hand
  ["Dragon Shortsword"])

(def epic-main-hand
  ["Shaun's Fist"])

(def common-off-hand
  ["Wooden Shield"])

(def uncommon-off-hand
  ["Steel Shield"])

(def rare-off-hand
  ["Dragon Shield"])

(def epic-off-hand
  ["Shaun's Other Fist"])

(def common-two-hand
  ["Wood Club"])

(def uncommon-two-hand
  ["Steel Maul"])

(def rare-two-hand
  ["Dragon Broadsword"])

(def epic-two-hand
  ["Shaun's Hammer"])

(def armor-map
  {:head {:rarity {:common common-head-armor
                   :uncommon uncommon-head-armor
                   :rare rare-head-armor
                   :epic epic-head-armor}}
   :chest {:rarity {:common common-chest-armor
                    :uncommon uncommon-chest-armor
                    :rare rare-chest-armor
                    :epic epic-chest-armor}}
   :legs {:rarity {:common common-leg-armor
                   :uncommon uncommon-leg-armor
                   :rare rare-leg-armor
                   :epic epic-leg-armor}}})

(def weapon-map
  {:main-hand {:rarity {:common common-main-hand
                        :uncommon uncommon-main-hand
                        :rare rare-main-hand
                        :epic epic-main-hand}}
   :off-hand {:rarity {:common common-off-hand
                       :uncommon uncommon-off-hand
                       :rare rare-off-hand
                       :epic epic-off-hand}}
   :two-hand {:rarity {:common common-two-hand
                       :uncommon uncommon-two-hand
                       :rare rare-two-hand
                       :epic epic-two-hand}}})