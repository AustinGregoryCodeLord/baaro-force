(ns baaro-force.core
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [baaro-force.class :as class]))

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





