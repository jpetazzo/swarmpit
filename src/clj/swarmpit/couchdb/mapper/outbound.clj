(ns swarmpit.couchdb.mapper.outbound
  (:require [buddy.hashers :as hashers]
            [swarmpit.config :refer [config]]
            [swarmpit.uuid :refer [uuid]]))

(defn ->password
  [password]
  (hashers/derive password (config :password-hashing)))

(defn ->user
  [user]
  (-> (assoc user :password (->password (:password user))
                  :type "user")
      (dissoc :isValid)))

(defn ->registry
  [registry]
  (-> (assoc registry :type "registry")
      (dissoc :isValid)))

(defn ->docker-user
  [docker-user docker-user-info]
  (let [full-name (:full_name docker-user-info)
        org-name (:orgname docker-user-info)]
    (-> (assoc docker-user :type "dockeruser"
                           :name (if (empty? full-name)
                                   org-name
                                   full-name)
                           :role (:type docker-user-info)
                           :location (:location docker-user-info)
                           :company (:company docker-user-info))
        (dissoc :isValid))))