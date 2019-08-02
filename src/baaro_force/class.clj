(ns baaro-force.class
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [baaro-force.core :as core]
            [baaro-force.utils :as utils]))

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
                            :class-path [(utils/keywordize-class class) :upgrades]
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

(defn check-tier
  [character]
  (if (utils/in? tier-one-classes (:class character))
    1
    (if (utils/in? tier-two-classes (:class character))
      2
      (if (utils/in? tier-three-classes (:class character))
        3
        (if (utils/in? tier-four-classes (:class character))
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