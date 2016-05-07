(ns guestbook.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [guestbook.db.core :as db]))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  (context "/api" []
    :tags ["guestbook"]

    (GET "/messages" []
      :return       [{:guest String :message String}]
      :summary      "get messages"
      (ok (db/get-messages)))

    (POST "/message" request
      :return      Long
      :body-params [guest :- String message :- String]
      :summary     "add new message"
      (ok (db/add-message {:guest guest
                           :message message})))))
