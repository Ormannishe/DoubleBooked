(ns double-booked.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [double-booked.core :as core]))

(def events-with-missing-data
  [{:id 1
    :title "The Rapture"
    :start "2029-11-01T00:00:00.000Z"}
   {:id 2
    :title "The End Of The World"
    :end "2029-12-31T23:59:59.999Z"}
   {:id 3
    :title "Generate New Universe" ;; This takes a full year :)
    :start "2029-01-01T00:00:00.000Z"
    :end "2030-01-01T00:00:00.000Z"}])

(def back-to-back-events
  [{:id 1
    :title "May 5th"
    :start "2024-05-05T00:00:00.000Z"
    :end "2024-05-06T00:00:00.000Z"}
   {:id 2
    :title "May 6th"
    :start "2024-05-06T00:00:00.000Z"
    :end "2024-05-07T00:00:00.000Z"}])

;; Contains several overlapping events, some nested
(def overlapping-events
  [{:id 1
    :title "Litera All Staff Meeting"
    :start "2024-05-03T10:00:00.000Z"
    :end "2024-05-03T11:00:00.000Z"}
   {:id 2
    :title "James / Karen BiWeekly 1:1"
    :start "2024-05-03T10:15:00.000Z"
    :end "2024-05-03T11:45:00.000Z"}
   {:id 3
    :title "OOO Dentist"
    :start "2024-05-03T00:00:00.000Z"
    :end "2024-05-04T00:00:00.000Z"}
   {:id 4
    :title "Quick Sync Up"
    :start "2024-05-03T10:00:00.000Z"
    :end "2024-05-03T10:15:00.000Z"}
   {:id 5
    :title "Lunch"
    :start "2024-05-03T11:00:00.000Z"
    :end "2024-05-03T12:00:00.000Z"}
   {:id 6
    :title "Cottage Weekend"
    :start "2024-05-06T00:00:00.000Z"
    :end "2024-05-08T00:00:00.000Z"}])

(defn pairs->IDs
  "Given a list of paired overlapping events, extract just the IDs in each pair for easier comparison."
  [pairs]
  (map (partial map :id) pairs))

(deftest find-conflicts-test
  ;; I thought about ensuring the function always returns a list as described in its docstring
  ;; but in general I prefer when functions return nil when given invalid input and no exception handling is required
  (testing "returns nil when given nil or an empty list"
    (is (nil? (core/find-conflicts nil)))
    (is (nil? (core/find-conflicts '()))))
  (testing "events that are missing a start or end date are not considered"
    (is (empty? (core/find-conflicts events-with-missing-data))))
  (testing "events which end and start at the exact same time don't actually overlap"
    (is (empty? (core/find-conflicts back-to-back-events))))
  ;; For ease of comparison I've added IDs to the events so we can just compare the set of conflicting IDs instead of the whole event
  (testing "All pairs of overlapping events are returned"
    (let [conflicting-IDs `((1 2) (1 3) (1 4) (2 3) (2 5) (3 4) (3 5))]
      (is (= (pairs->IDs (core/find-conflicts overlapping-events))
             conflicting-IDs)))))
