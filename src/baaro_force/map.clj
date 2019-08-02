(ns baaro-force.map
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [baaro-force.core :as core]))

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

