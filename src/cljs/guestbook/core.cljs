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


;; Helper functions

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

(defn input [tag id label form]
  [:div.form-group
   [:label label]
   [tag
    {:type :text
     :value (id @form)
     :on-change
     #(swap! form
             assoc
             id (-> % .-target .-value))}]]
  )

(defn add-message [form]
  (POST "/api/message"
        {:params @form
         :handler
         #(do
           (swap! messages conj @form)
           (reset! form {}))}))

(defn message-form []
  (let [form (r/atom {})]
    (fn []
      [:div
       [:p (str @form)]
       [input :input.form-control :guest "vieras" form]
       [input :textarea.form-control :message "viesti" form]
       [:button.btn.btn-primary
        {:on-click #(add-message form)}
        "add message"]])))

;; the actual page structure

(defn home-page []
  [:div#myapp.container
   [:h3 "Vieraskirja, jätä viesti!"]
   [message-form]
   [:hr]
   [message-list @messages]])

(defn mount-components []
  (r/render [#'home-page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (mount-components))
