(ns baaro-force.utils
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [baaro-force.core :as core]))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn keywordize-class
  [class]
  (keyword (string/lower-case (string/replace class #" " "-"))))