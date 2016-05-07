(ns guestbook.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [guestbook.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(def messages (r/atom []))

(GET "/api/messages"
     {:handler #(reset! messages %)})

(defn message-item [{:keys [guest message]}]
  [:li guest " says " message])

(defn message-list [messages]
  [:ul
   (for [[idx message] (map-indexed vector messages)]
     ^{:key idx}
     [message-item message])])

(defn wellcome []
  [:div.wellcome
   [:h2 "Wellcome to Ansku's site!"]])

(defn home-page []
  [:div#myapp.container
   [:h3 "guestbook"]
   [:hr]
   [message-list @messages]
   [wellcome]])

(defn mount-components []
  (r/render [#'home-page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (mount-components))
