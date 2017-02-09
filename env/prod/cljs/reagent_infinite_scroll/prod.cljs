(ns reagent-infinite-scroll.prod
  (:require [reagent-infinite-scroll.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
