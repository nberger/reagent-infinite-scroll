(ns reagent-infinite-scroll.core
  (:require
    [reagent.core :as reagent :refer [atom]]
    [reagent-infinte-scroll.scroll :as scroll]))

;; -------------------------
;; Views

(def page-size 10)
(def db
  (atom {:items (map inc (range page-size))}))

(defn items-list [items]
  [:ul {:style {:list-style-type :none}}
   (for [item items]
     ^{:key item}
     [:li {:style {:height "100px" :width "400px"
                   :border "1px solid gray"}}
      [:div "Item number " item]])])

(defn home-page []
  (fn []
    (let [items (:items @db)
          loading? (:loading? @db)
          more-items-available? (< (count items) 40)]
      (println "count " (count items) "more available? " more-items-available?)
      [:div
       [:h2 "Welcome to reagent-infinite-scroll example"]
       [:p "This is an example page for the infinite-scroll code from "
        [:a {:href "https://gist.github.com/nberger/b5e316a43ffc3b7d5e084b228bd83899"}
         "this gist"]]
       [items-list items]
       [:div
        (cond
          loading?
          "Loading more..."

          more-items-available?
          "Scroll to here!"

          :else
          "No more items!!")]
       [scroll/infinite-scroll {:can-show-more? more-items-available?
                                :load-fn (fn []
                                           (swap! db assoc :loading? true)
                                           (js/setTimeout
                                             (fn []
                                               (swap! db assoc :loading? false)
                                               (swap! db update :items
                                                      (fn [items]
                                                        (concat items (range (inc (last items))
                                                                             (+ (last items) (inc page-size))))))
                                               (println "finished loading. count: " (count (:items @db))))
                                             1000))}]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
