(ns baaro-force.core
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]))

(def class-map
  (hash-map :mage {:dark-mage  {:twilight-mage {:twilight-lord  ""
                                                :balance-wizard ""
                                                :upgrades ["Twilight Lord" "Balance Wizard"]}
                                :undead        {:necromancer       ""
                                                :skeletal-sorceror ""
                                                :upgrades ["Necromancer" "Skeletal Sorceror"]}
                                :demon         {:hell-wizard ""
                                                :warlock     ""
                                                :upgrades ["Hell Wizard" "Warlock"]}
                                :lunar-mage    {:twilight-lord  ""
                                                :moon-ascendant ""
                                                :upgrades ["Twilight Lord" "Moon Ascendant"]}
                                :void-mage     {:nullomancer   ""
                                                :galaxy-wizard ""
                                                :upgrades ["Twilight Lord" "Balance Wizard"]}
                                :upgrades ["Twilight Mage" "Undead" "Demon" "Lunar Mage" "Void Mage"]}
                   :light-mage {:twilight-mage {:twilight-lord  ""
                                                :balance-wizard ""
                                                :upgrades ["Twilight Lord" "Balance Wizard"]}
                                :priest        {:bishop ""
                                                :angel  ""
                                                :upgrades ["Bishop" "Angel"]}
                                :illusionist   {:summoner        ""
                                                :ethereal-wizard ""
                                                :upgrades ["Summoner" "Ethereal Wizard"]}
                                :solar-mage    {:twilight-lord ""
                                                :sun-ascendant ""
                                                :upgrades ["Twilight Lord" "Sun Ascendant"]}
                                :monk          {:sun-ascendant ""
                                                :zen-lord      ""
                                                :upgrades ["Sun Ascendant" "Zen Lord"]}
                                :upgrades ["Twilight Mage" "Priest" "Illusionist" "Solar Mage" "Monk"]}
                   :earth-mage {:volcanic-mage {:lava-lord         ""
                                                :apocalypse-wizard ""
                                                :upgrades ["Lava Lord" "Apocalypse Wizard"]}
                                :nature-mage   {:shapeshifter  ""
                                                :cosmic-caster ""
                                                :upgrades ["Shapeshifter" "Cosmic Caster"]}
                                :treant        {:arbor-lord   ""
                                                :terra-wizard ""
                                                :upgrades ["Terra Wizard" "Arbor Lord"]}
                                :fairy         {:spiritualist ""
                                                :fae-sorceror ""
                                                :upgrades ["Spiritualist" "Fae Sorceror"]}
                                :gem-mage    {:crystal-wizard   ""
                                              :enchanter ""
                                              :upgrades ["Crystal Wizard" "Enchanter"]}
                                :upgrades ["Volcanic Mage" "Nature Mage" "Treant" "Fairy" "Gem Mage"]}
                   :fire-mage  {:volcanic-mage {:lava-lord         ""
                                                :apocalypse-wizard ""
                                                :upgrades ["Lava Lord" "Apocalypse Wizard"]}
                                :pyromaniac    {:flame-elemental ""
                                                :infernal-wizard ""
                                                :upgrades ["Flame Elemental" "Infernal Wizard"]}
                                :demon         {:hell-wizard ""
                                                :warlock     ""
                                                :upgrades ["Hell Wizard" "Warlock"]}
                                :solar-mage    {:twilight-lord ""
                                                :sun-ascendant ""
                                                :upgrades ["Twilight Lord" "Sun Ascendant"]}
                                :dragon        {:draconic-wizard ""
                                                :dragon-knight   ""
                                                :upgrades ["Draconic Wizard" "Dragon Knight"]}
                                :upgrades ["Volcanic Mage" "Pyromaniac" "Demon" "Solar Mage" "Dragon"]}
                   :water-mage {:mist-mage    {:hurricane-wizard ""
                                               :mist-weaver      ""
                                               :upgrades ["Hurricane Wizard" "Mist Weaver"]}
                                :ice-sorceror {:ice-elemental   ""
                                               :blizzard-wizard ""
                                               :upgrades ["Ice Elemental" "Blizzard Wizard"]}
                                :merfolk      {:sea-ascendant ""
                                               :merfolk-healer ""
                                               :upgrades ["Sea Ascendant" "Merfolk Healer"]}
                                :hydro-wizard   {:sea-ascendant ""
                                                 :wave-caller ""
                                                 :upgrades ["Sea Ascendant" "Wave Caller"]}
                                :upgrades ["Mist Mage" "Ice Sorceror" "Merfolk" "Hydro Wizard"]}
                   :wind-mage  {:mist-mage    {:hurricane-wizard ""
                                               :mist-weaver      ""
                                               :upgrades ["Hurricane Wizard" "Mist Weaver"]}
                                :aero-caster {:wind-surfer   ""
                                              :air-ascendant ""
                                              :upgrades ["Wind Surfer" "Air Ascendant"]}
                                :monk {:sun-ascendant ""
                                       :zen-lord ""
                                       :upgrades ["Sun Ascendant" "Zen Lord"]}
                                :ghost {:spectral-sorceror ""
                                        :soul-whisperer ""
                                        :upgrades ["Spectral Sorceror" "Soul Whisperer"]}
                                :upgrades ["Mist Mage" "Aerocaster" "Monk" "Ghost"]}
                   :scholar  {:engineer    {:cyborg ""
                                            :mech-crafter ""
                                            :upgrades ["Cyborg" "Mech Crafter"]}
                              :alchemist {:potion-master ""
                                          :mutation ""
                                          :upgrades ["Potion Master" "Mutation"]}
                              :scientist {:cosmic-caster ""
                                          :truth-seeker ""
                                          :upgrades ["Cosmic Caster" "Truth Seeker"]}
                              :strategist {:general ""
                                           :tactitian ""
                                           :upgrades ["General" "Tactician"]}
                              :musician {:bard ""
                                         :sonic-wizard ""
                                         :upgrades ["Bard" "Sonic Wizard"]}
                              :upgrades ["Engineer" "Alchemist" "Scientist" "Strategist" "Musician"]}
                   :upgrades ["Dark Mage" "Light Mage" "Fire Mage" "Water Mage" "Wind Mage" "Earth Mage" "Scholar"]}
            :warrior {:paladin {:monk {:zen-lord ""
                                       :sun-ascendant ""
                                       :upgrades ["Zen Lord" "Sun Ascendant"]}
                                :justice-seeker {:enforcer ""
                                                 :judge ""
                                                 :upgrades ["Enforcer" "Judge"]}
                                :light-lord {:sun-ascendant ""
                                             :son-of-god ""
                                             :upgrades ["Sun Ascendant" "Son of God"]}
                                :valor-seeker {:legend ""
                                               :guardian ""
                                               :upgrades ["Legend" "Guardian"]}
                                :upgrades ["Monk" "Justice Seeker" "Light Lord" "Valor Seeker"]}
                      :thug {:savage {:maniac ""
                                      :blood-brawler ""
                                      :upgrades ["Maniac" "Blood Brawler"]}
                             :pirate {:captain ""
                                      :sea-dweller ""
                                      :upgrades ["Captain" "Sea Dweller"]}
                             :brawler {:blood-brawler ""
                                       :bruiser ""
                                       :upgrades ["Blood Brawler" "Bruiser"]}
                             :outlaw {:master-thief ""
                                      :godfather ""
                                      :upgrades ["Master Thief" "Godfather"]}
                             :upgrades ["Savage" "Pirate" "Brawler" "Outlaw"]}
                      :samurai {:kishi {:upgrades []}
                                :hatamoto {:daimyo {:shogun ""
                                                    :emperor ""
                                                    :upgrades ["Shogun" "Emperor"]}
                                           :upgrades ["Daimyo"]}
                                :kachi {:upgrades []}
                                :komono {:blade-dancer ""
                                         :feather ""
                                         :upgrades ["Blade Dancer" "Feather"]}
                                :upgrades ["Kishi" "Hatamoto" "Kachi" "Komono"]}
                      :soldier {:knight {:commander ""
                                         :hero ""
                                         :upgrades ["Commander" "Hero"]}
                                :mercenary {:blade-dancer ""
                                            :bounty-hunter ""
                                            :upgrades ["Blade Dancer" "Bounty Hunter"]}
                                :cavalry {:blood-rider ""
                                          :elephant-rider ""
                                          :upgrades ["Blood Rider" "Elephant Rider"]}
                                :defender {:shield-master ""
                                           :guardian ""
                                           :upgrades ["Shield Master" "Guardian"]}
                                :upgrades ["Knight" "Mercenary" "Cavalry" "Defender"]}
                      :duelist {:blade-master {:blade-dancer ""
                                               :weapon-master ""
                                               :upgrades ["Blade Dancer" "Weapon Master"]}
                                :gladiator {:pit-lord ""
                                            :upgrades ["Pit Lord"]}
                                :martial-artist {:zen-lord ""
                                                 :upgrades ["Zen Lord"]}
                                :upgrades ["Gladiator" "Blade Master" "Martial Artist"]}
                      :street-fighter {:savage {:maniac ""
                                                :blood-brawler ""
                                                :upgrades ["Maniac" "Blood Brawler"]}
                                       :brawler {:blood-brawler ""
                                                 :bruiser ""
                                                 :upgrades ["Blood Brawler" "Bruiser"]}
                                       :martial-artist {:zen-lord ""
                                                        :upgrades ["Zen Lord"]}
                                       :boxer {:ko-king ""
                                               :knuckle-buster ""}
                                       :upgrades ["Savage" "Brawler" "Martial Artist" "Boxer"]}
                      :nature-warrior {:centaur {:centaur-lord ""
                                                 :jouster ""
                                                 :upgrades ["Centaur Lord" "Jouster"]}
                                       :bear {:bear-knight ""
                                              :ursa-ascendant ""
                                              :upgrades ["Bear Knight" "Ursa Ascendant"]}
                                       :monk {:zen-lord ""
                                              :sun-ascendant ""
                                              :upgrades ["Zen Lord" "Sun Ascendant"]}
                                       :tomahawk-fighter {:chief ""
                                                          :beast-lord ""
                                                          :upgrades ["Chief" "Beast Lord"]}
                                       :wolf {:werewolf ""
                                              :wolf-spirit ""
                                              :upgrades ["Werewolf" "Wolf Spirit"]}
                                       :upgrades ["Centaur" "Bear" "Monk" "Tomahawk Fighter" "Wolf"]}
                      :upgrades ["Paladin" "Thug" "Samurai" "Soldier" "Duelist" "Street Fighter" "Nature Warrior"]}
            :archer {:ranger {:horseback-ranger {:khan's-rider ""
                                                 :dragonback-marksman ""
                                                 :upgrades ["Khan's Rider" "Dragonback Marksman"]}
                              :crossbowman {:wrist-bow-ranger ""
                                            :bolt-expert ""
                                            :upgrades ["Wrist-Bow Ranger" "Bolt Expert"]}
                              :bow-master {:upgrades []}
                              :sharpshooter {:rifler ""
                                             :sniper ""
                                             :upgrades ["Rifler" "Sniper"]}
                              :upgrades ["Horseback Ranger" "Crossbowman" "Bow Master" "Sharpshooter"]}
                     :gunner {:shotgunner {:blast-master ""
                                           :hand-cannoner ""
                                           :upgrades ["Blast Master" "Hand Cannoner"]}
                              :sharpshooter {:sniper ""
                                             :rifler ""
                                             :upgrades ["Sniper" "Rifler"]}
                              :cowboy {:wrangler ""
                                       :sheriff ""
                                       :upgrades ["Wrangler" "Sheriff"]}
                              :pirate {:captain ""
                                       :sea-dweller ""
                                       :upgrades ["Captain" "Sea Dweller"]}
                              :upgrades ["Shotgunner" "Sharpshooter" "Cowboy" "Pirate"]}
                     :hunter {:sharpshooter {:sniper ""
                                             :rifler ""
                                             :upgrades ["Sniper" "Rifler"]}
                              :trapper {:upgrades []}
                              :beast-master {:zoo-keeper ""
                                             :kindred-companion ""
                                             :upgrades ["Zoo Keeper" "Kindred Companion"]}
                              :tracker {:blood-hound ""
                                        :bounty-hunter ""
                                        :upgrades ["Blood Hound" "Bounty Hunter"]}
                              :upgrades ["Sharpshooter" "Trapper" "Beast Master" "Tracker"]}
                     :thrower {:monkey {:fling-master ""
                                        :gorilla ""
                                        :upgrades ["Fling Master" "Gorilla"]}
                               :dart-thrower {:piercer ""
                                              :poison-master ""
                                              :upgrades ["Piercer" "Poison Master"]}
                               :knife-thrower {:upgrades []}
                               :bomb-lobber {:grenadier ""
                                             :boom-boom-lord ""
                                             :upgrades ["Grenadier" "Boom Boom Lord"]}
                               :upgrades ["Monkey" "Dart Thrower" "Knife Thrower" "Bomb Lobber"]}
                     :elemental-archer {:scorch-ranger {:fire-elemental ""
                                                        :infernal-marksman ""
                                                        :upgrades ["Fire Elemental" "Infernal Marksman"]}
                                        :glacial-ranger {:ice-elemental ""
                                                         :blizzard-marksman ""
                                                         :upgrades ["Ice Elemental" "Blizzard Marksman"]}
                                        :flurry-ranger {:air-assailant ""
                                                        :eagle ""
                                                        :upgrades ["Air Assailant" "Eagle"]}
                                        :gem-ranger {:crystal-marksman ""
                                                     :gem-golem ""
                                                     :upgrades ["Crystal Marksman" "Gem Golem"]}
                                        :upgrades ["Scorch Ranger" "Glacial Ranger" "Flurry Ranger" "Gem Ranger"]}
                     :explosives-artist {:bomb-lobber {:grenadier ""
                                                       :boom-boom-lord ""
                                                       :upgrades ["Grenadier" "Boom Boom Lord"]}
                                         :big-gunner {:hand-cannoner ""
                                                      :boom-boom-lord ""
                                                      :upgrades ["Hand Cannoner" "Boom Boom Lord"]}
                                         :gnome {:heli-gunner ""
                                                 :button-presser ""
                                                 :upgrades ["Heli-gunner" "Button Presser"]}
                                         :goblin {:mech-suit ""
                                                  :gadget-master ""
                                                  :upgrades ["Mech Suit" "Gadget Master"]}
                                         :upgrades ["Bomb Lobber" "Big Gunner" "Gnome" "Goblin"]}
                     :upgrades ["Ranger" "Thrower" "Gunner" "Hunter" "Elemental Archer" "Explosives Artist"]}
            :rogue {:assassin {:tracker          {:bounty-hunter ""
                                                  :blood-hound   ""
                                                  :upgrades      ["Bounty Hunter" "Blood Hound"]}
                               :shadow           {:nightmare ""
                                                  :upgrades  ["Nightmare"]}
                               :panther          {:night-prowler ""
                                                  :upgrades      ["Night Prowler"]}
                               :toxin-enthusiast {:poison-master ""
                                                  :mutation      ""
                                                  :upgrades      ["Poison Master" "Mutation"]}
                               :blade-master     {:blade-dancer  ""
                                                  :weapon-master ""
                                                  :upgrades      ["Blade Dancer" "Weapon Master"]}
                               :upgrades         ["Tracker" "Shadow" "Panther" "Toxin Enthusiast" "Blade Master"]}
                    :scout    {:spy          {:secret-agent ""
                                              :godfather    ""
                                              :upgrades     ["Secret Agent" "Godfather"]}
                               :navigator    {:captain  ""
                                              :explorer ""
                                              :upgrades ["Captain" "Explorer"]}
                               :beast-master {:zoo-keeper        ""
                                              :kindred-companion ""
                                              :upgrades          ["Zoo Keeper" "Kindred Companion"]}
                               :tracker      {:blood-hound   ""
                                              :bounty-hunter ""
                                              :upgrades      ["Blood Hound" "Bounty Hunter"]}
                               :upgrades     ["Spy" "Navigator" "Beast Master" "Tracker"]}
                    :ninja    {:saboteur {:master-thief ""
                                          :arsonist ""
                                          :upgrades ["Master Thief" "Arsonist"]}
                               :spy {:secret-agent ""
                                     :godfather ""
                                     :upgrades ["Secret Agent" "Godfather"]}
                               :shadow {:nightmare ""
                                        :upgrades ["Nightmare"]}
                               :knife-thrower {:upgrades []}
                               :disguise-master {:faceless-man ""
                                                 :upgrades ["Faceless Man"]}
                               :upgrades ["Saboteur" "Spy" "Shadow" "Knife Thrower" "Disguise Master"]}
                    :thrower {:monkey {:fling-master ""
                                       :gorilla ""
                                       :upgrades ["Fling Master" "Gorilla"]}
                              :dart-thrower {:piercer ""
                                             :poison-master ""
                                             :upgrades ["Piercer" "Poison Master"]}
                              :knife-thrower {:upgrades []}
                              :bomb-lobber {:grenadier ""
                                            :boom-boom-lord ""
                                            :upgrades ["Grenadier" "Boom Boom Lord"]}
                              :upgrades ["Monkey" "Dart Thrower" "Knife Thrower" "Bomb Lobber"]}
                    :duelist {:blade-master {:blade-dancer ""
                                             :weapon-master ""
                                             :upgrades ["Blade Dancer" "Weapon Master"]}
                              :gladiator {:pit-lord ""
                                          :upgrades ["Pit Lord"]}
                              :martial-artist {:zen-lord ""
                                               :upgrades ["Zen Lord"]}
                              :upgrades ["Gladiator" "Blade Master" "Martial Artist"]}
                    :thug {:savage {:maniac ""
                                    :blood-brawler ""
                                    :upgrades ["Maniac" "Blood Brawler"]}
                           :pirate {:captain ""
                                    :sea-dweller ""
                                    :upgrades ["Captain" "Sea Dweller"]}
                           :brawler {:blood-brawler ""
                                     :bruiser ""
                                     :upgrades ["Blood Brawler" "Bruiser"]}
                           :outlaw {:master-thief ""
                                    :godfather ""
                                    :upgrades ["Master Thief" "Godfather"]}
                           :upgrades ["Savage" "Pirate" "Brawler" "Outlaw"]}
                    :upgrades ["Assassin" "Ninja" "Scout" "Thug" "Duelist" "Thrower"]}))

(def terrain-type-to-symbol
  {:void "V"
   :plains "P"
   :forest "F"
   :mountain "M"
   :water "W"
   :lava "L"
   :stone "S"
   :hills "H"})

(def character-type-to-symbol
  {:ally "A"
   :neutral "N"
   :enemy "E"})

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

(def conditions-map
  {:weather {:sun "+25% fire damage"
             :rain "+25% water damage"
             :blizzard "+50% frost damage, 10% chance at start of each characters turn to be frozen"
             :wind "+25% wind damage"
             :earthquake "+25% earth damage, 10% chance at start of each round to knockdown and damage 20% of characters"}
   :time-of-day {:night "+25% dark damage"
                 :day "+25% light damage"}
   :season {:spring "+10% earth damage, +10% water damage"
            :summer "+10% fire damage"
            :fall "+10% wind damage"
            :winter "+25% frost damage"}})

(def rarity-distribution
  {:common 50
   :uncommon 30
   :rare 20
   :epic 10})

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

(def tier-one-classes
  ["Mage"
   "Warrior"
   "Rogue"
   "Archer"])

(def tier-two-classes
  ["Dark Mage"
   "Light Mage"
   "Earth Mage"
   "Fire Mage"
   "Water Mage"
   "Wind Mage"
   "Scholar"])

(def tier-three-classes
  ["Twilight Mage"
   "Undead"
   "Demon"
   "Lunar Mage"
   "Void Mage"
   "Priest"
   "Illusionist"
   "Solar Mage"
   "Monk"
   "Volcanic Mage"
   "Nature Mage"
   "Treant"
   "Fairy"
   "Gem Mage"
   "Pyromaniac"
   "Dragon"
   "Mist Mage"
   "Ice Sorceror"
   "Merfolk"
   "Hydro Wizard"
   "Aero Caster"
   "Ghost"
   "Engineer"
   "Alchemist"
   "Scientist"
   "Strategist"
   "Musician"])

(def tier-four-classes
  ["Twilight Lord"
   "Balance Wizard"
   "Necromancer"
   "Skeletal Sorceror"
   "Hell Wizard"
   "Warlock"
   "Moon Ascendant"
   "Nullomancer"
   "Galaxy Wizard"
   "Bishop"
   "Angel"
   "Summoner"
   "Ethereal Wizard"
   "Sun Ascendant"
   "Zen Lord"
   "Lava Lord"
   "Apocalypse Wizard"
   "Shapeshifter"
   "Cosmic Caster"
   "Arbor Lord"
   "Terra Wizard"
   "Spiritualist"
   "Fae Sorceror"
   "Crystal Wizard"
   "Enchanter"
   "Flame Elemental"
   "Infernal Wizard"
   "Draconic Wizard"
   "Dragon Knight"
   "Hurricane Wizard"
   "Mist Weaver"
   "Ice Elemental"
   "Blizzard Wizard"
   "Sea Ascendant"
   "Air Ascendant"
   "Merfolk Healer"
   "Wave Caller"
   "Wind Surfer"
   "Spectral Sorceror"
   "Soul Whisperer"
   "Cyborg"
   "Mech Crafter"
   "Potion Master"
   "Mutation"
   "Cosmic Caster"
   "Truth Seeker"
   "General"
   "Tactitian"
   "Bard"
   "Sonic Wizard"])

(defn keywordize-class
  [class]
  (keyword (string/lower-case (string/replace class #" " "-"))))

(defn create-tier-one-character
  []
  (let [class (rand-nth tier-one-classes)
        character (hash-map :name "Woodrow"
                            :level 1
                            :class class
                            :health (+ 8 (rand-int 6))
                            :mana (+ 8 (rand-int 6))
                            :att (+ 8 (rand-int 6))
                            :def (+ 8 (rand-int 6))
                            :magic (+ 8 (rand-int 6))
                            :class-path [(keywordize-class class) :upgrades]
                            :items {:head nil
                                    :chest nil
                                    :legs nil
                                    :main-hand nil
                                    :off-hand nil})]
    character))


(defn create-tier-two-character
  []
  (hash-map :name "Woodrow"
            :level 4
            :class (rand-nth tier-two-classes)
            :health (+ 11 (rand-int 6))
            :mana (+ 11 (rand-int 6))
            :att (+ 11 (rand-int 6))
            :def (+ 11 (rand-int 6))
            :magic (+ 11 (rand-int 6))
            :items {:head nil
                    :chest nil
                    :legs nil
                    :main-hand nil
                    :off-hand nil}))

(defn create-tier-three-character
  []
  (hash-map :name "Woodrow"
            :level 7
            :class (rand-nth tier-three-classes)
            :health (+ 14 (rand-int 6))
            :mana (+ 14 (rand-int 6))
            :att (+ 14 (rand-int 6))
            :def (+ 14 (rand-int 6))
            :magic (+ 14 (rand-int 6))
            :items {:head nil
                    :chest nil
                    :legs nil
                    :main-hand nil
                    :off-hand nil}))


(defn create-tier-four-character
  []
  (hash-map :name "Woodrow"
            :level 10
            :class (rand-nth tier-four-classes)
            :health (+ 17 (rand-int 6))
            :mana (+ 17 (rand-int 6))
            :att (+ 17 (rand-int 6))
            :def (+ 17 (rand-int 6))
            :magic (+ 17 (rand-int 6))
            :items {:head nil
                    :chest nil
                    :legs nil
                    :main-hand nil
                    :off-hand nil}))

(defn level-up
  [character]
  (hash-map :name (:name character)
            :level (inc (:level character))
            :class (:class character)
            :health (+ (:health character) (rand-int 2))
            :mana (+ (:mana character) (rand-int 2))
            :att (+ (:att character) (rand-int 2))
            :def (+ (:def character) (rand-int 2))
            :magic (+ (:magic character) (rand-int 2))
            :items {:head (:head (:items character))
                    :chest (:chest (:items character))
                    :legs (:legs (:items character))
                    :main-hand (:main-hand (:items character))
                    :off-hand (:off-hand (:items character))}))

(defn level-up-n-times
  [character n]
  (hash-map :name (:name character)
            :level (+ (:level character) n)
            :class (:class character)
            :health (repeatedly n (+ (:health character) (rand-int 2)))
            :mana (repeatedly n (+ (:mana character) (rand-int 2)))
            :att (repeatedly n (+ (:att character) (rand-int 2)))
            :def (repeatedly n (+ (:def character) (rand-int 2)))
            :magic (repeatedly n (+ (:magic character) (rand-int 2)))
            :items {:head (:head (:items character))
                    :chest (:chest (:items character))
                    :legs (:legs (:items character))
                    :main-hand (:main-hand (:items character))
                    :off-hand (:off-hand (:items character))}))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn check-tier
  [character]
  (if (in? tier-one-classes (:class character))
    1
    (if (in? tier-two-classes (:class character))
      2
      (if (in? tier-three-classes (:class character))
        3
        (if (in? tier-four-classes (:class character))
          4
          "No Tier Found")))))

(defn promote?
  [character]
  (if (or (= (+ (check-tier character) 3) (:level character)) (not (= (check-tier character) 4)))
    true
    false))

;;((keywordize-class (:class character)) class-map)
(defn promote
  [choice character]
  (if (promote? character)
    (assoc character :class choice)))

(defn get-upgrade-options
  [character]
  (take 3 (shuffle (get-in class-map (:class-path character)))))

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
  (let [rarity (roll-for-rarity (roll-d100))
        name (rand-nth (get-in creature-map [:enemy :rarity (keywordize-class rarity)]))
        stats (creature-stats name rarity)]
    stats))

(defn create-random-neutral-creature
  []
  (let [rarity (roll-for-rarity (roll-d100))
        name (rand-nth (get-in creature-map [:neutral :rarity (keywordize-class rarity)]))
        stats (creature-stats name rarity)]
    stats))

(def biomes
  ["Lake" "Forest" "Plains" "Hills" "Mountains"])

(defn create-biomes
  [])

(defn initialize-map
  [map-size]
  (if (= map-size "S")
    (vec (repeat (+ (rand-int 6) 8) (vec (repeat (+ (rand-int 6) 8) (:void terrain-type-to-symbol)))))
    (if (= map-size "M")
      (vec (repeat (+ (rand-int 6) 16) (vec (repeat (+ (rand-int 6) 16) (:void terrain-type-to-symbol)))))
      (if (= map-size "L")
        (vec (repeat (+ (rand-int 6) 24) (vec (repeat (+ (rand-int 6) 24) (:void terrain-type-to-symbol)))))
        "Incorrect Map Size Key Given"))))

(defn roll-armor-piece
  [armor-type]
  (let [rarity (roll-for-rarity (roll-d100))
        armor (rand-nth (get-in armor-map [(keywordize-class armor-type) :rarity (keywordize-class rarity)]))]
    armor))

(defn roll-weapon-piece
  [weapon-type]
  (let [rarity (roll-for-rarity (roll-d100))
        weapon (rand-nth (get-in weapon-map [(keywordize-class weapon-type) :rarity (keywordize-class rarity)]))]
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


